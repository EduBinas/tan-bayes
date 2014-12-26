package poo.group4;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import poo.group4.patterns.Pattern;
import poo.group4.patterns.TrainSet;

/**
 * A graph, containing {@link Node} objects, connected by {@link Edge} objects.
 * <p>
 * After being created, the complete graph's edges should be created using the {@link #makeComplete()} method. The weight of all edge can be
 * calculated with {@link #updateWeights(ScoreModel)}. The Nodes must have the correct counts before this can be done. After this, a tree can be built
 * using {@link #getMaximumSpanningTree()}. This will create a {@link Tree} object, with the same Node and Edge objects of the graph, but will make
 * the original Graph object inconsistent.
 * 
 * @author Group 4
 */
public class Graph implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/**
	 * Array of the nodes representing <em>input attributes</em> in the graph. It is not guaranteed the nodes are connected. This list is ordered by
	 * the attributes' indexes, and nodeArray[i] will always contain the Node whose attribute has <code>{@link Attribute#index} == i</code>. This does
	 * not include the class output node, which is stored at {@link #outputNode}.
	 */
	ArrayList<Node> nodeArray;

	/** Node representing the output node in the graph. */
	Node outputNode;

	/** Edges connecting the nodes of the graph. No particular order. */
	List<Edge> edges;

	/** Total number of patterns of the used train set. Also known as N. */
	int nbOfTrainPatterns;

	/**
	 * Creates a new graph, with a starting capacity nNodes nodes.
	 * 
	 * @param nNodes
	 *            Starting capacity of the graph.
	 */
	public Graph(int nNodes) {
		this.nodeArray = new ArrayList<Node>(nNodes);
		this.edges = new LinkedList<Edge>();
	}

	/**
	 * Connects all the nodes of the graph between themselves, turning this into a Complete Graph.
	 */
	public void makeComplete() {
		// A complete graph has N(N-1)/2 edges. Create an ArrayList for those
		// for performance.
		int n = nodeArray.size();
		this.edges = new ArrayList<Edge>(n * (n - 1) / 2);

		// Connect every node to the others
		for (int i = 0; i < nodeArray.size(); i++) {
			for (int k = i + 1; k < nodeArray.size(); k++) {

				Edge edge = new Edge(nodeArray.get(i), nodeArray.get(k));
				edges.add(edge);
			}
		}
	}

	/**
	 * Adds a node to the graph. The nodes must be associated with attributes, and the attributes must have indexes from 0 to N-1, where N is the
	 * number of input attributes.
	 * 
	 * @param node
	 *            Node to be added.
	 */
	public void addInputNode(Node node) {
		// nodes.add(node);
		nodeArray.add(node);
	}

	/**
	 * Adds the class output node to the graph. This is necessary for the classifier to be built.
	 * 
	 * @param outputNode
	 *            Output node to be added.
	 */
	public void addOutputNode(Node outputNode) {
		this.outputNode = outputNode;
	}

	/**
	 * Generates a new {@link Tree} object representing the minimum spanning tree of this graph, using the current edges and their weights, which must
	 * be already calculated before. The weights should be calculated with {@link #updateWeights}. Uses the Prim's algorithm.
	 * <p>
	 * <b>Warning</b>: this method will delete the edges (on the nodes' lists) that are not used to make the tree, making this Graph object
	 * inconsistent.<br>
	 * 
	 * @return The minimum spanning tree of this graph.
	 * @see Graph#updateWeights(ScoreModel)
	 */
	public Tree getMaximumSpanningTree() {
		Tree tree = new Tree(this.nodeArray.size());

		// HashSets are very, very fast for .contains() checks
		Set<Node> oldNodes = new HashSet<Node>(this.nodeArray);
		Set<Node> newNodes = new HashSet<Node>(this.nodeArray.size());

		List<Edge> oldEdges = new LinkedList<Edge>(this.edges);

		// Start with arbitrary node:
		// 0 is a nice choice, because most groups did the same, and it's easier to compare trees and results
		Node startingNode = nodeArray.get(0);
		oldNodes.remove(startingNode);

		tree.root = startingNode;
		newNodes.add(startingNode);

		// Repeat until tree contains all the nodes...
		while (newNodes.size() < nodeArray.size()) {

			Edge heaviestEdge = null;

			boolean tempParentIsNew = false;
			boolean tempChildIsNew = false;
			boolean parentIsNew = false;
			boolean childIsNew = false;

			for (Edge e : oldEdges) {
				// Find edge that connects a node in the tree with another node not in the tree...

				tempParentIsNew = (newNodes.contains(e.parent) == false && newNodes.contains(e.child) == true);
				tempChildIsNew = (newNodes.contains(e.parent) == true && newNodes.contains(e.child) == false);

				if (tempParentIsNew || tempChildIsNew) {
					// ...which is also lighter than the one we have right now
					if (heaviestEdge == null || heaviestEdge.weight < e.weight) {
						heaviestEdge = e;
						parentIsNew = tempParentIsNew;
						childIsNew = tempChildIsNew;
					}
				}
			}

			if (heaviestEdge == null)
				break;

			// Kind of inefficient, too many .contains calls
			oldEdges.remove(heaviestEdge);
			tree.edges.add(heaviestEdge);

			if (parentIsNew)
				newNodes.add(heaviestEdge.parent);
			else if (childIsNew)
				newNodes.add(heaviestEdge.child);
			else
				System.err.println("Unexpected: neither A nor B are new nodes?!");
		}

		tree.nodeArray.addAll(nodeArray);
		tree.outputNode = outputNode;
		tree.nbOfTrainPatterns = nbOfTrainPatterns;

		// Re-update nodes' local edge lists
		for (Node node : tree.nodeArray) {
			node.edges.clear();
		}

		for (Edge edge : tree.edges) {
			edge.child.edges.add(edge);
			edge.parent.edges.add(edge);
		}

		return tree;
	}

	/**
	 * Updates all N_ijkc of all nodes present in the graph based on all patterns that exist in a train set.
	 * 
	 * @param set
	 *            The train set to use in the counts.
	 * @throws InconsistentGraphException
	 *             If the graph does not contain all attributes of the set, or if it does not contain the output node.
	 */
	public void updateNodeCounts(TrainSet set) throws InconsistentGraphException {

		/*
		 * Updates all {@link Node#counts} of all Nodes present in nodeArray based on all patterns that exist in {@link PatternSet#patterns}.
		 */

		Collections.sort(nodeArray);

		if (nodeArray.size() != nodeArray.get(nodeArray.size() - 1).attribute.index + 1) {

			throw new InconsistentGraphException("Graph does not contain all attributes of set! Contains " + nodeArray.size() + ", expected "
					+ nodeArray.get(nodeArray.size() - 1).attribute.index + 1);
		}

		if (outputNode == null) {
			throw new InconsistentGraphException("Graph was not associated with an output node!");
		}

		nbOfTrainPatterns = set.getPatternCount();

		int nbOfXAttributes = set.getAttributeCount();

		// Next lines will initialize the variable Node.counts of every Node in this.nodeArray
		int classRange = set.getClassAttribute().getRange();
		// Initialize nodes' counts
		for (int i = 0; i < nbOfXAttributes; i++) {

			nodeArray.get(i).counts = new int[nbOfXAttributes][][][];
			for (int j = 0; j < nbOfXAttributes; j++) {
				nodeArray.get(i).counts[j] = new int[nodeArray.get(j).attribute.getRange()][nodeArray.get(i).attribute.getRange()][classRange];
			}
			nodeArray.get(i).jCounts = new int[nodeArray.get(i).attribute.getRange()][classRange];
		}
		outputNode.cCounts = new int[classRange];

		// Now the listOfTrainPatterns will be iterated and Node.counts will be updated
		Iterator<Pattern> iterator = set.getPatterns().iterator();

		Pattern newPattern;

		while (iterator.hasNext()) {
			newPattern = iterator.next();

			updateNodeCountsFromPattern(set, newPattern);
		}

	}

	/**
	 * Updates {@link Node#counts} and {@link Node#jCounts} of all Nodes present in nodeArray based on a pattern given
	 * 
	 * @param nodeArray
	 * @param newPattern
	 *            TrainPattern that contains new information about a pattern
	 */
	private void updateNodeCountsFromPattern(TrainSet set, Pattern newPattern) {

		int nbOfXAttributes = nodeArray.size();

		outputNode.cCounts[newPattern.getOutput()]++;

		for (int i = 0; i < nbOfXAttributes; i++) { // each Xi
			for (int p = 0; p < nbOfXAttributes; p++) { // each possible parent node

				if (p == i) { // case in which Xi has no parent. We will store this case in the position where node Xi is the parent of itself

					/*
					 * For Node Xi, increment counts' entry that corresponds to: empty parent configuration, node Xi has value pattern[i] and
					 * classNode has value output
					 */
					nodeArray.get(i).counts[i][0][newPattern.getAttributeValue(i)][newPattern.getOutput()]++;
					continue;
				}
				/*
				 * For Node Xi, increment counts' entry that corresponds to: parent p has value pattern[p], node Xi has value pattern[i] and classNode
				 * has value output
				 */
				nodeArray.get(i).counts[p][newPattern.getAttributeValue(p)][newPattern.getAttributeValue(i)][newPattern.getOutput()]++;
			}

			nodeArray.get(i).jCounts[newPattern.getAttributeValue(i)][newPattern.getOutput()]++;
		}
	}

	/**
	 * Updates weights of all edges of the graph, using a specified criterion.
	 * 
	 * @param model
	 *            Score model to be used for weighting.
	 */
	public void updateWeights(ScoreModel model) {
		for (Edge e : edges) {
			e.updateWeight(model, this);
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Graph:\n");

		sb.append("   Nodes:\n");
		for (Node n : nodeArray) {
			sb.append("     ");
			sb.append(n);
			sb.append("\n");
		}

		sb.append("   Edges:\n");
		for (Edge e : edges) {
			sb.append("     ");
			sb.append(e);
			sb.append("\n");
		}

		return sb.toString();
	}
}

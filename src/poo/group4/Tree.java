package poo.group4;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * A tree, containing {@link Node} objects, connected by {@link Edge} objects in a non-circular fashion. A particular case of a Graph.
 * 
 * @author Group 4
 * 
 */
public class Tree extends Graph implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/** Root node of the tree. */
	Node root;

	/**
	 * Creates a new tree, with a starting capacity nNodes nodes. Trees should only be created by graphs, so this is package-visible.
	 * 
	 * @param nNodes
	 *            Number of nodes.
	 */
	Tree(int nNodes) {
		super(nNodes);
	}

	/**
	 * Chooses a node arbitrarily and transforms an undirected tree into a directed one.
	 */
	public void makeDirectedTree() {

		// Iterator<Node> nodeIterator = nodes.iterator();

		// Start with an arbitrary root node
		// Node root = nodeIterator.next();

		// Debug: to match example
		// while (root.attribute.index != 0)
		// root = nodeIterator.next();

		// this.root = root;

		Set<Edge> visitedEdges = new HashSet<Edge>(edges.size());
		Queue<Node> bag = new LinkedList<Node>();
		bag.add(root);

		// Put nodes in a bag, recursively
		while (!bag.isEmpty()) {

			// Get a node
			Node node = bag.poll();

			for (Edge e : node.edges) {

				// Don't visit the same edge twice (don't go up in the tree, only down)
				if (visitedEdges.contains(e))
					continue;

				e.isDirected = true;
				visitedEdges.add(e);

				// Make sure the edges point outwards (down)
				if (!e.parent.equals(node)) {
					e.flip();
				}

				// Add all the children to the bag, so their edges are also flipped outwards
				bag.add(e.child);
			}
		}
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());

		sb.append("Tree layout:\n");
		sb.append(layoutToString());

		return sb.toString();
	}

	/**
	 * Gets a string representation of the tree's structure. Each node's children is indented one level after its parent.
	 * <p>
	 * {@link Tree#toString()} also shows this, along with the list of Nodes and Edges of the tree.
	 * 
	 * @return String representation of the tree.
	 */
	public String layoutToString() {

		StringBuilder sb = new StringBuilder();

		Queue<Node> bag = new LinkedList<Node>();
		Queue<Integer> bagLevel = new LinkedList<Integer>();

		bag.add(root);
		bagLevel.add(1);

		// Put nodes in a bag, recursively
		while (!bag.isEmpty()) {

			// Get a node
			Node node = bag.poll();
			int level = bagLevel.poll();

			for (int i = 0; i < level; i++)
				sb.append("   ");

			sb.append(node.toString(false));
			sb.append("\n");

			for (Edge e : node.edges) {

				// Add all the children to the bag
				if (e.parent.equals(node)) {
					bag.add(e.child);
					bagLevel.add(level + 1);
				}
			}
		}

		return sb.toString();
	}

	/**
	 * Calculates the OFE (Observed Frequency Estimates) of the nodes in the tree, and stores them in each node's {@link Node#parameters} field.
	 * 
	 */
	public void updateParameters() {

		for (Node node : nodeArray) {

			// Find the node's parent to know how many parameters there will be
			// If the node has no parent, there is only one row of parameters
			Node parent = getParent(node);
			int parentRange = parent == null ? 1 : parent.attribute.getRange();
			int parentIndex = parent == null ? node.attribute.index : parent.attribute.index;

			node.parameters = new double[parentRange][node.attribute.getRange()][outputNode.attribute.getRange()];

			for (int j = 0; j < node.parameters.length; j++) {
				for (int k = 0; k < node.parameters[j].length; k++) {
					for (int c = 0; c < node.parameters[j][k].length; c++) {

						int count = node.counts[parentIndex][j][k][c];

						int kCount = 0;

						// If the node has no parent, counting the times
						// "the parent has any configuration and C has c-th value"
						// is just counting the times "c has c-th value"
						if (parent != null)
							kCount = parent.jCounts[j][c];
						else
							kCount = outputNode.cCounts[c];

						node.parameters[j][k][c] = (count + 0.5) / (kCount + node.attribute.getRange() * 0.5);

					}
				}
			}
		}

		// Update node c with OFE (Observed Frequency Estimates)
		int classRange = outputNode.attribute.getRange();

		outputNode.cParameters = new double[classRange];
		for (int i = 0; i < classRange; i++) {
			outputNode.cParameters[i] = (outputNode.cCounts[i] + 0.5) / (nbOfTrainPatterns + classRange * 0.5);
		}
	}

	/**
	 * Gets the parent of a given node. Returns null if the node has no parents.
	 * 
	 * @param node
	 *            Child node
	 * @return Parent of node, or null if none is found.
	 */
	public Node getParent(Node node) {
		for (Edge e : node.edges) {
			if (node.equals(e.child))
				return e.parent;
		}
		return null;
	}

}

package poo.group4;

import java.io.Serializable;

/**
 * A directed edge of a graph or tree. Connects two {@link Node} objects of the graph/tree. Holds information about counts for computing network
 * parameter. This can also represent an undirected edge.
 * 
 * @author Group 4
 */
public class Edge implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/** Node connected by this edge. Has j possible values. */
	Node parent;
	/** Node connected by this edge. Has k possible values. */
	Node child;

	/** Indicates if the edge is directed. If true, the direction is {@link #parent} -> {@link #child} */
	boolean isDirected;

	/** Weight of this node. */
	double weight;

	/**
	 * Connects two nodes of a graph, and creates a directed Edge object representing that connection. The direction will be from parent to child.
	 * Updates the nodes that were connected.
	 * 
	 * @param parent
	 *            Node to be connected. Its list of edges will be updated.
	 * @param child
	 *            Node to be connected. Its list of edges will be updated.
	 * @param isDirected
	 *            True if the edge is directed, with the direction parent->child.
	 */
	public Edge(Node parent, Node child, boolean isDirected) {
		this.parent = parent;
		this.child = child;
		this.isDirected = isDirected;

		if (parent.equals(child)) {
			System.err.println("Tried to add circular edge!");
		}

		parent.edges.add(this);
		child.edges.add(this);
	}

	/**
	 * Connects two nodes of a graph, and creates an undirected Edge object representing that connection. Updates the nodes that were connected.
	 * 
	 * @param a
	 *            Node to be connected. Its list of edges will be updated.
	 * @param b
	 *            Node to be connected. Its list of edges will be updated.
	 */
	public Edge(Node a, Node b) {
		this(a, b, false);
	}

	/**
	 * Updates the weight of this edge, according to a given score model.
	 * 
	 * @param model
	 *            Model object that will be used to calculate the score.
	 * @param graph
	 *            Graph that contains this edge.
	 */
	public void updateWeight(ScoreModel model, Graph graph) {
		this.weight = model.calculate(this, graph);
	}

	/**
	 * Flips the edge, switching the parent and child nodes. This will be done even if the edge is not directed. Does not update the weight.
	 */
	public void flip() {
		Node tempParent = parent;
		parent = child;
		child = tempParent;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((parent == null) ? 0 : parent.hashCode()) + ((child == null) ? 0 : child.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	/**
	 * Returns true if the two edges connect the same two nodes.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;

		if (parent == null || child == null)
			return false;

		if ((parent.equals(other.parent) && child.equals(other.child)) || ((parent.equals(other.child) && child.equals(other.parent))))
			return true;

		return false;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Edge [" + parent.toString(false) + (isDirected ? " --> " : " <-> ") + child.toString(false) + "]";
	}

}

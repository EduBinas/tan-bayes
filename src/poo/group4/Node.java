package poo.group4;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * A node of a graph or tree, representing an {@link Attribute}. Holds a list of edges which is connected to.
 * 
 * @author Group 4
 */
public class Node implements Serializable, Comparable<Node> {

	/**  */
	private static final long serialVersionUID = 1L;

	/** Attribute held inside this node. */
	final Attribute attribute;

	/** Edges to which this node is connected to. */
	List<Edge> edges;

	/**
	 * Counts needed to compute the network parameters. Indexed by [p][j][k][c].
	 * <p>
	 * p: index of the parent attribute<br>
	 * j: configuration of the parent's attribute <br>
	 * k: configuration of this node's attribute <br>
	 * c: c-th value of the class (output)<br>
	 * <p>
	 * The value of counts[p][j][k][c] is the number of Train Patterns which have the parent's attribute and own attribute with values j and k,
	 * respectively, and an output of c.
	 * <p>
	 * If p corresponds to the Node itself (p==attribute.index), we consider it as having no parent, i.e., case with empty parent configuration.
	 */
	int[][][][] counts;

	/**
	 * The value of jCounts[k][c] is the number of Train Patterns in which this attribute takes its k-th value, and the class variable takes its c-th
	 * value. Needed to compute the network parameters.
	 * <p>
	 * k: value of this attribute<br>
	 * c: value of the class output C<br>
	 */
	int[][] jCounts;

	/**
	 * The value of cCounts[c] is the number of Train Patterns in which the node C takes the c-th value. Needed to compute the network parameters.
	 * <p>
	 * c: value of the class output C<br>
	 */
	int[] cCounts;

	/**
	 * Observed frequency estimates for the attribute of this node. Indexed by [j][k][c].
	 * <p>
	 * j: configuration of the parent's attribute <br>
	 * k: configuration of this node's attribute <br>
	 * c: c-th value of the class (output)<br>
	 * <p>
	 * The value of parameters[j][k][c] is an estimation of the probability of this node having its k-th value, knowing that its parent has its j-th
	 * value, and the output class is the c-th value.
	 * <p>
	 */
	double[][][] parameters;

	/**
	 * Observed frequency estimates for the attribute of the class node. Indexed by [c].
	 * <p>
	 * c: c-th value of the class (output)<br>
	 * <p>
	 * The value of parameters[c] is an estimation of the probability of this node having its c-th value
	 * <p>
	 */
	double[] cParameters;

	/**
	 * Creates a new Node object, associated with an attribute of the learning system, and with no connections to any other Nodes.
	 * 
	 * @param attribute
	 *            Attribute object which this node will represent.
	 */
	public Node(Attribute attribute) {
		this.attribute = attribute;
		this.edges = new LinkedList<Edge>();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attribute == null) ? 0 : attribute.hashCode());
		return result;
	}

	/**
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
		Node other = (Node) obj;
		if (attribute == null) {
			if (other.attribute != null)
				return false;
		} else if (!attribute.equals(other.attribute))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Node " + (attribute != null ? attribute.name : "(null)") + ": counts=" + Arrays.deepToString(counts) + "]";
	}

	/**
	 * Returns a string representation of the node, including optionally the counts needed to the learning process.
	 * 
	 * @param printCounts
	 *            If true, the string will include the counts.
	 * @return A string with the node's attribute's name, and optionally the counts.
	 */
	public String toString(boolean printCounts) {
		if (printCounts)
			return toString();
		else
			return "Node " + attribute != null ? attribute.name : "(null)";
	}

	/**
	 * Compares the Nodes using their {@link Attribute} field's comparator.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Node o) {
		return this.attribute.compareTo(o.attribute);
	}

}

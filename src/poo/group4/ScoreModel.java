package poo.group4;

/**
 * Abstract class. Represents a Score Model for calculating weights of a certain edge in a graph.
 * 
 * @author Group 4
 */
public abstract class ScoreModel {

	/**
	 * Calculates the weight of an edge in a graph, according to a certain Score Model.
	 * 
	 * @param edge
	 *            Edge to calculate the weight of.
	 * @param graph
	 *            Graph containing all the necessary counts to calculate the score.
	 * @return Weight of the edge.
	 */
	public abstract double calculate(Edge edge, Graph graph);

	/** Constant used many times in function {@link #log2(double)}. Stores the logarithm base-e of 2. */
	protected static final double ln2 = Math.log(2);

	/**
	 * Helper function. Calculates the logarithm base-2 of a value.
	 * 
	 * @param number
	 *            Number to calculate log2 of.
	 * @return Equivalent to ln(number) / ln(2)
	 */
	protected final double log2(double number) {

		double log2Value = Math.log(number) / ScoreModel.ln2;

		if (Double.isNaN(log2Value))
			throw new RuntimeException("Tried to calculate log2(" + number + "): NaN");

		return log2Value;
	}
}

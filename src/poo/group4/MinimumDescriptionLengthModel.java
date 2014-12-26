package poo.group4;

/**
 * Calculates the weight of an edge in the graph using the Minimum Description Length model, using the conditional mutual information between the two
 * nodes of an edge, conditioned on the output class. This is a penalized version of the Log-Likelihood model. This weight is symmetrical: given two
 * Nodes A and B, the directed edges connecting A->B and B->A will have the same weight.
 * 
 * @author Group 4
 */
public class MinimumDescriptionLengthModel extends LogLikelihoodModel {

	/*
	 * Since the MDL uses the same formula as the LL, it is a subclass of LogLikelihoodModel, and overrides the calculate method, to account for the
	 * penalizing value.
	 */

	@Override
	public double calculate(Edge edge, Graph graph) {

		double original = super.calculate(edge, graph);

		double s = graph.outputNode.attribute.getRange();
		double N = graph.nbOfTrainPatterns;

		double penalization = s * (edge.child.attribute.getRange() - 1) * (edge.parent.attribute.getRange() - 1) / 2 * Math.log(N);

		return original - penalization;

	}

}

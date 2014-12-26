package poo.group4;

/**
 * Calculates the weight of an edge in the graph using the Log-Likelihood model, using the conditional mutual information between the two nodes of an
 * edge, conditioned on the output class. This weight is symmetrical: given two Nodes A and B, the directed edges connecting A->B and B->A will have
 * the same weight.
 * 
 * @author Group 4
 */
public class LogLikelihoodModel extends ScoreModel {

	public double calculate(Edge edge, Graph graph) {

		double weight = 0;

		for (int j = 0; j < edge.parent.attribute.getRange(); j++) {

			for (int k = 0; k < edge.child.attribute.getRange(); k++) {

				for (int c = 0; c < graph.outputNode.attribute.getRange(); c++) {

					int Nijkc = edge.child.counts[edge.parent.attribute.index][j][k][c];
					int N = graph.nbOfTrainPatterns;

					int Nc = graph.outputNode.cCounts[c];

					int Nikc_J = edge.child.jCounts[k][c];
					int Nijc_K = edge.parent.jCounts[j][c];

					// Try to calculate only if counts are not zero, otherwise log2() would explode to NaN
					if (Nijkc != 0 && Nc != 0) {

						weight += (double) Nijkc / N * log2((double) (Nijkc * Nc) / (Nikc_J * Nijc_K));
					}
				}
			}
		}

		return weight;
	}
}

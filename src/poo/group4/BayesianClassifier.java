package poo.group4;

import java.util.Iterator;

import poo.group4.patterns.Pattern;
import poo.group4.patterns.TestSet;

/**
 * Helper class. Provides methods for classifying a test pattern, using a classifier.
 * 
 * @author Group 4
 * 
 */
public final class BayesianClassifier {

	/**
	 * {@link BayesianClassifier} objects cannot be created.
	 */
	private BayesianClassifier() {
	}

	/**
	 * Calculates the probability P_B(X1, ..., Xn, C): the probability of a certain pattern occurring. Uses the learned parameters of the classifier.
	 * 
	 * @param testPattern
	 *            Test pattern to be considered
	 * @param outputValue
	 *            Value of the output C.
	 * @param tree
	 *            Tree representing the classifier
	 * @return The probability P_B(X1, ..., Xn, C).
	 */
	private static double calculateJointProbability(Pattern testPattern, int outputValue, Tree tree) {

		double probabilityB = 1;

		probabilityB *= tree.outputNode.cParameters[outputValue];

		for (Node node : tree.nodeArray) {

			int j;
			int k;

			Node parent = tree.getParent(node);
			if (parent == null) {
				j = 0;
			} else {
				j = testPattern.getAttributeValue(parent.attribute.index);

			}
			k = testPattern.getAttributeValue(node.attribute.index);

			probabilityB *= node.parameters[j][k][outputValue];
		}

		return probabilityB;
	}

	// Removed out of redundancy
	/**
	 * Calculates the sum all probabilities for all values of the output C.
	 * 
	 * @param probabilities
	 *            Array containing all the probabilities for all output values of C.
	 */
	/*
	 * private static double sumOfProbabilities(double[] probabilities) { double probabilitySum = 0; for (int i = 0; i < probabilities.length; i++) {
	 * probabilitySum += probabilities[i]; } if (probabilitySum == 0) System.err.println("The sum of probabilities cannot be zero."); return
	 * probabilitySum; }
	 */

	/**
	 * Classifies a single test pattern using the classifier. Sets the pattern's {@link Pattern#output} field to the classified value, and the
	 * 
	 * @param testPattern
	 *            Pattern to be classified.
	 * @param tree
	 *            Tree of the classifier.
	 */
	private static void classifyTestPattern(Pattern testPattern, Tree tree) {

		double[] probabilities = new double[tree.outputNode.attribute.getRange()];

		double highestProbability = 0;

		for (int c = 0; c < tree.outputNode.attribute.getRange(); c++) {
			probabilities[c] = calculateJointProbability(testPattern, c, tree);
			if (probabilities[c] > highestProbability) {
				highestProbability = probabilities[c];
				testPattern.setOutput(c);
			}		
		}
	}

	/**
	 * Runs the Tree Augmented Bayesian Classifier to classify the patterns on a test set.
	 * 
	 * @param testData
	 *            The test patterns. The field {@link Pattern#output} of each TestPattern will be set with the classified value.
	 * @param tree
	 *            The tree representing the classifier.
	 */
	public static void classifyTestData(TestSet testData, Tree tree) {

		Iterator<Pattern> iterator = testData.getPatterns().iterator();

		Pattern newTestPattern;

		while (iterator.hasNext()) {
			newTestPattern = iterator.next();
			classifyTestPattern(newTestPattern, tree);
		}
	}

}

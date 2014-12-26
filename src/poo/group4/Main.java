package poo.group4;

import java.io.File;

import poo.group4.patterns.Pattern;
import poo.group4.patterns.TestSet;
import poo.group4.patterns.TrainSet;

/**
 * Entry point for the OOP Project - Tree Augmented Naive Bayes Classifier. Provides a command-line executable with arguments:
 * <code>trainfile testfile scoremodel</code>.
 * 
 * @author Group 4
 * 
 */
public class Main {

	/**
	 * Entry point of the program. The command-line arguments are as follows:
	 * <ul>
	 * <li><b>Train</b>: File containing the train set, in CSV format
	 * <li><b>Test</b>: File containing the test set, in CSV format
	 * <li><b>Score</b>: Score model to use to choose the tree. "LL" for Log-Likelihood, or "MDL" for Minimum Description Length
	 * </ul>
	 * 
	 * @param args
	 *            Command-line arguments.
	 */
	public static void main(String[] args) {

		if (args.length == 0) {
			Menu.startGui();
			return;
		}
		if (args.length != 3) {
			System.out.println("Arguments: train test score");
			System.exit(1);
		}

		// Start counting the time for building the classifier
		long startTime = System.currentTimeMillis();

		// Load train set from disk
		File trainDataFile = new File(args[0]);
		TrainSet trainData = null;
		try {
			trainData = new TrainSet(trainDataFile);
		} catch (Exception e) {
			System.err.println("Error during parsing of " + trainDataFile.getName());
			e.printStackTrace();
			System.exit(-1);
		}

		ScoreModel scoreModel = null;
		if ("LL".equals(args[2])) {
			scoreModel = new LogLikelihoodModel();
		} else if ("MDL".equals(args[2])) {
			scoreModel = new MinimumDescriptionLengthModel();
		} else {
			System.err.println("Score argument must be LL or MDL.");
			System.exit(1);
		}

		// Generate and train the classifier using the train set and the specified score model
		TreeAugmentedNaiveBayesClassifier classifier = null;
		try {
			classifier = new TreeAugmentedNaiveBayesClassifier(trainData, scoreModel);
		} catch (InconsistentGraphException e1) {
			System.err.println("Error building the classifier:");
			e1.printStackTrace();
			System.exit(-1);
		}

		long endTime = System.currentTimeMillis();
		System.out.println("Building the classifier: " + (endTime - startTime) / 1000.0 + " seconds");

		// Load the test set from disk to classify it
		File testDataFile = new File(args[1]);
		TestSet testData = null;
		try {
			testData = new TestSet(testDataFile, trainData);
		} catch (Exception e) {
			System.err.println("Error during parsing of " + trainDataFile.getName() + ": " + e.getMessage());
			System.exit(-1);
		}

		classifier.classify(testData);

		System.out.println("Testing the classifier:");

		// Output as specified in the project statement
		int i = 0;
		for (Pattern tp : testData.getPatterns()) {
			System.out.println("-> instance " + i + ":\t" + tp.getOutput());
			i++;
		}
	}

}

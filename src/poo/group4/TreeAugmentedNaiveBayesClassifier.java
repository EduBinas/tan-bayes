package poo.group4;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import poo.group4.patterns.TestSet;
import poo.group4.patterns.TrainSet;

/**
 * A tree-augmented naive Bayes classifier. Allows several scoring models to be used.
 * 
 * @author Group 4
 * 
 */
public class TreeAugmentedNaiveBayesClassifier implements Serializable {

	/** Serial version unique identifier; auto-generated. */
	private static final long serialVersionUID = 8043027288642563827L;

	/** Supporting tree for the classifier. */
	Tree tree;

	/**
	 * Creates a Tree augmented naive Bayes classifier, and trains it.
	 * 
	 * @param trainData
	 *            Data patterns to be used in training.
	 * @param model
	 *            Score model used to generate the tree.
	 * @throws InconsistentGraphException
	 *             The attributes' indexes on the trainData are not sequential, starting from 0.
	 */
	public TreeAugmentedNaiveBayesClassifier(TrainSet trainData, ScoreModel model) throws InconsistentGraphException {

		// Make a complete graph will all the attributes
		Graph graph = trainData.getGraph();
		graph.makeComplete();

		// Make counts and calculate weights for the scores
		graph.updateNodeCounts(trainData);
		graph.updateWeights(model);

		// Make tree based on scores and turn undirected tree into directed one
		tree = graph.getMaximumSpanningTree();
		tree.makeDirectedTree();

		// Calculate learning parameters
		tree.updateParameters();
	}

	/**
	 * Classifies a test set using the classifier, according to the training used in the constructor.
	 * 
	 * @param testData
	 *            Data patterns to be used in testing.
	 */
	public void classify(TestSet testData) {

		BayesianClassifier.classifyTestData(testData, tree);

	}

	/**
	 * Saves the fully-trained classifier to a file, for later testing. This saves all the calculated counts and parameters.
	 * 
	 * @param file
	 *            File where the classifier will be saved to.
	 * @throws FileNotFoundException
	 *             if the file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any
	 *             other reason.
	 * @throws IOException
	 *             if an I/O error occurs while writing the classifier.
	 */
	public void saveClassifier(File file) throws FileNotFoundException, IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));

		oos.writeObject(this);

		oos.close();
	}

	/**
	 * Loads a fully-trained classifier from a file, to be used in testing.
	 * 
	 * @param file
	 *            File where the classifier has been saved.
	 * @return A classifier object, ready to be used in training.
	 * @throws FileNotFoundException
	 *             if the file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading.
	 * @throws IOException
	 *             if an I/O error occurs while reading the classifier.
	 * @throws ClassNotFoundException
	 *             One of the classes of the objects composing the classifier could not be found.
	 */
	public static TreeAugmentedNaiveBayesClassifier loadClassifier(File file) throws FileNotFoundException, IOException, ClassNotFoundException {

		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));

		TreeAugmentedNaiveBayesClassifier classifier = (TreeAugmentedNaiveBayesClassifier) ois.readObject();

		ois.close();
		return classifier;
	}
}

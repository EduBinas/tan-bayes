package poo.group4.patterns;

import java.io.File;
import java.io.IOException;

import poo.group4.Attribute;
import poo.group4.DataSetParsingException;
import poo.group4.Graph;
import poo.group4.Node;

/**
 * Stores the train data set for the classifier.
 * 
 * @author Group 4
 * 
 */
public class TrainSet extends PatternSet {

	/** Graph that will contain nodes representing all the attributes read from the input file. Will then be used to build and train the classifier. */
	private Graph graph;

	/**
	 * Creates a TrainData file with train patterns read from a CSV file.
	 * 
	 * @param file
	 *            CSV file containing the train patterns.
	 * @throws IOException
	 *             If the file could not be opened
	 * @throws DataSetParsingException
	 *             If the input file has lines with the wrong size
	 */
	public TrainSet(File file) throws DataSetParsingException, IOException {
		super(file, true, null);

		graph = new Graph(getAttributeCount());

		for (int i = 0; i < attributes.length; i++) {
			graph.addInputNode(new Node(attributes[i]));
		}

		graph.addOutputNode(new Node(outputAttribute));

	}

	@Override
	protected void addPattern(Pattern pattern, TrainSet reference) throws DataSetParsingException {

		if (pattern.hasOutput() == false) {
			System.out.println("Tried to add pattern with no output to a TrainSet.");
			return;
		}

		// Update all attributes' ranges
		for (int i = 0; i < pattern.pattern.length; i++) {
			if (pattern.pattern[i] + 1 > attributes[i].getRange())
				attributes[i].setRange(pattern.pattern[i] + 1);
		}

		// Update the output node's range
		if (pattern.output + 1 > outputAttribute.getRange())
			outputAttribute.setRange(pattern.output + 1);

		super.addPattern(pattern, reference);
	}

	/**
	 * Gets the attribute representing the output of the classifier (class).
	 * 
	 * @return Output (class) attribute of the classifier.
	 */
	public Attribute getClassAttribute() {
		return outputAttribute;
	}

	/**
	 * Gets a graph built based on this train set's attributes, ready to be used for building a tree-augmented Bayes classifier.
	 * 
	 * @return A graph with the attributes of the train set as notes.
	 */
	public Graph getGraph() {
		return graph;
	}

	@Override
	public String toString() {
		return "TrainData [patterns=" + patterns + "]";
	}

}

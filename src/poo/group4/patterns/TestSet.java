package poo.group4.patterns;

import java.io.File;
import java.io.IOException;

import poo.group4.DataSetParsingException;

/**
 * Stores a test data set to be classified by the classifier.
 * 
 * @author Group 4
 * 
 */
public class TestSet extends PatternSet {

	/**
	 * Creates a TestData object storing the test data set to be classified, read from a CSV file.
	 * 
	 * @param file
	 *            CSV file containing the data of the test set.
	 * @param baseTrainData
	 *            Used for dimension check.
	 * @throws IOException
	 *             If the file could not be opened
	 * @throws DataSetParsingException
	 *             If the input file has lines with the wrong size
	 */
	public TestSet(File file, TrainSet baseTrainData) throws DataSetParsingException, IOException {

		super(file, false, baseTrainData);
	}

	@Override
	protected void addPattern(Pattern pattern, TrainSet reference) throws DataSetParsingException {

		// Check read pattern against expected ranges, from a previously read set
		for (int i = 0; i < pattern.pattern.length; i++) {
			if (reference != null && pattern.pattern[i] + 1 > reference.attributes[i].getRange()) {

				throw new DataSetParsingException("Value of attribute " + reference.attributes[i].getName() + " is out of range");
			}
		}

		super.addPattern(pattern, reference);
	}

	@Override
	public String toString() {
		return "TestData: [listOfTestPatterns = \n" + patterns + "]";
	}

}

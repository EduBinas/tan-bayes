package poo.group4.patterns;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import poo.group4.Attribute;
import poo.group4.DataSetParsingException;

/**
 * A set of learning patterns. Contains information about the attributes of the data set, and a list of patterns. This may represent a train set
 * and/or a test set, depending on how it is used.
 * 
 * @author Group 4
 * 
 */
public class PatternSet {

	/** The patterns of this set, in no particular order. */
	protected List<Pattern> patterns;

	/** The input attributes of this set, in the order they were read. */
	protected Attribute[] attributes;

	/** The output attribute of the set, also known as C. */
	protected Attribute outputAttribute;

	/**
	 * Gets the list of patterns contained in this set.
	 * 
	 * @return List of patterns of this set.
	 */
	public List<Pattern> getPatterns() {
		return patterns;
	}

	/**
	 * No-args constructor, for eventual PatternSets that do not load information from CSV files.
	 */
	public PatternSet() {
	}

	/**
	 * Creates a new PatternSet, from a CSV file. Optionally accepts a reference set, to check for out-of-range values in a test set.
	 * 
	 * @param file
	 *            CSV file of the set
	 * @param hasOutput
	 *            True if the data file contains the expected output on the last column of the data
	 * @param reference
	 *            An already-built set for checking. Optional, may be null.
	 * @throws DataSetParsingException
	 *             If the input file has lines with the wrong size
	 * @throws IOException
	 *             If the file could not be opened
	 */
	public PatternSet(File file, boolean hasOutput, TrainSet reference) throws DataSetParsingException, IOException {

		BufferedReader in;
		patterns = new LinkedList<Pattern>();

		in = new BufferedReader(new FileReader(file));

		String line;
		int nbOfXAttributes = 0;

		try {
			boolean foundVariableDescription = false;
			while ((line = in.readLine()) != null) {
				if (!foundVariableDescription) {
					if (line.equals(""))
						continue;
					else { /* found the initial line that corresponds to the attributes description */
						foundVariableDescription = true;
						String[] parts = line.split("\\s*,\\s*");

						// Excludes the output class attribute, if it exists
						if (hasOutput)
							nbOfXAttributes = parts.length - 1;
						else
							nbOfXAttributes = parts.length;

						attributes = new Attribute[nbOfXAttributes];

						for (int i = 0; i < nbOfXAttributes; i++) {
							attributes[i] = new Attribute(parts[i], i);
						}

						if (hasOutput) {
							outputAttribute = new Attribute(parts[nbOfXAttributes], nbOfXAttributes);
						}
					}
					continue;
				}
				if (!line.equals(""))
					addPattern(new Pattern(line, hasOutput), reference);
				else
					continue;
			}

			in.close();

			if (patterns.size() == 0) {
				throw new DataSetParsingException("File " + file.getName() + " does not contain any valid patterns.");
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	protected void addPattern(Pattern pattern, TrainSet reference) throws DataSetParsingException {

		patterns.add(pattern);
	}

	/**
	 * Gets the number of input attributes of the set. This does not include the output attribute.
	 * 
	 * @return The number of input attributes in the set.
	 */
	public int getAttributeCount() {
		return attributes.length;
	}

	/**
	 * Gets the number of patterns in the set. Also known as N.
	 * 
	 * @return the number of patterns in a set.
	 */
	public int getPatternCount() {
		return patterns.size();
	}
}

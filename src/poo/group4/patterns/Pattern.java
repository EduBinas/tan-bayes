package poo.group4.patterns;

import java.util.Arrays;

import poo.group4.DataSetParsingException;

/**
 * Abstract class. Stores a single pattern, with a value for each attribute. May also store the output class.
 * <p>
 * The pattern can be created from a raw CSV line from an input file.
 * 
 * @author Group 4
 * 
 */
public class Pattern {

	/** Value of each attribute, in the same order as the input file. */
	final int[] pattern;

	/**
	 * Gets the value of a certain attribute, specified by its index, in this pattern.
	 * 
	 * @param index
	 *            Index of the attribute.
	 * @return Value of the attribute, in this pattern.
	 */
	public int getAttributeValue(int index) {
		return pattern[index];
	}

	/** Value of class output, C. Is only valid if {@link #hasOutput()} is true. */
	int output;

	/**
	 * Indicates if this pattern has an associated output value. The method {@link #setOutput(int)} makes this value true. This value can come from a
	 * predefined train set, or from a classifier.
	 */
	boolean hasOutput;

	/**
	 * Checks if this pattern has a valid output value.
	 * 
	 * @return True if the output value is valid.
	 */
	public boolean hasOutput() {
		return hasOutput;
	}

	/**
	 * Gets the value of the output attribute for this pattern, or -1 if {@link #hasOutput()} is false.
	 * 
	 * @return Value of the output attribute for this pattern.
	 */
	public int getOutput() {
		if (hasOutput)
			return output;
		else
			return -1;
	}

	/**
	 * Sets the output for this pattern. Makes the value valid. After calling this, {@link #hasOutput()} will return true. This method does not make
	 * any verification about the correctness of the provided value.
	 * 
	 * @param value
	 *            The new output value.
	 */
	public void setOutput(int value) {
		output = value;
		hasOutput = true;
	}

	/**
	 * Creates a Pattern based on a line from an input file, separated by commas. It is only invoked from the subclasses constructors. Only checks if
	 * dimensions are correct for either a TrainPattern or a TestPattern. Only updates {@link #output} if the parameter <code>hasOutput</code> is
	 * <code>true</code>.
	 * 
	 * @param line
	 *            Line coming directly from the input file, containing one value for each attribute, and eventually one value for the class output.
	 * @param hasOutput
	 *            True if the line's last value is the C output. For patters of the train set.
	 * @throws DataSetParsingException
	 *             If a line in the input files has malformatted numbers.
	 */
	public Pattern(String line, boolean hasOutput) throws DataSetParsingException {

		String[] parts = line.split("\\s*,\\s*");

		pattern = new int[parts.length - (hasOutput ? 1 : 0)];

		try {
			for (int i = 0; i < pattern.length; i++) {
				pattern[i] = Integer.parseInt(parts[i]);
			}

			if (hasOutput) {
				output = Integer.parseInt(parts[pattern.length]);
				this.hasOutput = true;
			}
		} catch (NumberFormatException e) {
			throw new DataSetParsingException("A line in the input files had malformatted numbers: " + line, e);

		}
	}

	/**
	 * Returns a string representation of the pattern, in a format ready to be written to an output file, in the form
	 * <p>
	 * [x1, x2, x3, ..., xn], c = output
	 * </p>
	 * where <code>xn</code> are the attributes' values, and <code>output</code> is the output's value.
	 * 
	 * @param pretty
	 *            True to use a pretty format. False to use the regular {@link #toString()} format.
	 * @return an output-file-format string representation of the pattern.
	 */
	public String toString(boolean pretty) {

		if (pretty)
			return Arrays.toString(pattern) + ", c = " + output;
		else
			return this.toString();

	}
}

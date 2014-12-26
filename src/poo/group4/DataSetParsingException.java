package poo.group4;

/**
 * Indicates an exception during the parsing of the CSV files containing the train or test data.
 * 
 * @author Group 4
 * 
 */
public class DataSetParsingException extends Group4Exception {

	/** */
	private static final long serialVersionUID = 4643811151746881021L;

	/**
	 * Constructs a new exception with the specified detail message.
	 * 
	 * @see Group4Exception#Group4Exception(String)
	 * @param message
	 *            the detail message. The detail message is saved for later retrieval by the getMessage() method.
	 */
	public DataSetParsingException(String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.
	 * 
	 * @see Exception#Exception(String, Throwable)
	 * @param message
	 *            The detail message. The detail message is saved for later retrieval by the getMessage() method.
	 * @param cause
	 *            The cause (which is saved for later retrieval by the getCause() method). (A null value is permitted, and indicates that the cause is
	 *            nonexistent or unknown.)
	 */
	public DataSetParsingException(String message, Throwable cause) {
		super(message, cause);
	}

}

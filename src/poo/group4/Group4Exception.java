package poo.group4;

/**
 * A general exception that this project may throw.
 * 
 * @author Group 4
 * 
 */
public abstract class Group4Exception extends Exception {

	/**  */
	private static final long serialVersionUID = -7932774217304337719L;

	/**
	 * Constructs a new exception with the specified detail message.
	 * 
	 * @see Exception#Exception(String)
	 * @param message
	 *            The detail message. The detail message is saved for later retrieval by the getMessage() method.
	 */
	public Group4Exception(String message) {
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
	public Group4Exception(String message, Throwable cause) {
		super(message, cause);
	}

}

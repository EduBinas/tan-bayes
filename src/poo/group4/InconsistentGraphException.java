package poo.group4;

/**
 * Indicates that a graph does not contain all attributes of a set, or if it does not contain the output node.
 * 
 * @author Group 4
 * 
 */
public class InconsistentGraphException extends Group4Exception {

	/**	*/
	private static final long serialVersionUID = -6794675476230591365L;

	/**
	 * Constructs a new exception with the specified detail message.
	 * 
	 * @see Group4Exception#Group4Exception(String)
	 * @param message
	 *            the detail message. The detail message is saved for later retrieval by the getMessage() method.
	 */
	public InconsistentGraphException(String message) {
		super(message);
	}

}

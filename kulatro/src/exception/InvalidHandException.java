package exception;

/**
 * This exception is a subclass of CardGameException, and is thrown when user's hand is invalid,
 * such as the hand consists more than four, or less than 1 cards.
 * @author Muhammed Cagan Goktas
 */
public class InvalidHandException extends CardGameException {

	private static final long serialVersionUID = 103L;

	/**
	 * Constructs a new InvalidHandException with the specified detail message.
	 * @param message the detail message explaining the cause of the exception
	 */
	public InvalidHandException(String message) {
        super("Invalid Hand: " + message);
    }
}
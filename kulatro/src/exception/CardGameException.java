package exception;

/**
 * This is the superclass of exceptions for this game.
 * @author Muhammed Cagan Goktas
 */
public class CardGameException extends Exception {

	private static final long serialVersionUID = 100L;

	/**
	 * Constructs a new CardGameException with the specified detail message.
	 * @param message the detail message explaining the cause of the exception
	 */
	public CardGameException(String message) {
        super(message);
    }
}
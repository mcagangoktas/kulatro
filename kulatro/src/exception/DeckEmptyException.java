package exception;

/**
 * This exception is a subclass of CardGameException, and is thrown when there is no card left in the deck.
 * @author Muhammed Cagan Goktas
 */
public class DeckEmptyException extends CardGameException {
	private static final long serialVersionUID = 101L;

	/**
	 * Constructs a new DeckEmptyException with the specified detail message.
	 */
	public DeckEmptyException() {
        super("Deck and discard pile are empty! Cannot draw a new card.");
    }
}

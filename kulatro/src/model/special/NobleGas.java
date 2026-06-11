package model.special;
import model.*;
import java.util.List;

/**
 * Represents the Noble Gas special card belonging to the Element deck theme.
 * This card locks a designated card in place, preventing it from being discarded 
 * while doubling its numeric value during score calculations.
 * @author Muhammed Cagan Goktas
 */
public class NobleGas extends SpecialCard {
	
	/**
     * Constructs a new Noble Gas special card and assigns it to the Element deck.
     */
    public NobleGas() {
    	super("Noble Gas", DeckType.ELEMENT);
    }

    /**
     * Modifies the final calculated score. For Noble Gas, the direct scoring impact 
     * is resolved by the scoring engine based on the card's locked status, 
     * so this returns the current score unchanged.
     * @param currentScore the baseline calculated score
     * @param hand the current list of cards in the player's hand
     * @return the unchanged current integer score
     */
    @Override
    public int applyScoreEffect(int currentScore, List<Card> hand) {
        return currentScore;
    }

    /**
     * Executes the unique action when the card is played. 
     * For Noble Gas, the locking behavior is triggered explicitly via the {@link #executeLock} method.
     * @param player the current active player
     * @param deck the active deck instance
     */
    @Override
    public void performAction(Player player, Deck deck) {}

    /**
     * Locks a specific numbered card in place, preventing it from being discarded 
     * and marking it for double value evaluation.
     * @param targetCard the {@link Card} to be locked in the player's hand
     */
    public void executeLock(Card targetCard) {
        if (targetCard instanceof NumberCard) {
            targetCard.setLocked(true);
        }
    }
}
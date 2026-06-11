package model.special;
import model.*;
import java.util.List;

/**
 * Represents the Quantum Entanglement special card belonging to the Quantum deck theme.
 * This card targets a single card in the player's hand and triples its score value during calculations.
 * @author Muhammed Cagan Goktas
 */
public class QuantumEntanglement extends SpecialCard {
	/**
     * Constructs a new Quantum Entanglement special card and assigns it to the Quantum deck.
     */
    public QuantumEntanglement() {
    	super("Quantum Entanglement", DeckType.QUANTUM);
    }

    /**
     * Modifies the final calculated score. For Quantum Entanglement, the score impact 
     * is dynamically computed by the scoring engine using the card's multiplier attribute, 
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
     * Appends a 3x score multiplier to a specific numbered card in the player's hand.
     * @param targetCard the {@link Card} to be entangled and multiplied
     */
    public void executeEntangle(Card targetCard) {
        if (targetCard instanceof NumberCard) {
            targetCard.setScoreMultiplier(3.0);
        }
    }
}
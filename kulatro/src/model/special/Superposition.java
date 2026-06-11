package model.special;
import model.*;
import java.util.List;

/**
 * Represents the Superposition special card belonging to the Quantum deck theme.
 * Allows the player to submit two different hands in the same round and takes the better score.
 * @author Muhammed Cagan Goktas
 */
public class Superposition extends SpecialCard {
		
	private int firstHandScore = -1;
    private boolean firstHandSubmitted = false;
    
	/**
     * Constructs a new Superposition special card and assigns it to the Quantum deck.
     */
    public Superposition() {
    	super("Superposition", DeckType.QUANTUM);
    }
    
    public boolean isFirstHandSubmitted() {
        return firstHandSubmitted;
    }

    public void registerFirstHand(int score) {
        this.firstHandScore = score;
        this.firstHandSubmitted = true;
    }
    
    
    /**
     * Modifies the final calculated score. Player submits two different hands, and Superposition
     * allows the player to get the maximum points.
     * @param currentScore current round's calculated score
     * @param hand the current list of cards in the player's hand
     * @return the maximum of two submission scores
     */
    @Override
    public int applyScoreEffect(int currentScore, List<Card> hand) {
        if (!isUsed() || !firstHandSubmitted || firstHandScore == -1) return currentScore;

        int finalScore = Math.max(firstHandScore, currentScore);
        
        this.firstHandScore = -1;
        this.firstHandSubmitted = false;
        
        return finalScore;
    }
}
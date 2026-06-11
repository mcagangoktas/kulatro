package model.special;
import model.*;
import java.util.List;

/**
 * Represents the Philosopher's Stone special card belonging to the Alchemy deck theme.
 * This powerful card doubles the total calculated score of the current hand for a single use.
 * @author Muhammed Cagan Goktas
 */
public class PhilosophersStone extends SpecialCard {
    
    /**
     * Constructs a new Philosopher's Stone special card and assigns it to the Alchemy deck.
     */
    public PhilosophersStone() {
        super("Philosopher's Stone", DeckType.ALCHEMY);
    }

    /**
     * Applies a one-time score modification that doubles the baseline hand score.
     * Subsequent calls during the same calculation cycle will return the score unchanged.
     * @param currentScore the baseline calculated score before doubling
     * @param hand the current list of cards in the player's hand
     * @return the doubled total score if unused, or the original score if already triggered
     */
    @Override
    public int applyScoreEffect(int currentScore, List<Card> hand) {
    	this.used = true;
    	return currentScore * 2;
    }
    
}
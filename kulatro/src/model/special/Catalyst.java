package model.special;
import java.util.ArrayList;
import java.util.List;

import model.Card;
import model.DeckType;
import model.NumberCard;
import model.SpecialCard;

/**
 * Represents the Catalyst special card belonging to the Alchemy deck theme.
 * This card boosts scoring power by increasing the multiplier of any "pair" combination by +1.
 * @author Muhammed Cagan Goktas
 */
public class Catalyst extends SpecialCard {
	private List<Card> catalyzedCards = new ArrayList<>();
	
	/**
     * Constructs a new Catalyst special card and assigns it to the Alchemy deck.
     */
    public Catalyst() {
    	super("Catalyst", DeckType.ALCHEMY);
    }
    
    public void setCatalyzedCards(List<Card> cards) {
        this.catalyzedCards = new ArrayList<>(cards);
    }
    
    /**
     * Modifies the final calculated score. For Catalyst, the direct multiplier 
     * modification is handled by the scoring calculation logic, so this returns the current score unchanged.
     * @param currentScore the baseline calculated score
     * @param hand the current list of cards in the player's hand
     * @return the unchanged current integer score
     */
    @Override
    public int applyScoreEffect(int currentScore, List<Card> hand) {
    	if (!isUsed() || catalyzedCards.size() != 2) return currentScore;
        
        int extraPoints = 0;
        
        for (Card submittedCard : hand) {
            if (submittedCard instanceof NumberCard) {
                if (catalyzedCards.contains(submittedCard)) {
                    NumberCard nc = (NumberCard) submittedCard;
                    
                    int val = nc.getValue();
                    double multiplier = nc.getScoreMultiplier(); 
                    
                    extraPoints += (int) (val * multiplier);
                }
            }
        }
        
        return currentScore + extraPoints;
    }
    
}
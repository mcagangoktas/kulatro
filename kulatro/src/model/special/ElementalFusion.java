package model.special;
import java.util.ArrayList;
import java.util.List;

import model.Card;
import model.Deck;
import model.DeckType;
import model.NumberCard;
import model.Player;
import model.SpecialCard;

/**
 * Represents the Elemental Fusion special card belonging to the Alchemy deck theme.
 * This card boosts scoring by treating any matching pair (at least 2 cards of the same type) 
 * as a set of 4 cards of that type for the current round.
 * @author Muhammed Cagan Goktas
 */
public class ElementalFusion extends SpecialCard {
	private List<Card> fusedCards = new ArrayList<>();
	
	/**
     * Constructs a new Elemental Fusion special card and assigns it to the Alchemy deck.
     */
    public ElementalFusion() {
    	super("Elemental Fusion", DeckType.ALCHEMY);
    }
    
    public void setFusedCards(List<Card> cards) {
        this.fusedCards = new ArrayList<>(cards);
    }
    
    /**
     * Modifies the final calculated score. For Elemental Fusion, the set count 
     * modification is handled by the scoring logic, so this returns the current score unchanged.
     * @param currentScore the baseline calculated score
     * @param hand the current list of cards in the player's hand
     * @return the unchanged current integer score
     */
    @Override
    public int applyScoreEffect(int currentScore, List<Card> hand) {
    	if (!isUsed() || fusedCards.size() != 2) return currentScore;
        
        int extraPoints = 0;
        int fusedBaseSum = 0;
        int countFound = 0;
        
        for (Card submittedCard : hand) {
            if (submittedCard instanceof NumberCard) {
                if (fusedCards.contains(submittedCard)) {
                    NumberCard nc = (NumberCard) submittedCard;
                    fusedBaseSum += nc.getValue();
                    countFound++;
                }
            }
        }
        if (countFound == 2) {
            extraPoints = fusedBaseSum * 8;
        }
        
        return currentScore + extraPoints;
    }
    
}
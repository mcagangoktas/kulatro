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
 * Represents the Electron Bond special card belonging to the Element deck theme.
 * This card allows two different element types to be counted as a pair during scoring.
 * @author Muhammed Cagan Goktas
 */
public class ElectronBond extends SpecialCard {
	private List<Card> bondedCards = new ArrayList<>();
	
	/**
     * Constructs a new Electron Bond special card and assigns it to the Element deck.
     */
    public ElectronBond() {
    	super("Electron Bond", DeckType.ELEMENT);
    }
    
    public void setBondedCards(List<Card> cards) {
        this.bondedCards = new ArrayList<>(cards);
    }
    
    public List<Card> getBondedCards() {
        return this.bondedCards;
    }
    
    /**
     * Modifies the final calculated score. For Electron Bond, the effect is handled 
     * by the core scoring engine, so this returns the current score unchanged.
     * @param currentScore the baseline calculated score
     * @param hand the current list of cards in the player's hand
     * @return the unchanged current integer score
     */
    @Override
    public int applyScoreEffect(int currentScore, List<Card> hand) {
    	if (!isUsed() || bondedCards.size() != 2) return currentScore;

        Card c1 = bondedCards.get(0);
        Card c2 = bondedCards.get(1);
        
        int extraPoints = 0;
        
        if (c1 instanceof NumberCard && c2 instanceof NumberCard) {
            int val1 = ((NumberCard) c1).getValue();
            int val2 = ((NumberCard) c2).getValue();
            
            int finalVal1 = c1.isLocked() ? val1 * 2 : val1;
            int finalVal2 = c2.isLocked() ? val2 * 2 : val2;
            
            int p1 = (int) (finalVal1 * c1.getScoreMultiplier());
            int p2 = (int) (finalVal2 * c2.getScoreMultiplier());
            
            extraPoints = p1 + p2;
        }
        
        return currentScore + extraPoints;
    }
}
package model.special;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Card;
import model.DeckType;
import model.NumberCard;
import model.SpecialCard;

/**
 * Represents the Periodic Boost special card belonging to the Element deck theme.
 * This card provides a global score enhancement by adding +2 to the numeric value 
 * of all standard numbered cards currently in the player's hand.
 * @author Muhammed Cagan Goktas
 */
public class PeriodicBoost extends SpecialCard {
	
	/**
     * Constructs a new Periodic Boost special card and assigns it to the Element deck.
     */
    public PeriodicBoost() {
    	super("Periodic Boost", DeckType.ELEMENT);
    }
    
    /**
     * Calculates and adds a score bonus by evaluating all numbered cards in the hand 
     * and applying a +2 value boost to each.
     * @param currentScore the baseline calculated score before the boost
     * @param hand the current list of cards in the player's hand to be evaluated
     * @return the newly calculated total integer score including the global boost
     */
    @Override
    public int applyScoreEffect(int currentScore, List<Card> hand) {
    	if (!isUsed()) return currentScore;

        Map<String, List<Card>> grouped = new HashMap<>();
        for (Card c : hand) {
            grouped.computeIfAbsent(c.getType(), _ -> new ArrayList<>()).add(c);
        }

        int maxGroupSize = 0;
        for (List<Card> g : grouped.values()) {
            if (g.size() > maxGroupSize) maxGroupSize = g.size();
        }

        int extraPoints = 0;

        if (hand.size() == 4 && maxGroupSize == 4) {
            int totalBoost = 0;
            for (Card c : hand) {
                if (c instanceof NumberCard) {
                    totalBoost += (int) (2 * c.getScoreMultiplier());
                }
            }
            extraPoints = totalBoost * 10;
        } 

        else if (hand.size() == 4 && grouped.size() == 4) {
            int totalBoost = 0;
            for (Card c : hand) {
                if (c instanceof NumberCard) {
                    totalBoost += (int) (2 * c.getScoreMultiplier());
                }
            }
            extraPoints = totalBoost * 5;
        } 
        else {
            for (List<Card> group : grouped.values()) {
                int groupBoost = 0;
                for (Card c : group) {
                    if (c instanceof NumberCard) {
                        groupBoost += (int) (2 * c.getScoreMultiplier());
                    }
                }

                if (group.size() == 2) {
                    extraPoints += (groupBoost * 2);
                } else {
                    extraPoints += groupBoost;
                }
            }
        }
        
        return currentScore + extraPoints;
    }
    
}
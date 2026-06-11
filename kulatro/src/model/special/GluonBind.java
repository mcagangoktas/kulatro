package model.special;

import model.Card;
import model.Deck;
import model.DeckType;
import model.NumberCard;
import model.Player;
import model.SpecialCard;

/**
 * Represents the Gluon Bind special card belonging to the Quantum deck theme.
 * This card allows a player to merge two numbered cards into a single card with 
 * their combined value and draw a replacement card.
 * @author Muhammed Cagan Goktas
 */
public class GluonBind extends SpecialCard {
	
	/**
     * Constructs a new Gluon Bind special card and assigns it to the Quantum deck.
     */
    public GluonBind() {
    	super("Gluon Bind", DeckType.QUANTUM);
    }
    
    /**
     * Merges two numbered cards into one with their combined numeric value (capped at 9) 
     * and draws a new card from the deck. The new card adopts the element type of the first card.
     * @param c1 the first selected {@link Card} (determines the final type)
     * @param c2 the second selected {@link Card} to be merged
     * @param player the active {@link Player} performing the merge operation
     * @param deck the active {@link Deck} used to draw a replacement card
     */
    public void merge(Card c1, Card c2, Player player, Deck deck) {
    	if (c1 == c2) {
            System.err.println("You cannot merge the same cards!");
            return;
        }
    	
        if (c1 instanceof NumberCard && c2 instanceof NumberCard) {
            int val = Math.min(9, ((NumberCard) c1).getValue() + ((NumberCard) c2).getValue());
            player.getHand().remove(c1);
            player.getHand().remove(c2);
            player.getHand().add(new NumberCard(c1.getType(), c1.getStyle(), val));
            try {
            	player.addCardToHand(deck.drawCard());
            } catch (Exception e) {
            	System.err.println("Could not merge (gluon bind): " + e.getMessage());
            }
        }
    }
}
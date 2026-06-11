package model.special;

import model.Card;
import model.Deck;
import model.DeckType;
import model.Player;
import model.SpecialCard;

/**
 * Represents the Transmutation special card belonging to the Alchemy deck theme.
 * This card allows a player to swap a specific card from their hand with a random 
 * card drawn from the deck without affecting the discard limit.
 * @author Muhammed Cagan Goktas
 */
public class Transmutation extends SpecialCard {
	
	/**
     * Constructs a new Transmutation special card and assigns it to the Alchemy deck.
     */
    public Transmutation() {
    	super("Transmutation", DeckType.ALCHEMY);
    }
   
    /**
     * Removes the targeted card from the player's hand, sends it to the discard pile, 
     * and draws a new card from the deck as a replacement.
     * @param target the {@link Card} in the player's hand to be replaced
     * @param player the active {@link Player} performing the swap
     * @param deck the active {@link Deck} used to draw the new card
     */
    public void executeSwap(Card target, Player player, Deck deck) {
    	if (!used) {
	        player.getHand().remove(target);
	        deck.addToDiscardPile(target);
	        try {
	            player.addCardToHand(deck.drawCard());
	        } catch (Exception e) {
	        	System.err.println("Could not swap (transmutation error): " + e.getMessage());
	        }
    
    	} 
    }
}
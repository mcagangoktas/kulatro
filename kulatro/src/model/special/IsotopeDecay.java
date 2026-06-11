package model.special;
import model.*;
import java.util.ArrayList;
import java.util.List;

/**
* Represents the Isotope Decay special card belonging to the Element deck theme.
* This card allows a player to discard their entire hand and draw 4 replacement cards 
* without consuming their standard discard limit.
* @author Muhammed Cagan Goktas
*/
public class IsotopeDecay extends SpecialCard {
	
	/**
     * Constructs a new Isotope Decay special card and assigns it to the Element deck.
     */
    public IsotopeDecay() {
    	super("Isotope Decay", DeckType.ELEMENT); 
    }
    
    /**
     * Executes the unique action when the card is played. 
     * It clears the player's entire hand into the deck's discard pile and draws exactly 4 new cards.
     * @param player the current active player whose hand will be cycled
     * @param deck the active deck instance used to draw new cards
     */
    @Override
    public void performAction(Player player, Deck deck) {
        List<Card> currentHand = new ArrayList<>(player.getHand());
        for (Card c : currentHand) {
            player.getHand().remove(c);
            c.setLocked(false);
            deck.addToDiscardPile(c);
        }
        for (int i = 0; i < 4; i++) {
        	if (deck.getCards().isEmpty()) {
                deck.reshuffle(); 
            }
            try {
            	player.addCardToHand(deck.drawCard()); 
            } catch (Exception e) {System.err.println("> Error drawing card (Isotope Decay): " + e.getMessage());}
        }
        this.used = true;
    }
    
}
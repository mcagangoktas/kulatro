package model.special;
import model.*;
import java.util.List;

/**
 * Represents the Photon Burst special card belonging to the Quantum deck theme.
 * This card allows a player to look at the next 3 cards in the deck and optionally 
 * swap one of them with a card from their current hand.
 * @author Muhammed Cagan Goktas
 */
public class PhotonBurst extends SpecialCard {
	
	/**
     * Constructs a new Photon Burst special card and assigns it to the Quantum deck.
     */
    public PhotonBurst() {
    	super("Photon Burst", DeckType.QUANTUM);
    }
    
}
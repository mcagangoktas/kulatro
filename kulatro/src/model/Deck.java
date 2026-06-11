package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import exception.DeckEmptyException;
import exception.GameDataException;
import model.special.Catalyst;
import model.special.ElectronBond;
import model.special.ElementalFusion;
import model.special.GluonBind;
import model.special.IsotopeDecay;
import model.special.NobleGas;
import model.special.PeriodicBoost;
import model.special.PhilosophersStone;
import model.special.PhotonBurst;
import model.special.QuantumEntanglement;
import model.special.Superposition;
import model.special.Transmutation;
import service.LogHandler;

/**
 * Represents the game deck containing both standard number cards and special cards.
 * Manages drawing, discarding, peeking, and reshuffling operations, while supporting
 * serialization for game saving capabilities.
 * @author Muhammed Cagan Goktas
 */
public class Deck implements Serializable {
	/**
     * Serial version UID for verifying serialization compatibility.
     */
	private static final long serialVersionUID = 600L;
	
	private List<Card> cards;
	private List<Card> discardPile;
    private DeckType style;

    /**
     * Constructs a new Deck with the specified theme style and initializes the cards.
     * @param style the {@link DeckType} specifying the card set to be used
     */
    public Deck(DeckType style) {
        this.style = style;
        this.cards = new ArrayList<>();
        this.discardPile = new ArrayList<>();
        initializeDeck();
    }

    /**
     * Standardizes the deck population by generating numbered cards for each type 
     * in the style, appending special cards, and shuffling the final product.
     */
    private void initializeDeck() {
        String[] types = style.getTypes();
        for (String type : types) {
            for (int i = 1; i <= 9; i++) {
                cards.add(new NumberCard(type, style, i));
            }
        }
        addSpecialCards();
        Collections.shuffle(cards);
    }

    /**
     * Dynamically loads and instantiates special card definitions from external configurations
     * using the {@link LogHandler}, appending them safely into the active draw pile.
     */
    private void addSpecialCards() {
	    try {
	        Map<String, List<String>> allSpecialCards = LogHandler.loadCardDefinitions();
	        
	        List<String> cardNames = allSpecialCards.get(this.style.getDisplayName());

	        if (cardNames != null) {
	            for (String name : cardNames) {
	                SpecialCard card = null;

	                if (name.equals("PhilosophersStone")) {
	                    card = new PhilosophersStone();
	                } else if (name.equals("Transmutation")) {
	                    card = new Transmutation();
	                } else if (name.equals("ElementalFusion")) {
	                    card = new ElementalFusion();
	                } else if (name.equals("Catalyst")) {
	                    card = new Catalyst();
	                } else if (name.equals("PeriodicBoost")) {
	                    card = new PeriodicBoost();
	                } else if (name.equals("NobleGas")) {
	                    card = new NobleGas();
	                } else if (name.equals("IsotopeDecay")) {
	                    card = new IsotopeDecay();
	                } else if (name.equals("ElectronBond")) {
	                    card = new ElectronBond();
	                } else if (name.equals("QuantumEntanglement")) {
	                    card = new QuantumEntanglement();
	                } else if (name.equals("Superposition")) {
	                    card = new Superposition();
	                } else if (name.equals("GluonBind")) {
	                    card = new GluonBind();
	                } else if (name.equals("PhotonBurst")) {
	                    card = new PhotonBurst();
	                }

	                if (card != null) {cards.add(card);}
	            }
	        }
	    } catch (GameDataException e) {
	        System.err.println("File reading error: " + e.getMessage());
	    }
    }

    /**
     * Draws the top card from the deck. If the draw pile is empty, it attempts to
     * recycle the discard pile. If both are completely empty, an exception is raised.
     * @return the {@link Card} drawn from the top of the pile
     * @throws DeckEmptyException if there are absolutely no cards left to draw in either pile
     */
    public Card drawCard() throws DeckEmptyException {
        if (cards.isEmpty()) {
            if (discardPile.isEmpty()) throw new DeckEmptyException();
            reshuffle();
        }
        int lastIndex = cards.size() - 1;
        Card drawnCard = cards.get(lastIndex);
        cards.remove(lastIndex);
        return drawnCard;
    }

    /**
     * Peeks at a designated amount of cards from the top of the draw pile without removing them.
     * @param count the number of cards to observe from the top
     * @return a {@link List} containing the peeked cards ordered from top down
     */
    public List<Card> peekTopCards(int count) {
        List<Card> peeked = new ArrayList<>();
        int start = Math.max(0, cards.size() - count);
        for (int i = cards.size() - 1; i >= start; i--) {
            peeked.add(cards.get(i));
        }
        return peeked;
    }
    
    /**
     * Extracts and removes a specific card relative to its peeked index location.
     * @param peekIndex the relative index from the top of the pile (0 represents the topmost card)
     * @return the extracted {@link Card} item, or null if the index boundary is invalid
     */
    public Card takeCard(int peekIndex) {
        int deckIndex = cards.size() - 1 - peekIndex;
        if (deckIndex < 0 || deckIndex >= cards.size()) return null;
        return cards.remove(deckIndex);
    }
    
    /**
     * Disposes a card into the discard pile.
     * @param card the {@link Card} to pass to the discard stack
     */
    public void addToDiscardPile(Card card) {
        discardPile.add(card);
    }

    /**
     * Empties the current discard pile back into the primary deck and randomizes 
     * the order of the newly formed pile.
     */
    public void reshuffle() {
        cards.addAll(discardPile);
        discardPile.clear();
        Collections.shuffle(cards);
    }
    
    /**
     * Restores the discard pile from a saved game state.
     * @param discardPile the list of cards that were previously discarded
 	 */
    public void restoreDiscardPile(List<Card> discardPile) {
        this.discardPile = new ArrayList<>(discardPile);
    }
    
    /**
     * Restores the remaining draw pile count from a saved game state by adjusting card list size.
     * @param remaining the number of cards that should remain in the draw pile
     */
    public void restoreRemainingCount(int remaining) {
        while (cards.size() > remaining) {
            cards.remove(cards.size() - 1);
        }
    }

    public int getRemainingCount() {return cards.size();}
	public List<Card> getCards() {return cards;}
    public DeckType getStyle() {return style;}
    public List<Card> getDiscardPile() {return discardPile;}
}
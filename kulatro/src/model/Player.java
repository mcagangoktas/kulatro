package model;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a player in the card game, maintaining their username, 
 * current hand of cards, and discard statistics.
 * @author Muhammed Cagan Goktas
 */
public class Player implements Serializable {
    private static final long serialVersionUID = 200L;
    
    private String username;
    private List<Card> hand;
    private int usedDiscards;
    private int roundDiscards;

    /**
     * Constructs a new Player with the given username and initializes an empty hand.
     * @param username the unique name of the player
     */
    public Player(String username) {
        this.username = username;
        this.hand = new ArrayList<>();
        this.usedDiscards = 0;
        this.roundDiscards = 0;
    }

    /**
     * Adds a card to the player's hand if they have fewer than four cards.
     * @param card the {@link Card} to be added
     */
    public void addCardToHand(Card card) {
        if (hand.size() < 4) {
            hand.add(card);
        }
    }

    /**
     * Removes all cards from the player's current hand.
     */
    public void clearHand() {
        hand.clear();
    }
    
    /**
     * Restores the player's discard statistics to the specified values, 
     * used when loading a saved game state.
     * @param used the total number of used discards
     * @param round the number of discards used in the current round
     */
    public void restoreDiscards(int used, int round) {
        this.usedDiscards = used;
        this.roundDiscards = round;
    }

    /**
     * Gets the player's current hand of cards.
     * @return a {@link List} of {@link Card} objects in the player's hand
     */
    public List<Card> getHand() {return hand;}
    
    /**
     * Gets the username of the player.
     * @return the player's username as a String
     */
    public String getUsername() {return username;}
    
    /**
     * Gets the total number of discards used by the player throughout the game.
     * @return the total used discard count
     */
    public int getUsedDiscards() {return usedDiscards;}
    
    /**
     * Increments both the total game discard count and the current round discard count by one.
     */
    public void incrementDiscardCount() {this.usedDiscards++; this.roundDiscards++;}
    
    /**
     * Resets the total game discard count back to zero.
     */
    public void resetDiscardCount() {this.usedDiscards = 0;}
    
    /**
     * Gets the number of discards used by the player in the current round.
     * @return the round discard count
     */
    public int getRoundDiscards() {return roundDiscards;}
    
    /**
     * Resets the current round's discard count back to zero.
     */
    public void resetRoundDiscards() {this.roundDiscards = 0;}
}
package service;

import java.io.Serializable;
import java.util.List;

import exception.DeckEmptyException;
import exception.InvalidHandException;
import model.Card;
import model.Deck;
import model.DeckType;
import model.Difficulty;
import model.Player;
import model.SpecialCard;
import model.special.PhilosophersStone;
import model.special.Superposition;

/**
 * The core controller orchestrating the main game loop, managing turns, 
 * deck interactions, player hands, round progression, and score tracking.
 * @author Muhammed Cagan Goktas
 */
public class GameEngine implements Serializable {
    private static final long serialVersionUID = 500L;
    
    private String sessionName;
    private Player player;
    private Deck deck;
    private ScoreManager scoreManager;
	private RoundManager roundManager;
    private SpecialCard specialCard;
    
    /**
     * Initializes a new game session, generates a unique session identifier, setups managers 
     * based on difficulty, logs the starting state, and deals the initial hand to the player.
     * @param player the active {@link Player} participating in this session
     * @param style the selected {@link DeckType} theme for the match
     * @param difficulty the selected {@link Difficulty} level setting the score targets
     */
    public GameEngine(Player player, DeckType style, Difficulty difficulty) {
        this.player = player;
        this.deck = new Deck(style);
        this.sessionName = player.getUsername() + "_" + System.currentTimeMillis();
        
        this.scoreManager = new ScoreManager();
        this.scoreManager.setDifficultyThresholds(difficulty);
        
        this.roundManager = new RoundManager(difficulty, scoreManager.getCurrentThresholds());
        
        LogHandler.logEvent(player.getUsername(), "Game has started. Deck: " + style + " Difficulty: " + difficulty);
        
        refillHand();
        LogHandler.logEvent(player.getUsername(), "First hand is given: " + player.getHand().toString());
    }

    /**
     * An overload for the case where the player does not select any cards, resulting in submiting 
     * the whole hand.
     * @throws InvalidHandException if the submitted card structure fails evaluation rules
     */
    public void submitHand() throws InvalidHandException {
        submitHand(player.getHand());
    }
 
    /**
     * Submits the player's selected cards for score calculation, updates game statistics, 
     * and either advances the game to the next round or concludes the session if final round is reached.
     * @throws InvalidHandException if the submitted card structure fails evaluation rules
     */
    public void submitHand(List<Card> cardsToScore) throws InvalidHandException {
        LogHandler.logEvent(player.getUsername(), "Hand submitted: " + cardsToScore.toString());
        int score = scoreManager.calculateHandScore(cardsToScore, specialCard);
        LogHandler.logEvent(player.getUsername(), "Round score: " + score + " | Total: " + (scoreManager.getTotalScore() + score));
        scoreManager.finalizeRound(score);
        
        for (Card c : cardsToScore) {
            player.getHand().remove(c);
            c.setLocked(false);
            deck.addToDiscardPile(c);
        }
 
        if (roundManager.getCurrentRound() < 4) {
            roundManager.nextRound();
            player.resetRoundDiscards();
            
            if (specialCard != null) {
                if (specialCard instanceof PhilosophersStone && specialCard.isUsed()) setSpecialCard(null);
                else specialCard.setUsed(false);
            }
            
            refillHand();
            LogHandler.logEvent(player.getUsername(), "Round " + roundManager.getCurrentRound() + " has started.");
        } else {
            boolean isWin = scoreManager.checkGameWin();
            LogHandler.logEvent(player.getUsername(), "Game over. Result: " + (isWin ? "WON" : "LOST"));
        }
        
        if (specialCard != null && specialCard.isUsed()) {
        	if (specialCard instanceof Superposition && ((Superposition) specialCard).isFirstHandSubmitted()) {} 
        	else {
                setSpecialCard(null);
            }
        }
    }

    /**
     * Processes a manual discard action by discarding selected cards from the player's hand, 
     * moving them to the discard pile, incrementing the round's discard counter, and drawing replacements.
     * @param cardsToDiscard the subset {@link List} of {@link Card} objects chosen to be replaced
     */
    public void discardAndSwap(List<Card> cardsToDiscard) {
        LogHandler.logEvent(player.getUsername(), "Cards discarded: " + cardsToDiscard.size() + " cards.");
        
        for (Card c : cardsToDiscard) {
            player.getHand().remove(c);
            c.setLocked(false);
            deck.addToDiscardPile(c);
        }

        if (!(specialCard instanceof Superposition)) {
        	for (int i = 0; i < cardsToDiscard.size(); i++) {
        		player.incrementDiscardCount();
        	}
        }
        refillHand();
    }

    /**
     * Safely populates the player's hand until it contains exactly 4 cards. 
     * Automatically triggers a deck reshuffle if the drawing pool becomes exhausted.
     */
    private void refillHand() {
        while (player.getHand().size() < 4) {
            try {
                player.addCardToHand(deck.drawCard());
            } catch (DeckEmptyException e) {
                deck.reshuffle();
                try {
                    player.addCardToHand(deck.drawCard());
                } catch (Exception ex) {
                    System.err.println("Deck is completely empty even after reshuffle!");
                    break; 
                }
            }
        }
    }

    /**
     * Checks whether the game has completed all mandatory rounds and calculations.
     * @return true if the game session is finished, false otherwise
     */
    public boolean isGameOver() {return roundManager.getCurrentRound() == 4 && scoreManager.getRoundScores().size() == 4;}
    
    /**
     * Gets the unique session name token generated for this engine instance.
     * @return the unique session ID String
     */
    public String getSessionName() {return sessionName;}
    
    /**
     * Retrieves the active player bound to this game instance.
     * @return the {@link Player} reference
     */
    public Player getPlayer() {return player;}
    
    /**
     * Retrieves the card deck currently being drawn from.
     * @return the {@link Deck} reference
     */
    public Deck getDeck() {return deck;}
    
    /**
     * Retrieves the engine's score calculation and tracking sub-manager.
     * @return the {@link ScoreManager} reference
     */
    public ScoreManager getScoreManager() {return scoreManager;}
    
    /**
     * Retrieves the round counter and state controller.
     * @return the {@link RoundManager} reference
     */
    public RoundManager getRoundManager() {return roundManager;}
    
    /**
     * Retrieves the active special card modifier for the current scoring phase.
     * @return the active {@link SpecialCard}, or null if none is selected
     */
	public SpecialCard getSpecialCard() {return specialCard;}
	
	/**
     * Sets special card to be used in the current round.
     * @param specialCard the target {@link SpecialCard} instance to bind
     */
	public void setSpecialCard(SpecialCard specialCard) {this.specialCard = specialCard;}
}
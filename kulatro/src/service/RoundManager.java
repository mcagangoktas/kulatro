package service;

import java.io.Serializable;
import model.Difficulty;

/**
 * Manages the progression of game rounds and tracks the scoring thresholds 
 * required to advance based on the selected difficulty level.
 * @author Muhammed Cagan Goktas
 */
public class RoundManager implements Serializable {
    private static final long serialVersionUID = 400L;

    private int currentRound;
    private int[] thresholds;
    private Difficulty difficulty;

    /**
     * Constructs a RoundManager with specific difficulty settings and sets the starting round to 1.
     * @param difficulty the {@link Difficulty} level of the current game
     * @param thresholds an array of integer values representing the target scores for each round
     */
    public RoundManager(Difficulty difficulty, int[] thresholds) {
        this.difficulty = difficulty;
        this.thresholds = thresholds;
        this.currentRound = 1;
    }

    /**
     * Increments the round counter, capping the progression at the final round (Round 4).
     */
    public void nextRound() {
        if (currentRound < 4) {
            currentRound++;
        }
    }

    /**
     * Retrieves the specific score target that the player must reach for the active round.
     * @return the integer threshold for the current round, or 0 if the round index is invalid
     */
    public int getCurrentTargetScore() {
        if (currentRound <= thresholds.length) {
            return thresholds[currentRound - 1];
        }
        return 0;
    }

    /**
     * Calculates the cumulative score required to win the entire game session.
     * @return the sum of all individual round thresholds as an integer
     */
    public int getTotalTargetScore() {
        int sum = 0;
        for (int t : thresholds) sum += t;
        return sum;
    }

    /**
     * Gets the current round number (1 through 4).
     * @return the current round index
     */
    public int getCurrentRound() {return currentRound;}
    
    /**
     * Gets the difficulty setting bound to this manager.
     * @return the {@link Difficulty} enum value
     */
    public Difficulty getDifficulty() {return difficulty;}
}

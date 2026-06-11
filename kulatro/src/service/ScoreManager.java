package service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exception.InvalidHandException;
import model.Card;
import model.Difficulty;
import model.NumberCard;
import model.SpecialCard;

/**
 * Manages game difficulty configurations, processes card evaluation strategies, 
 * computes complex score multipliers, and validates final win/loss conditions.
 * @author Muhammed Cagan Goktas
 */
public class ScoreManager implements Serializable {
    private static final long serialVersionUID = 300L;
    private int totalScore = 0;
    private int[] currentThresholds = new int[4]; 
    private List<Integer> roundScores = new ArrayList<>();
    
    /**
     * Maps the game difficulty level to concrete step-based round target score thresholds.
     * @param difficulty the chosen {@link Difficulty} enum value
     */
    public void setDifficultyThresholds(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                currentThresholds = new int[]{30, 40, 50, 60};
                break;
            case MEDIUM:
                currentThresholds = new int[]{60, 80, 100, 120};
                break;
            case HARD:
                currentThresholds = new int[]{90, 120, 150, 180};
                break;
        }
    }
    
    /**
     * Evaluates a player's hand score without applying any special card modifiers.
     * @param hand the current {@link List} of {@link Card} objects to calculate
     * @return the calculated base integer score
     * @throws InvalidHandException if the hand size or structure fails validation rules
     */
    public int calculateHandScore(List<Card> hand) throws InvalidHandException {
        return calculateHandScore(hand, null);
    }
    
    /**
     * Evaluates the hand structure, injects active special card mechanics, dynamically 
     * calculates multipliers, and aggregates locked or scaled card values into a final score.
     * @param hand the active {@link List} of {@link Card} objects to evaluate
     * @param specialCard the active {@link SpecialCard} power applied to this turn, or null
     * @return the calculated total integer score after applying multipliers and bonuses
     * @throws InvalidHandException if the hand composition is structurally illegal
     */
    public int calculateHandScore(List<Card> submittedCards, SpecialCard specialCard) throws InvalidHandException {
        validateHand(submittedCards);

        Map<String, List<Card>> grouped = new HashMap<>();
        for (Card c : submittedCards) {
            grouped.computeIfAbsent(c.getType(), _ -> new ArrayList<>()).add(c);
        }

        int totalScore = 0;
        int maxGroupSize = 0;
        for (List<Card> g : grouped.values()) {
            if (g.size() > maxGroupSize) maxGroupSize = g.size();
        }

        if (submittedCards.size() == 4 && maxGroupSize == 4) {
            totalScore = calculateBaseSum(submittedCards) * 10;
        } 

        else if (submittedCards.size() == 4 && grouped.size() == 4) {
            totalScore = calculateBaseSum(submittedCards) * 5;
        } 

        else {
            for (Map.Entry<String, List<Card>> entry : grouped.entrySet()) {
                List<Card> group = entry.getValue();
                int groupSum = calculateBaseSum(group);

                if (group.size() == 2) {
                    totalScore += (groupSum * 2);
                } else {
                    totalScore += groupSum;
                }
            }
        }

        if (specialCard != null && specialCard.isUsed()) {
            totalScore = specialCard.applyScoreEffect(totalScore, submittedCards);
        }

        return totalScore;
    }

    /**
     * This method is a helper method to calculate the sum of the values of all cards.
     * @param cards the {@link List} of {@link Card} objects to calculate the total value 
     * @return sum of the values of the {@link Card} objects
     */
    private int calculateBaseSum(List<Card> cards) {
        int sum = 0;
        for (Card c : cards) {
            if (c instanceof NumberCard) {
            	int cardValue = ((NumberCard) c).getValue();
                
                if (c.isLocked()) {
                    cardValue *= 2;
                }
                
                sum += (int) (cardValue * c.getScoreMultiplier());
            }
        }
        return sum;
    }

    /**
     * Asserts that the player's active card hand complies with structural boundaries (1 to 4 cards).
     * @param hand the {@link List} of {@link Card} objects to validate
     * @throws InvalidHandException if the hand is null, empty, or exceeds 4 cards
     */
    public void validateHand(List<Card> hand) throws InvalidHandException {
        if (hand == null || hand.isEmpty()) {
            throw new InvalidHandException("You must choose at least 1 card.");
        }
        if (hand.size() > 4) {
            throw new InvalidHandException("There can be at most 4 cards in your hand.");
        }
    }
    
    /**
     * Closes the active round, archiving its individual score and adding it to the player's session total.
     * @param score the calculated score of the round being finalized
     */
    public void finalizeRound(int score) {
        roundScores.add(score);
        totalScore += score;
    }

    /**
     * Evaluates the win/loss state by testing whether the player's rolling round 
     * performance average meets or exceeds the required difficulty average threshold.
     * @return true if the overall average requirements are fulfilled, false otherwise
     */
    public boolean checkGameWin() {
        if (roundScores.size() < 4) return false;
        
        int sum = 0;
        for (int num : currentThresholds) sum += num;
        
        int thresholdAvg = sum / 4;
        double playerAvg = totalScore / 4.0;
        
        return playerAvg >= thresholdAvg;
    }
    
    /**
     * Restores the total score and individual round scores when loading a saved game.
     * @param totalScore the accumulated score to restore
     * @param roundScores the list of past round scores to restore
     */
    public void restoreState(int totalScore, List<Integer> roundScores) {
        this.totalScore = totalScore;
        this.roundScores = new ArrayList<>(roundScores);
    }

    /**
     * Gets the individual scores recorded for each completed round.
     * @return a {@link List} of round score integers
     */
	public List<Integer> getRoundScores() {return roundScores;}
	
	/**
     * Gets the aggregate score accumulated across all rounds in the current session.
     * @return the total score integer
     */
	public int getTotalScore() {return totalScore;}
	
	/**
     * Gets the active scoring thresholds mapped to the selected game difficulty.
     * @return an integer array containing the 4 round threshold scores
     */
	public int[] getCurrentThresholds() {return currentThresholds;}
}
package model;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a registered user in the system, storing login credentials 
 * and a complete history of played games.
 * @author Muhammed Cagan Goktas
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String password;
    private List<GameRecord> gameHistory;

    /**
     * Constructs a new User with the specified credentials and initializes an empty game history.
     * @param username the unique login name of the user
     * @param password the account security password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.gameHistory = new ArrayList<>();
    }

    /**
     * Represents a single game session's performance outcome and data.
     */
    public static class GameRecord implements Serializable {
        private static final long serialVersionUID = 2L;
		public String sessionName;
        public int finalScore;
        public boolean isWon;
        public String date;

        /**
         * Constructs a new GameRecord capturing the session results and automatically 
         * timestamps the record with the current system date.
         * @param sessionName the name or ID of the game session
         * @param finalScore the total score achieved at the end of the game
         * @param isWon true if the player won the game, false otherwise
         */
        public GameRecord(String sessionName, int finalScore, boolean isWon) {
            this.sessionName = sessionName;
            this.finalScore = finalScore;
            this.isWon = isWon;
            this.date = new Date().toString();
        }
    }

    /**
     * Gets the username of the user.
     * @return the user's login username String
     */
    public String getUsername() {return username;}
    
    /**
     * Gets the account password of the user.
     * @return the security password String
     */
    public String getPassword() {return password;}
    
    /**
     * Retrieves the chronological list of all game records saved by this user.
     * @return a {@link List} of {@link GameRecord} objects
     */
    public List<GameRecord> getGameHistory() {return gameHistory;}
    
    /**
     * Appends a completed game session record into the user's history log.
     * @param record the {@link GameRecord} to add
     */
    public void addGameRecord(GameRecord record) {
        this.gameHistory.add(record);
    }
}
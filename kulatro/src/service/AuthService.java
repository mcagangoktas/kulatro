package service;

import exception.GameDataException;
import model.User;

import java.util.Map;
import java.util.HashMap;

/**
 * Provides authentication and user management services, handling registration, 
 * login sessions, and persisting user game history updates.
 * @author Muhammed Cagan Goktas
 */
public class AuthService {
    private Map<String, User> userDatabase;
    private User currentUser;

    /**
     * Constructs a new AuthService, attempting to populate the user database 
     * by loading existing users from the system logs. Falls back to an empty database on failure.
     */
    public AuthService() {
        try {
            this.userDatabase = LogHandler.loadUsers();
        } catch (GameDataException e) {
            this.userDatabase = new HashMap<>();
        }
    }

    /**
     * Registers a new user with the given credentials and persists the updated database.
     * @param username the desired unique login name
     * @param password the password for account security
     * @throws GameDataException if the username is already registered or persistence fails
     */
    public void register(String username, String password) throws GameDataException {
        if (userDatabase.containsKey(username)) {
            throw new GameDataException("This username has been taken!", null);
        }

        User newUser = new User(username, password);
        userDatabase.put(username, newUser);
        
        LogHandler.saveUsers(userDatabase);
    }

    /**
     * Validates credentials against the database and establishes the current logged-in user session.
     * @param username the login name to check
     * @param password the password to verify
     * @return true if credentials match and login succeeds, false otherwise
     */
    public boolean login(String username, String password) {
        if (userDatabase.containsKey(username)) {
            User user = userDatabase.get(username);
            if (user.getPassword().equals(password)) {
                this.currentUser = user;
                return true;
            }
        }
        return false;
    }

    /**
     * Appends a new game session result to the active user's record history and saves changes to storage.
     * @param sessionName the identifier of the completed game session
     * @param finalScore the end score achieved by the user
     * @param isWon true if the user won the game, false otherwise
     * @throws GameDataException if writing updated user history to disk fails
     */
    public void updateStats(String sessionName, int finalScore, boolean isWon) throws GameDataException {
        if (currentUser != null) {
            User.GameRecord record = new User.GameRecord(sessionName, finalScore, isWon);
            currentUser.addGameRecord(record);
            
            userDatabase.put(currentUser.getUsername(), currentUser);
            LogHandler.saveUsers(userDatabase);
        }
    }

    /**
     * Retrieves the currently authenticated user in this session.
     * @return the active {@link User} object, or null if no user is logged in
     */
    public User getCurrentUser() {return currentUser;}
    
    /**
     * Terminates the current active user session by clearing the logged-in token.
     */
    public void logout() {this.currentUser = null;}
    
    /**
     * Gets the collection of all registered users mapped by their usernames.
     * @return a {@link Map} of username Strings to {@link User} objects
     */
    public Map<String, User> getUserDatabase() {return userDatabase;}
}
package view;

import service.GameEngine;

/**
 * Defines the contract for screen navigation and shared state management.
 * This interface decouples the individual panels from the main application frame,
 * allowing them to request view changes and access the active game session.
 * @author Muhammed Cagan Goktas
 */
public interface ViewNavigator {
	/**
     * Triggers a transition to the specified view.
     * @param viewName the unique identifier of the target panel (e.g., "Menu", "TheGame")
     */
    void navigateTo(String viewName);
    
    /**
     * Updates the global game engine instance to be shared across panels.
     * @param engine the active {@link GameEngine} session
     */
    void setGameEngine(GameEngine engine);
    
    /**
     * Retrieves the current game engine managing the active session.
     * @return the {@link GameEngine} instance, or null if no session is active
     */
    GameEngine getGameEngine();
    
    /**
     * Provides access to the primary gameplay panel, facilitating state 
     * restoration during load operations.
     * @return the active {@link BasePanel} designated for gameplay
     */
    BasePanel getActualGamePanel();
}

package view;

import java.awt.*;
import javax.swing.*;

/**
 * Acts as the primary landing hub after a successful login. 
 * Provides access to the main game setup, statistics, and application settings.
 * @author Muhammed Cagan Goktas
 */
public class MainMenuPanel extends BasePanel {
    private static final long serialVersionUID = 1002L;

    /**
     * Initializes the menu panel with a consistent background and navigation controller.
     * @param navigator the {@link ViewNavigator} used for routing to different game screens
     */
	public MainMenuPanel(ViewNavigator navigator) {
        super(navigator, "main_menu_bg");
    }
	
	/**
     * Composes the visual layout, centering the game title and generating the 
     * primary navigation buttons for the user interface.
     */
    @Override
	protected void setupUI() {
        add(Box.createRigidArea(new Dimension(0, 100)));

        JLabel title = new JLabel("KULATRO");
        title.setFont(new Font("Lucida Calligraphy", Font.BOLD, 72));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        add(Box.createRigidArea(new Dimension(0, 50)));

        createMenuButton("START GAME", "StartPanel", 300);
        createMenuButton("LEADERBOARD", "Leaderboard", 300);
        createMenuButton("SETTINGS", "Settings", 300);
        createMenuButton("EXIT", "EXIT", 300);
    }
}
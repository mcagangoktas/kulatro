package view;

import java.awt.Dimension;

import javax.swing.Box;

/**
 * Serves as a gateway for the player to choose between starting a fresh 
 * game session or restoring a previously saved experiment.
 * @author Muhammed Cagan Goktas
 */
public class StartPanel extends BasePanel {
    private static final long serialVersionUID = 1003L;

    /**
     * Constructs the start options panel with the standard menu background.
     * @param navigator the {@link ViewNavigator} for routing to setup or load screens
     */
    public StartPanel(ViewNavigator navigator) {
    	super(navigator, "main_menu_bg");
    }

    /**
     * Populates the panel with centered navigation buttons, utilizing vertical glue 
     * to maintain a balanced layout regardless of screen scaling.
     */
    @Override
    protected void setupUI() {
        add(Box.createVerticalGlue());
        createMenuButton("New Game", "Setup", 300);

        add(Box.createRigidArea(new Dimension(0, 30)));
        createMenuButton("Load Game", "Load", 300);

        add(Box.createVerticalGlue());
        createMenuButton("BACK TO MENU", "Menu", 200);
        
        add(Box.createRigidArea(new Dimension(0, 20)));
    }
}
package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import exception.GameDataException;
import service.GameEngine;
import service.LogHandler;
import service.SoundManager;

/**
 * Manages the game loading screen, providing interactive slots that check for 
 * existing save files and restore game states into the active session.
 * @author Muhammed Cagan Goktas
 */
public class LoadPanel extends BasePanel {
	private static final long serialVersionUID = 1008L;
	
	/**
	 * Constructs the load screen panel with the main menu background.
	 * @param navigator the {@link ViewNavigator} used for screen transitions
	 */
	public LoadPanel(ViewNavigator navigator) {
		super(navigator, "main_menu_bg");
	}

	/**
	 * Clears and rebuilds the UI to reflect changes in the save file directory, 
	 * ensuring slot labels are up to date.
	 */
	public void refreshSaveList() {
        removeAll();
        setupUI();
        revalidate();
        repaint();
    }
	
	/**
	 * Configures the layout by adding the title and generating load slots 
	 * flanked by vertical glue for centered alignment.
	 */
	@Override
	protected void setupUI() {
		add(Box.createVerticalGlue());

        JLabel title = new JLabel("Load Game");
        title.setFont(new Font("Lucida Calligraphy", Font.BOLD, 45));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        add(Box.createRigidArea(new Dimension(0, 40)));

        createLoadButton("slot1", 1);
        createLoadButton("slot2", 2);
        createLoadButton("slot3", 3);

        add(Box.createRigidArea(new Dimension(0, 30)));
        createMenuButton("BACK", "StartPanel", 200);

        add(Box.createVerticalGlue());
	}
	
	/**
	 * Creates a button for a specific save slot. If the file exists, it triggers 
	 * the loading sequence; otherwise, it redirects the user to create a new session.
	 * @param sessionName the internal filename of the save slot
	 * @param slotNumber the display index for the user interface
	 */
	private void createLoadButton(String sessionName, int slotNumber) {
	    File checkFile = new File("data/saves/" + sessionName + ".txt");
	    boolean exists = checkFile.exists();

	    String buttonText = "SLOT " + slotNumber + (exists ? " (LOAD)" : " (EMPTY)");
	    JButton btn = createStyledButton(buttonText, 300);
	    
	    btn.addActionListener(_ -> {
	    	SoundManager.getInstance().playClickSound();
	    	if (!exists) {
                navigator.navigateTo("Setup");
                return;
            }
	    	try {
                GameEngine loadedEngine = (GameEngine) LogHandler.loadGameSession(sessionName);
                ActualGamePanel gamePanel = (ActualGamePanel) navigator.getActualGamePanel();
                gamePanel.loadSession(loadedEngine);
                navigator.navigateTo("TheGame");
                LogHandler.logEvent(loadedEngine.getPlayer().getUsername(), "Session loaded from slot " + slotNumber);
            } catch (GameDataException ex) {
                JOptionPane.showMessageDialog(this, "Load error: " + ex.getMessage());
            }
	    });

	    add(btn);
	    add(Box.createRigidArea(new Dimension(0, 15)));
	}
}

package view;

import java.awt.*;
import javax.swing.*;

import service.SoundManager;

/**
 * Provides a user interface for adjusting audio preferences, 
 * including sound effects and background music toggles.
 * @author Muhammed Cagan Goktas
 */
public class SettingsPanel extends BasePanel {
    private static final long serialVersionUID = 1004L;

    /**
     * Constructs the settings panel with a unique settings-themed background.
     * @param navigator the {@link ViewNavigator} for returning to the main menu
     */
	public SettingsPanel(ViewNavigator navigator) {
        super(navigator, "settings_menu_bg");
    }

	/**
     * Builds the settings interface, featuring interactive checkboxes linked to 
     * the {@link SoundManager} for real-time audio control.
     */
    @Override
    protected void setupUI() {
        add(Box.createRigidArea(new Dimension(0, 80)));

        JLabel title = new JLabel("SETTINGS");
        title.setFont(new Font("Lucida Calligraphy", Font.BOLD, 45));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        add(Box.createRigidArea(new Dimension(0, 100)));

        JCheckBox soundCheck = new JCheckBox("Sound Effects", false);
        soundCheck.setAlignmentX(Component.CENTER_ALIGNMENT);
        soundCheck.setOpaque(false);
        soundCheck.setForeground(Color.WHITE);
        soundCheck.addActionListener(_ -> {
            SoundManager.getInstance().setSoundOn(soundCheck.isSelected());
        });
        add(soundCheck);

        add(Box.createRigidArea(new Dimension(0, 20)));
        
        JCheckBox musicCheck = new JCheckBox("Background Music", false);
        musicCheck.setAlignmentX(Component.CENTER_ALIGNMENT);
        musicCheck.setOpaque(false);
        musicCheck.setForeground(Color.WHITE);
        musicCheck.addActionListener(_ -> {
            if (musicCheck.isSelected()) SoundManager.getInstance().startBackgroundMusic();
            else SoundManager.getInstance().stopBackgroundMusic();
        });
        add(musicCheck);

        add(Box.createRigidArea(new Dimension(0, 30)));

        createMenuButton("BACK TO MENU", "Menu", 200);
    }
}
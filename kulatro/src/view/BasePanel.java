package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import service.SoundManager;

/**
 * Provides a common foundation for all game panels, handling background rendering, 
 * uniform button styling, and navigation logic.
 * @author Muhammed Cagan Goktas
 */
public abstract class BasePanel extends JPanel {
    private static final long serialVersionUID = 1000L;
	protected ViewNavigator navigator;
    protected Image backgroundImage;

    /**
     * Constructs a base panel with a specific background image and navigation controller.
     * @param navigator the {@link ViewNavigator} instance for screen transitions
     * @param fileName the name of the background image file (without extension)
     */
    public BasePanel(ViewNavigator navigator, String fileName) {
        this.navigator = navigator;
        this.backgroundImage = new ImageIcon("resources/images/" + fileName + ".png").getImage();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setupUI();
    }
    
    /**
     * Abstract method to be implemented by subclasses to build their specific UI components.
     */
    protected abstract void setupUI();
    
    /**
     * Creates a JButton with a standardized dark theme, hover effects, and custom font.
     * @param text the label displayed on the button
     * @param width the preferred width of the button
     * @return a styled {@link JButton} instance
     */
    protected JButton createStyledButton(String text, int width) {
    	JButton btn = new JButton(text);
        
        Dimension btnSize = new Dimension(width, 45);
        btn.setPreferredSize(btnSize);
        btn.setMaximumSize(btnSize);
        btn.setMinimumSize(btnSize);
        
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(new Font("SansSerif", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setBorderPainted(true);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        
        Color normalColor = new Color(20, 20, 20, 180);
        Color hoverColor = new Color(60, 60, 60, 220);
        
        btn.setBackground(normalColor);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100, 100), 1));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hoverColor);
                btn.setOpaque(true);
                btn.repaint();
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(normalColor);
                btn.setOpaque(true);
                btn.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                btn.setBackground(normalColor);
                btn.setOpaque(true);
                btn.repaint();
            }
        });
        
        return btn;
    }
    
    /**
     * Helper method to create a menu button with default width and add it to the panel.
     * @param text the button text
     * @param target the navigation target string
     */
    protected void createMenuButton(String text, String target) {
        createMenuButton(text, target, 250);
    }
    
    /**
     * Creates a menu button with navigation logic and a sound effect on click.
     * @param text the button text
     * @param target the screen name to navigate to, or "EXIT" to close the application
     * @param width the custom width of the button
     */
    protected void createMenuButton(String text, String target, int width) {
        JButton btn = createStyledButton(text, width);

        btn.addActionListener(_ -> {
            SoundManager.getInstance().playClickSound();
            if (target.equals("EXIT")) System.exit(0);
            else navigator.navigateTo(target);
        });

        add(btn);
        add(Box.createRigidArea(new Dimension(0, 20)));
    }

    /**
     * Paints the background image and a slight dark overlay to the panel.
     * @param g the {@link Graphics} context used for drawing
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            g.setColor(new Color(0, 0, 0, 10)); 
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    /**
     * Gets the navigator associated with this panel.
     * @return the {@link ViewNavigator} instance
     */
	public ViewNavigator getNavigator() {
		return navigator;
	}
    
}
package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxUI;

import service.AuthService;
import service.SoundManager;

/**
 * Handles the game configuration interface where players select their researcher name, 
 * deck type, difficulty, and special cards before initializing the game engine.
 * @author Muhammed Cagan Goktas
 */
public class SetupPanel extends BasePanel {
	private static final long serialVersionUID = 1006L;
	private JTextField nameField;
	private JComboBox<String> difficultyCombo;
	private JComboBox<String> deckCombo;
	private JComboBox<String> specialCardCombo;
	
	/** Matrix of special cards mapped to their respective deck types (Alchemy, Element, Quantum). */
	private static final String[][] SPECIAL_CARDS = {
	        {"Philosopher's Stone", "Transmutation", "Elemental Fusion", "Catalyst"},
	        {"Periodic Boost", "Noble Gas", "Isotope Decay", "Electron Bond"},
	        {"Quantum Entanglement", "Superposition", "Gluon Bind", "Photon Burst"}
	    };
    
	/**
	 * Constructs the setup panel with the main menu background.
	 * @param navigator the {@link ViewNavigator} for navigating to the game view
	 */
	public SetupPanel(ViewNavigator navigator) {
		super(navigator, "main_menu_bg");
	}

	/**
	 * Sets up the configuration form with customized inputs and dynamic 
	 * action listeners for deck-special card synchronization.
	 */
	@Override
	protected void setupUI() {
		add(Box.createRigidArea(new Dimension(0, 100))); 

	    JLabel nameLabel = new JLabel("RESEARCHER IDENTIFIER");
	    nameLabel.setForeground(Color.GRAY);
	    nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    add(nameLabel);
	    
	    add(Box.createRigidArea(new Dimension(0, 10)));

	    nameField = new JTextField();
	    nameField.setBackground(new Color(30, 30, 30));
	    nameField.setForeground(Color.WHITE);
	    nameField.setBorder(BorderFactory.createCompoundBorder(
	    	    BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
	    	    BorderFactory.createEmptyBorder(0, 10, 0, 10) 
	    	));
	    
	    Dimension fieldSize = new Dimension(350, 35);
	    nameField.setMaximumSize(fieldSize);
	    nameField.setPreferredSize(fieldSize);
	    nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
	    add(nameField);
	    add(Box.createRigidArea(new Dimension(0, 30)));

	    JLabel deckLabel = new JLabel("DECK:");
	    deckLabel.setForeground(Color.GRAY);
	    deckLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    add(deckLabel);
	    add(Box.createRigidArea(new Dimension(0, 10)));
	    String[] decks = {"Alchemy", "Element", "Quantum"};
	    deckCombo = createStyledComboBox(decks);
	    add(deckCombo);
	    add(Box.createRigidArea(new Dimension(0, 20)));
	    
	    JLabel specialLabel = new JLabel("SPECIAL CARD:");
        specialLabel.setForeground(Color.GRAY);
        specialLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(specialLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        specialCardCombo = createStyledComboBox(SPECIAL_CARDS[0]);
        add(specialCardCombo);
        add(Box.createRigidArea(new Dimension(0, 20)));
 
        deckCombo.addActionListener(_ -> {
            int idx = deckCombo.getSelectedIndex();
            specialCardCombo.removeAllItems();
            for (String s : SPECIAL_CARDS[idx]) specialCardCombo.addItem(s);
        });
	    
	    JLabel diffLabel = new JLabel("EXPERIMENT PARAMETERS");
	    diffLabel.setForeground(Color.GRAY);
	    diffLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    add(diffLabel);
	    add(Box.createRigidArea(new Dimension(0, 10)));
	    String[] diffs = {"Easy", "Medium", "Hard"};
	    difficultyCombo = createStyledComboBox(diffs);
	    add(difficultyCombo);
	    add(Box.createRigidArea(new Dimension(0, 30)));

	    JButton startBtn = createStyledButton("START SYSTEM", 350);
	    startBtn.addActionListener(_ -> {
	    	SoundManager.getInstance().playClickSound();
	    	String name = nameField.getText().trim();
	    	if (name.isEmpty()) name = "Researcher";
	        String diff = (String) difficultyCombo.getSelectedItem();
	        String deck = (String) deckCombo.getSelectedItem();
	        String specialCard = (String) specialCardCombo.getSelectedItem();

	        ActualGamePanel gamePanel = (ActualGamePanel) navigator.getActualGamePanel(); 
	        AuthService auth = ((MainFrame) navigator).getAuthService();
	        
	        gamePanel.initSession(name, diff, deck, specialCard, auth);
            navigator.navigateTo("TheGame");
        });
	    add(startBtn);

	    add(Box.createRigidArea(new Dimension(0, 20)));
		createMenuButton("BACK", "StartPanel");
	}
	
	/**
	 * Creates a JComboBox with heavily customized UI to match the dark aesthetic.
	 * Overrides standard arrow buttons, background painting, and cell rendering.
	 * @param items the content to populate the combo box
	 * @return a styled {@link JComboBox}
	 */
	private JComboBox<String> createStyledComboBox(String[] items) {
	    JComboBox<String> comboBox = new JComboBox<>(items);

	    comboBox.setUI(new BasicComboBoxUI() {
	        @Override
	        protected JButton createArrowButton() {
	            JButton button = super.createArrowButton();
	            button.setBackground(new Color(30, 30, 30));
	            button.setBorder(BorderFactory.createEmptyBorder());
	            button.setContentAreaFilled(false);
	            button.setOpaque(true);
	            return button;
	        }

	        @Override
	        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
	            g.setColor(new Color(30, 30, 30));
	            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
	        }
	        
	        @Override
	        public void installUI(JComponent c) {
	            super.installUI(c);
	            comboBox.setOpaque(true);
	        }
	    });
	    
	    comboBox.setBackground(new Color(20, 20, 20));
	    comboBox.setForeground(Color.WHITE);
	    comboBox.setFont(new Font("Consolas", Font.PLAIN, 15));
	    comboBox.setFocusable(false); 
	    comboBox.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));

	    comboBox.setRenderer(new DefaultListCellRenderer() {
	        @Override
	        public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
	                                                      boolean isSelected, boolean cellHasFocus) {
	            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	            
	            label.setBackground(isSelected ? new Color(50, 50, 50) : new Color(20, 20, 20));
	            label.setForeground(isSelected ? Color.WHITE : Color.GRAY);
	            label.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
	            return label;
	        }
	    });

	    comboBox.setMaximumSize(new Dimension(350, 35));
	    comboBox.setPreferredSize(new Dimension(350, 35));
	    comboBox.setAlignmentX(Component.CENTER_ALIGNMENT);

	    return comboBox;
	}
	
}

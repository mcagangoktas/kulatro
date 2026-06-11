package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import exception.GameDataException;
import exception.InvalidHandException;
import model.Card;
import model.DeckType;
import model.Difficulty;
import model.Player;
import model.SpecialCard;
import model.special.Catalyst;
import model.special.ElectronBond;
import model.special.ElementalFusion;
import model.special.GluonBind;
import model.special.IsotopeDecay;
import model.special.NobleGas;
import model.special.PeriodicBoost;
import model.special.PhilosophersStone;
import model.special.PhotonBurst;
import model.special.QuantumEntanglement;
import model.special.Superposition;
import model.special.Transmutation;
import service.AuthService;
import service.GameEngine;
import service.LogHandler;
import service.SoundManager;

/**
 * Represents the main gameplay interface panel where the user interacts with cards, 
 * activates special abilities, tracks session statistics, and logs actions.
 * @author Muhammed Cagan Goktas
 */
public class ActualGamePanel extends BasePanel {
	private static final long serialVersionUID = 1007L;
	private GameEngine engine;
	private AuthService authService;
	private JPanel cardContainer;
    private JLabel statsLabel;
    private JTextArea logConsole;
    private JButton specialCardBtn;
    private List<Card> selectedCards = new ArrayList<>();
    
    /**
	 * Constructs the gameplay panel and binds it to the application navigator.
	 * @param navigator the {@link ViewNavigator} used for switching screens
	 */
	public ActualGamePanel(ViewNavigator navigator) {
		super(navigator, "game_bg");
	}
	
	/**
	 * Initializes a brand new game session state, maps dependencies, and resets UI configurations.
	 * @param playerName the active user profile name
	 * @param difficultyStr the difficulty level selected in configuration screens
	 * @param deckStyleStr the visual/mechanical theme of the deck
	 * @param specialCardName the name of the assigned special power card
	 * @param authService the centralized authentication and user profile service
	 */
	public void initSession(String playerName, String difficultyStr, String deckStyleStr, String specialCardName, AuthService authService) {
		this.authService = authService;
		if (cardContainer != null) cardContainer.removeAll();
	    if (logConsole != null) logConsole.setText("");
	    
		Difficulty diff = Difficulty.valueOf(difficultyStr.toUpperCase());
        DeckType style = DeckType.valueOf(deckStyleStr.toUpperCase());
        Player player = new Player(playerName);
        
        this.engine = new GameEngine(player, style, diff);
        engine.setSpecialCard(createSpecialCard(specialCardName, style));
        
        refreshUI();
        
        logConsole.append("> KULATRO System Initialized...\n");
        logConsole.append("> Researcher: " + playerName + "\n");
        logConsole.append("> Special Card: " + specialCardName + "\n");

    }
	
	/**
	 * Factory helper mapping text definitions to target concrete {@link SpecialCard} model classes.
	 */
	private SpecialCard createSpecialCard(String name, DeckType style) {
        switch (name) {
            case "Philosopher's Stone":   return new PhilosophersStone();
            case "Transmutation":         return new Transmutation();
            case "Elemental Fusion":      return new ElementalFusion();
            case "Catalyst":              return new Catalyst();
            case "Periodic Boost":        return new PeriodicBoost();
            case "Noble Gas":             return new NobleGas();
            case "Isotope Decay":         return new IsotopeDecay();
            case "Electron Bond":         return new ElectronBond();
            case "Quantum Entanglement":  return new QuantumEntanglement();
            case "Superposition":         return new Superposition();
            case "Gluon Bind":            return new GluonBind();
            case "Photon Burst":          return new PhotonBurst();
            default: return null;
        }
    }
	
	/**
	 * Shows a tooltip/description for a special card effect.
	 */
	private void showSpecialCardTooltip(SpecialCard card) {
		String cardName = card.getName();
		String description = getCardDescription(cardName);
		
		JOptionPane.showMessageDialog(this, 
			description, 
			cardName + " - Effect Description",
			JOptionPane.INFORMATION_MESSAGE);
	}
	
	private String getCardDescription(String cardName) {
		return switch(cardName) {
			case "Philosopher's Stone" -> 
				"Doubles your hand score this round.\n\n" +
				"Effect: Score × 2\n" +
				"Limitation: Can only be used once per game.";
			case "Transmutation" -> 
				"Swap one card in your hand with a random card from the deck.\n\n" +
				"Effect: Card replacement\n" +
				"Limitation: Can be used once per round.";
			case "Elemental Fusion" -> 
				"Combine 2 cards of the same type into a virtual set of 4.\n\n" +
				"Effect: Pair counts as 4 cards on submit\n" +
				"Limitation: Can be used once per round.";
			case "Catalyst" -> 
				"Apply +1 multiplier to a pair of cards.\n\n" +
				"Effect: Selected pair gets +1 score multiplier\n" +
				"Limitation: Can be used once per round.";
			case "Periodic Boost" -> 
				"All cards in this round receive +2 value bonus.\n\n" +
				"Effect: All card values + 2\n" +
				"Limitation: Can be used once per round.";
			case "Noble Gas" -> 
				"Lock a card so its value counts twice.\n\n" +
				"Effect: Selected card value × 2\n" +
				"Limitation: Can be used once per round.";
			case "Isotope Decay" -> 
				"Reduce your hand score by 50% this round.\n\n" +
				"Effect: Score ÷ 2\n" +
				"Limitation: Can only be used once per game.";
			case "Electron Bond" -> 
				"Create a bonded pair with 2 different card types for +1 multiplier.\n\n" +
				"Effect: Pair gets +1 multiplier\n" +
				"Limitation: Can be used once per round.";
			case "Quantum Entanglement" -> 
				"Triple the score value of a single card.\n\n" +
				"Effect: Selected card value × 3\n" +
				"Limitation: Can be used once per round.";
			case "Superposition" -> 
				"Submit two hands and choose the better result!\n\n" +
				"Effect: Hand comparison & best result selected\n" +
				"Limitation: Can be used once per round.";
			case "Gluon Bind" -> 
				"Merge two cards into one super card.\n\n" +
				"Effect: Cards combine (special calculation)\n" +
				"Limitation: Can be used once per round.";
			case "Photon Burst" -> 
				"Peek at top 3 deck cards and swap with any hand card.\n\n" +
				"Effect: Strategic card exchange\n" +
				"Limitation: Can be used once per round.";
			default -> "No description available for this card.";
		};
	}
	
	/**
	 * Builds layout frames, operational buttons, text logging areas, and registers mouse event listeners.
	 */
	@Override
	protected void setupUI() {
		this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
        JPanel northPanel = new JPanel(new GridLayout(1, 2));
        northPanel.setOpaque(false);
        statsLabel = new JLabel();
        statsLabel.setForeground(Color.WHITE);
        statsLabel.setFont(new Font("Consolas", Font.BOLD, 18));
        northPanel.add(statsLabel);
        add(northPanel, BorderLayout.NORTH);

        cardContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 30));
        cardContainer.setOpaque(false);
        add(cardContainer, BorderLayout.CENTER);

        specialCardBtn = new JButton();
        specialCardBtn.setPreferredSize(new Dimension(130, 210));
        specialCardBtn.setBorder(BorderFactory.createEmptyBorder());
        specialCardBtn.setContentAreaFilled(false);
	    specialCardBtn.addActionListener(_ -> {
	    	SoundManager.getInstance().playClickSound();
	    	
	    	int response = JOptionPane.showConfirmDialog(
	    	        this, 
	    	        "Do you want to use this special card?", 
	    	        "Confirmation", 
	    	        JOptionPane.YES_NO_OPTION, 
	    	        JOptionPane.QUESTION_MESSAGE
	    	    );
	    	if (response != JOptionPane.YES_OPTION) {
	            logConsole.append("> Special card usage is cancelled.\n");
	            return; 
	        }
	    	
	    	handleSpecialCard();	
	    });

	    specialCardBtn.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            if (e.getButton() == MouseEvent.BUTTON3) {
	                SpecialCard card = engine.getSpecialCard();
	                if (card != null) {
	                    showSpecialCardTooltip(card);
	                }
	            }
	        }
	    });
	    
	    JPanel westPanel = new JPanel(new GridBagLayout());
        westPanel.setOpaque(false);
        westPanel.setPreferredSize(new Dimension(140, 0));
        westPanel.add(specialCardBtn);
        add(westPanel, BorderLayout.WEST);
	    
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        
        JButton submitBtn = createStyledButton("SUBMIT EXPERIMENT", 200);
        submitBtn.addActionListener(_ -> {
	    	SoundManager.getInstance().playClickSound();
	    	try {
				handleSubmit();
			} catch (InvalidHandException e) {
				e.printStackTrace();
			}	
	    });
        
        JButton discardBtn = createStyledButton("DISCARD & SWAP", 200);
        discardBtn.addActionListener(_ -> {
	    	SoundManager.getInstance().playClickSound();
	    	handleDiscard();	
	    });
        
        JButton saveBtn = createStyledButton("SAVE GAME", 150);
        saveBtn.addActionListener(_ -> {
	    	SoundManager.getInstance().playClickSound();
	    	handleSave();	
	    });

        btnPanel.add(discardBtn);
        btnPanel.add(submitBtn);
        btnPanel.add(saveBtn);
        southPanel.add(btnPanel, BorderLayout.NORTH);

        logConsole = new JTextArea(5, 20);
        logConsole.setBackground(new Color(10, 10, 10));
        logConsole.setForeground(new Color(0, 255, 0));
        logConsole.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logConsole.setEditable(false);
        JScrollPane logScroll = new JScrollPane(logConsole);
        logScroll.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        southPanel.add(logScroll, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);
    }
	
	/**
	 * Loads and downscales asset images to match regular playing card GUI proportions.
	 */
	private ImageIcon getScaledIcon(String path) {
	    try {
	    	ImageIcon icon = new ImageIcon(path);
	        Image img = icon.getImage();
	        Image scaledImg = img.getScaledInstance(180, 250, Image.SCALE_SMOOTH);
	        return new ImageIcon(scaledImg);
	    } catch (Exception e) {
	        System.err.println("Görsel yüklenemedi: " + path);
	        return null;
	    }
	}
	
	/**
	 * Loads and downscales asset images to fit customized layout boundary sizes.
	 */
	private ImageIcon getScaledIcon(String path, int width, int height) {
	    try {
	    	ImageIcon icon = new ImageIcon(path);
	        Image img = icon.getImage();
	        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	        return new ImageIcon(scaledImg);
	    } catch (Exception e) {
	        System.err.println("Görsel yüklenemedi: " + path);
	        return null;
	    }
	}
	
	/**
	 * Intercepts active special card activations and evaluates contextual game board mutations.
	 */
	private void handleSpecialCard() {
		SpecialCard card = engine.getSpecialCard();
        if (card == null) return;
        
        if (card instanceof Superposition) {
        	Superposition superpositionCard = (Superposition) card;
            
            if (superpositionCard.isUsed()) {
                logConsole.append("> Superposition is already used! You can submit your hand.\n");
                return;
            }
            
            superpositionCard.performAction(engine.getPlayer(), engine.getDeck());
            logConsole.append("> Superposition ACTIVATED! Your next TWO hand submissions will be compared.\n");
            refreshUI();
            return;
        }
        
        if (card instanceof ElementalFusion) {
            if (selectedCards.size() != 2) {
                logConsole.append("> Elemental Fusion: Select exactly 2 cards to fuse.\n");
                return;
            }

            if (!selectedCards.get(0).getType().equals(selectedCards.get(1).getType())) {
                logConsole.append("> Elemental Fusion: Selected cards must be of the same type!\n");
                return;
            }
            
            if (!card.isUsed()) {
                ElementalFusion fusionCard = (ElementalFusion) card;
                fusionCard.setFusedCards(selectedCards);
                fusionCard.performAction(engine.getPlayer(), engine.getDeck());
                logConsole.append("> Elemental Fusion: Pair selected! This pair will count as a set of 4 cards on submit!\n");
            } else {
                logConsole.append("> Elemental Fusion: Already used this round!\n");
            }
            
            selectedCards.clear();
            refreshUI();
            return;
        }
        
        if (card instanceof Catalyst) {
            if (selectedCards.size() != 2) {
                logConsole.append("> Catalyst: Select exactly 2 cards to apply catalyst.\n");
                return;
            }
            
            if (!card.isUsed()) {
                Catalyst catalystCard = (Catalyst) card;
                catalystCard.setCatalyzedCards(selectedCards);
                catalystCard.performAction(engine.getPlayer(), engine.getDeck());
                logConsole.append("> Catalyst: Pair selected! This pair will receive a +1 multiplier bonus on submit.\n");
            } else {
                logConsole.append("> Catalyst: Already used this round!\n");
            }
            
            selectedCards.clear();
            refreshUI();
            return;
        }
        
        if (card instanceof PeriodicBoost) {
        	if (!card.isUsed()) {
                card.performAction(engine.getPlayer(), engine.getDeck());
                logConsole.append("> Periodic Boost: Activated! All cards in this round get +2 value.\n");
            } else {
                logConsole.append("> Periodic Boost: Already used this round!\n");
            }
            refreshUI();
            return;
        }
 
        if (card instanceof IsotopeDecay) {
        	if (!card.isUsed()) {
                card.performAction(engine.getPlayer(), engine.getDeck());
                logConsole.append("> Isotope Decay: Hand replaced!\n");
                engine.setSpecialCard(null);
            } else {
                logConsole.append("> Isotope Decay: Already used this round!\n");
            }
            refreshUI();
            return;
        }
 
        if (card instanceof PhilosophersStone) {
        	if (!card.isUsed()) {
        		card.performAction(engine.getPlayer(), engine.getDeck());
                logConsole.append("> Philosopher's Stone: Will double your score this round!\n");
        	} else {
        		logConsole.append("> Philosopher's Stone: Already used this round!\n");
        	}
            refreshUI();
            return;
        }
 
        if (card instanceof Transmutation) {
        	if (selectedCards.size() != 1) {
                logConsole.append("> Transmutation: Select exactly 1 card to swap.\n");
                return;
            }
            
            if (!card.isUsed()) {
                ((Transmutation) card).executeSwap(selectedCards.get(0), engine.getPlayer(), engine.getDeck());
                card.performAction(engine.getPlayer(), engine.getDeck());
                logConsole.append("> Transmutation: Card swapped!\n");
            } else {
                logConsole.append("> Transmutation: Already used this round!\n");
            }
            
            selectedCards.clear();
            refreshUI();
            return;
        }
 
        if (card instanceof NobleGas) {
        	if (selectedCards.size() != 1) {
                logConsole.append("> Noble Gas: Select exactly 1 card to lock.\n");
                return;
            }
            
            if (!card.isUsed()) {
                ((NobleGas) card).executeLock(selectedCards.get(0));
                card.performAction(engine.getPlayer(), engine.getDeck());
                logConsole.append("> Noble Gas: Card locked! Value counts twice.\n");
            } else {
                logConsole.append("> Noble Gas: Already used this round!\n");
            }
            
            selectedCards.clear();
            refreshUI();
            return;
        }
        
        if (card instanceof ElectronBond) {
            if (selectedCards.size() != 2) {
                logConsole.append("> Electron Bond: Select exactly 2 different cards to bond as a pair.\n");
                return;
            }
            
            Card card1 = selectedCards.get(0);
            Card card2 = selectedCards.get(1);
            
            if (card1.getType().equals(card2.getType())) {
                logConsole.append("> WARNING: Electron Bond requires two DIFFERENT card types!\\n");
                return;
            }
            
            if (!card.isUsed()) {
                ((ElectronBond) card).setBondedCards(selectedCards);
                card.performAction(engine.getPlayer(), engine.getDeck());
                engine.setSpecialCard(card);
                logConsole.append("> Electron Bond: Selected cards are now bonded as a pair (x2 multiplier)!\n");
            } else {
                logConsole.append("> Electron Bond: Already used this round!\n");
            }
            
            selectedCards.clear();
            refreshUI();
            return;
        }

        if (card instanceof QuantumEntanglement) {
        	if (selectedCards.size() != 1) {
                logConsole.append("> Quantum Entanglement: Select exactly 1 card.\n");
                return;
            }
            
            if (!card.isUsed()) {
                ((QuantumEntanglement) card).executeEntangle(selectedCards.get(0));
                card.performAction(engine.getPlayer(), engine.getDeck());
                logConsole.append("> Quantum Entanglement: Card score tripled!\n");
            } else {
                logConsole.append("> Quantum Entanglement: Already used this round!\n");
            }
            
            selectedCards.clear();
            refreshUI();
            return;
        }
 
        if (card instanceof GluonBind) {
        	GluonBind gluonCard = (GluonBind) card;
        	
            if (selectedCards.size() != 2) {
                logConsole.append("> Gluon Bind: Select exactly 2 cards to merge.\n");
                return;
            }
            
            for (Card c : selectedCards) {
                if (c.isLocked()) {
                    logConsole.append("> WARNING: Locked cards cannot be merged!\n");
                    return;
                }
            }
            
            if (!card.isUsed()) {
                gluonCard.performAction(engine.getPlayer(), engine.getDeck());
                
                Card c1 = selectedCards.get(0);
                Card c2 = selectedCards.get(1);
                logConsole.append("> Gluon Bind worked! " + c1.getName() + " and " + c2.getName() + " are being merged...\n");
                gluonCard.merge(c1, c2, engine.getPlayer(), engine.getDeck());
                logConsole.append("> Gluon Bind: Cards merged!\n");
            } else {
                logConsole.append("> Gluon Bind: Already used this round!\n");
            }
            
            selectedCards.clear();
            refreshUI();
            return;
        }
 
        if (card instanceof PhotonBurst) {
        	if (!card.isUsed()) {
        		card.performAction(engine.getPlayer(), engine.getDeck());
                handlePhotonBurst();
        	} else {
        		logConsole.append("> Photon Burst: Already used this round!\n");
        		refreshUI();
        	}
            return;
        }
    }

	/**
	 * Manages the specific deck peering UI choices and substitution prompts for Photon Burst.
	 */
	private void handlePhotonBurst() {
        List<Card> peeked = engine.getDeck().peekTopCards(3);
        if (peeked.isEmpty()) {
            logConsole.append("> Photon Burst: No cards in deck!\n");
            //engine.setSpecialCard(null);
            refreshUI();
            return;
        }
 
        StringBuilder sb = new StringBuilder("Next cards in deck:\n");
        for (int i = 0; i < peeked.size(); i++) {
            sb.append(i).append(": ").append(peeked.get(i).getName()).append("\n");
        }
        sb.append("\nEnter deck card index to swap (or -1 to cancel):");
        String deckIdxStr = JOptionPane.showInputDialog(this, sb.toString());
        if (deckIdxStr == null || deckIdxStr.equals("-1")) {
        	logConsole.append("> Photon Burst: Action cancelled by player.\n");
        	//engine.setSpecialCard(null);
        	refreshUI();
            return;
        };
 
        List<Card> hand = engine.getPlayer().getHand();
        StringBuilder sb2 = new StringBuilder("Your hand:\n");
        for (int i = 0; i < hand.size(); i++) {
            sb2.append(i).append(": ").append(hand.get(i).getName()).append("\n");
        }
        sb2.append("\nEnter hand card index to replace:");
        String handIdxStr = JOptionPane.showInputDialog(this, sb2.toString());
        if (handIdxStr == null) {
        	logConsole.append("> Photon Burst: Action cancelled during hand selection.\n");
            //engine.setSpecialCard(null);
            refreshUI();
            return;
        };
 
        try {
            int deckIdx = Integer.parseInt(deckIdxStr.trim());
            int handIdx = Integer.parseInt(handIdxStr.trim());
            if (deckIdx >= 0 && deckIdx < peeked.size() && handIdx >= 0 && handIdx < hand.size()) {
                Card fromDeck = engine.getDeck().takeCard(deckIdx);
                Card fromHand = hand.get(handIdx);
                hand.set(handIdx, fromDeck);
                engine.getDeck().addToDiscardPile(fromHand);
                logConsole.append("> Photon Burst: Swapped " + fromHand.getName() + " with " + fromDeck.getName() + "\n");
            } else {
                logConsole.append("> Photon Burst: Invalid indices entered. Action wasted.\n");
            }
        } catch (NumberFormatException e) {
            logConsole.append("> Invalid input.\n");
        }

        selectedCards.clear();
        refreshUI();
    }
	
	/**
	 * Completely rebuilds the visible card grid components, selection colored borders, and text labels.
	 */
	private void refreshUI() {
        int round = engine.getRoundManager().getCurrentRound();
        int target = engine.getRoundManager().getTotalTargetScore();
        int total = engine.getScoreManager().getTotalScore();
        String deckType = engine.getDeck().getStyle().getDisplayName();
        int remaining = engine.getDeck().getRemainingCount();
        
        statsLabel.setText(String.format("ROUND: %d/4 | TOTAL: %d | TOTAL TARGET: %d | DISCARDS: %d/4 (total %d/6) | DECK: %s | REMAINING: %d",
                round, total, target,
                engine.getPlayer().getRoundDiscards(),
                engine.getPlayer().getUsedDiscards(),
                deckType.substring(0, deckType.indexOf(" ")), remaining));
        cardContainer.removeAll();
        
        SpecialCard sc = engine.getSpecialCard();
        if (sc == null) {
            specialCardBtn.setVisible(false);
        } else {
            ImageIcon icon = getScaledIcon(sc.getImagePath(), 130, 210);
            if (icon != null) specialCardBtn.setIcon(icon);
            else specialCardBtn.setText(sc.getName());
            specialCardBtn.setVisible(true);
        }
        
        List<Card> hand = engine.getPlayer().getHand();
        for (Card card : hand) {
            JButton cardBtn = new JButton();
            ImageIcon cardIcon = getScaledIcon(card.getImagePath());
            if (cardIcon != null) cardBtn.setIcon(cardIcon);
            else cardBtn.setText(card.getName());
            
            cardBtn.setPreferredSize(new Dimension(180, 250));
            cardBtn.setContentAreaFilled(false);
            
            if (card.isLocked()) {
                cardBtn.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
            } else if (selectedCards.contains(card)) {
                cardBtn.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
            } else {
                cardBtn.setBorder(BorderFactory.createEmptyBorder());
            }
            
            cardBtn.addActionListener(_ -> {
                if (selectedCards.contains(card)) {
                    selectedCards.remove(card);
                    cardBtn.setBorder(card.isLocked() ? 
                            BorderFactory.createLineBorder(Color.CYAN, 3) : 
                            BorderFactory.createEmptyBorder());
                    logConsole.append("> Card untoggled: " + card.getName() + "\n");
                } else {
                    selectedCards.add(card);
                    cardBtn.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
                    logConsole.append("> Card toggled: " + card.getName() + "\n");
                }
            });
            cardContainer.add(cardBtn);
        }
        cardContainer.revalidate();
        cardContainer.repaint();
	}
	
	/**
     * Updates the panel to display the state of a loaded save game.
     * @param loadedEngine the {@link GameEngine} object containing the saved data
     */
	public void loadSession(GameEngine loadedEngine) {
        this.engine = loadedEngine;
        selectedCards.clear();
        if (logConsole != null) logConsole.setText("");
        refreshUI();
        logConsole.append("> Session loaded: " + loadedEngine.getSessionName() + "\n");
    }
	
	/**
	 * Prompts slot dialogue overlays and converts the active session state into persistent disk files.
	 */
	private void handleSave() {
        String[] slots = {"Slot 1", "Slot 2", "Slot 3"};
        int choice = JOptionPane.showOptionDialog(this, "Select save slot:", "Save Game",
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, slots, slots[0]);
        if (choice == JOptionPane.CLOSED_OPTION) return;
 
        String sessionName = "slot" + (choice + 1);
        try {
            LogHandler.saveGameSession(sessionName, engine);
            logConsole.append("> Game saved to " + sessionName + "\n");
        } catch (GameDataException e) {
            logConsole.append("> Save failed: " + e.getMessage() + "\n");
        }
    }

	/**
	 * Forwards the verified chosen card group composition down to logic checking systems.
	 * @throws InvalidHandException 
	 */
	private void handleSubmit() throws InvalidHandException {
	    if (selectedCards.isEmpty()) {
	        logConsole.append("> WARNING: Please select at least 1 card to submit!\n");
	        return;
	    }

	    SpecialCard activeSpecial = engine.getSpecialCard();
	    
	    if (activeSpecial instanceof Superposition && activeSpecial.isUsed()) {
	        Superposition superpositionCard = (Superposition) activeSpecial;
	        
	        if (!superpositionCard.isFirstHandSubmitted()) {
	            int firstScore = engine.getScoreManager().calculateHandScore(selectedCards, null);
	            superpositionCard.registerFirstHand(firstScore);
	            
	            logConsole.append("> Superposition: First hand state registered! Score: " + firstScore + "\n");
	            logConsole.append("> Your hand will be refilled. Submit a second hand to choose the better result!\n");
	            
	            engine.discardAndSwap(selectedCards);
	            selectedCards.clear();
	            refreshUI();
	            return;
	        } 
	        else {
	            logConsole.append("> Superposition: Second hand state registered! Collapsing quantum states...\n");
	        }
	    }

	    engine.submitHand(selectedCards);

	    if (activeSpecial instanceof Superposition && activeSpecial.isUsed() && !((Superposition) activeSpecial).isFirstHandSubmitted()) {
	        engine.setSpecialCard(null); 
	    }

	    selectedCards.clear();
	    logConsole.append("> Hand submitted successfully. Proceeding to next step.\n");
	    refreshUI();
	    
	    if (engine.isGameOver()) { 
	        handleEndGame();
	    }
	}
	
	/**
	 * Filters rules and transfers designated targets out into discard areas to trigger a card swap.
	 */
	private void handleDiscard() {
        if (engine.getPlayer().getUsedDiscards() >= 6) {
        	logConsole.append("> WARNING: Total discard limit reached (6/6)!\n");
            return;
        }
        if (selectedCards.isEmpty()) {
            logConsole.append("> WARNING: Select cards to discard first!\n");
            return;
        }
        
        int roundAfter = engine.getPlayer().getRoundDiscards() + selectedCards.size();
        if (roundAfter > 4) {
            logConsole.append("> WARNING: Cannot discard " + selectedCards.size() + " cards! Round would exceed 4. (Current: " + engine.getPlayer().getRoundDiscards() + "/4)\n");
            return;
        }
        
        int totalAfter = engine.getPlayer().getUsedDiscards() + selectedCards.size();
        if (totalAfter > 6) {
            logConsole.append("> WARNING: Cannot discard " + selectedCards.size() + " cards! Total would exceed 6. (Current: " + engine.getPlayer().getUsedDiscards() + "/6)\n");
            return;
        }
        
        for (Card c : selectedCards) {
            if (c.isLocked()) {
                logConsole.append("> WARNING: Cannot discard a locked card!\n");
                return;
            }
        }
        engine.discardAndSwap(selectedCards);
        selectedCards.clear();
        refreshUI();
    }
	
	/**
	 * Assesses absolute score averages, updates statistics registries, and pushes to final Win/Loss views.
	 */
	private void handleEndGame() {
        boolean won = engine.getScoreManager().checkGameWin();
        int totalScore = engine.getScoreManager().getTotalScore();
 
        if (authService != null) {
            try {
                authService.updateStats(engine.getSessionName(), totalScore, won);
            } catch (GameDataException e) {
                logConsole.append("> Could not save game record: " + e.getMessage() + "\n");
            }
        }
 
        WinScreen winScreen = ((MainFrame) getNavigator()).getWinScreen();
        winScreen.refresh(engine);
        getNavigator().navigateTo("WinScreen");
    }
}

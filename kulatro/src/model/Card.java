package model;

import java.io.Serializable;

/**
 * This class is an abstract superclass for all card types in the game.
 * It defines the core attributes and behaviors of a card and implements 
 * {@link Serializable} to allow game states to be saved and loaded.
 * @author Muhammed Cagan Goktas
 */
public abstract class Card implements Serializable {
	/**
     * Serial version UID for verifying the serialization compatibility.
     */
	private static final long serialVersionUID = 100L;
	

	private final String type;
	private final DeckType style;
	private final String imagePath;
	
	/**
     * Indicates whether the card is locked. 
     * Primarily used for specific game mechanics like Noble Gas effects.
     */
	private boolean isLocked = false;
	private double scoreMultiplier = 1.0;

	/**
     * Sets the locked status of the card.
     * @param locked true to lock the card, false to unlock it
     */
	public void setLocked(boolean locked) {this.isLocked = locked;}
	
	/**
     * Checks if the card is currently locked.
     * @return true if the card is locked, false otherwise
     */
	public boolean isLocked() {return isLocked;}

	/**
     * Sets the score multiplier for this card.
     * @param mult the new double multiplier value
     */
	public void setScoreMultiplier(double mult) {this.scoreMultiplier = mult;}
	
	/**
     * Gets the current score multiplier of this card.
     * @return the double score multiplier
     */
	public double getScoreMultiplier() {return scoreMultiplier;}
	
	/**
     * Constructs a new Card with the specified type, style, and image path.
     * @param type the element category or type of the card
     * @param style the theme/style of the deck
     * @param imagePath the file path to the card's image
     */
	public Card(String type, DeckType style, String imagePath) {
	    this.type = type;
	    this.style = style;
	    this.imagePath = imagePath;
	}
	
	/**
     * Returns a string representation of the card, which is its name.
     * @return the name of the card as a String
     */
	@Override
	public String toString() {return getName();}
	
	/**
     * Abstract method to get the specific name of the card.
     * Must be implemented by subclasses.
     * @return the specific name of the card
     */
	public abstract String getName();
	
	/**
     * Gets the type or category of the card.
     * @return the card type as a String
     */
	public String getType() {return type;}
	
	/**
     * Gets the deck style of the card.
     * @return the {@link DeckType} of this card
     */
	public DeckType getStyle() {return style;}
	
	/**
     * Gets the image file path of the card.
     * @return the image path as a String
     */
	public String getImagePath() {return imagePath;}
}
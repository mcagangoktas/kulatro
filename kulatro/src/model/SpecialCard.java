package model;

import java.util.List;

/**
 * Represents an abstract superclass for all special cards in the game.
 * Special cards provide unique actions or score modifications that alter standard gameplay.
 * @author Muhammed Cagan Goktas
 */
public abstract class SpecialCard extends Card {
	public boolean used = false;
	
	/**
	 * Constructs a new SpecialCard with the specified name and deck theme,
	 * automatically resolving the file path for its unique image resource.
	 * @param name the unique title of the special card
	 * @param style the theme style of the deck
	 */
	public SpecialCard(String name, DeckType style) {
	 	super(name, style, "resources/images/special_cards/" + style.name().toLowerCase() + "_" + nameConverter(name).toLowerCase() + ".png");
 	}

	/**
	 * Returns the element category or type as the definitive name of this special card.
	 * @return the card name as a String
	 */
 	@Override
 	public String getName() {return getType();}
 	
 	public boolean isUsed() {return used;}
	public void setUsed(boolean used) {this.used = used;}
	

	/**
	 * Modifies the final calculated hand score based on the specific rules of the special card.
	 * Default implementation returns the score unchanged.
	 * @param currentScore the baseline calculated score before modifications
	 * @param hand the current list of cards in the player's hand
	 * @return the updated final integer score
	 */
 	public int applyScoreEffect(int currentScore, List<Card> hand) {
	 	return currentScore;
 	}
 	
 	/**
	 * Normalizes special card names by stripping single quotes and injecting underscores 
	 * instead of spaces to match asset file structures.
	 * @param name the raw string name of the special card
	 * @return the formatted clean string file name
	 */
 	private static String nameConverter(String name) {
 		String cleaned = name.replace("'", "").trim();
 		String[] arr = cleaned.split(" ");
 		String res = "";
 		if (cleaned.contains(" ")) {
 			for (int i=0; i<arr.length-1; i++) {
 				res += arr[i] + "_";
 			}
 			res += arr[arr.length-1];
 			return res;
 		}
 		else return cleaned;
 	}

 	/**
	 * Executes the unique strategic capability or effect of the special card 
	 * targeting the active player or the deck. Must be implemented by concrete subclasses.
	 * @param player the current active {@link Player} triggering the card
	 * @param deck the active {@link Deck} instance used in the game session
	 */
 	public void performAction(Player player, Deck deck) {this.used=true;};
}
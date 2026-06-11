package model;

/**
 * Represents a standard numbered card in the game, holding a specific numeric value
 * alongside its elemental type and visual style.
 * @author Muhammed Cagan Goktas
 */
public class NumberCard extends Card {
	private final int value;

	/**
	 * Constructs a new NumberCard with the specified type, style, and numeric value,
	 * automatically generating the correct image resource path.
	 * @param type the element category of the card
	 * @param style the theme style of the deck
	 * @param value the numeric value of the card (e.g., 1-9)
	 */
	public NumberCard(String type, DeckType style, int value) {
		super(type, style, "resources/images/number_cards/" + style.name().toLowerCase() + "_" + nameConverter(type).toLowerCase() + "_" + value + ".png");
		this.value = value;
	}

	/**
	 * Converts element names containing spaces into underscore-separated strings 
	 * to match image file naming conventions (e.g., "Carbon Dioxide" to "Carbon_Dioxide").
	 * @param name the original name of the element
	 * @return the formatted name with spaces replaced by underscores
	 */
	private static String nameConverter(String name) {
 		String[] arr = name.split(" ");
 		String res = "";
 		if (name.contains(" ")) {
 			for (int i=0; i<arr.length-1; i++) {
 				res += arr[i] + "_";
 			}
 			res += arr[arr.length-1];
 			return res;
 		}
 		else return name;
 	}
	
	/**
	 * Gets the numeric value of the card.
	 * @return the integer value of this card
	 */
	public int getValue() {return value;}

	/**
	 * Returns the full descriptive name of the card by combining its type and value.
	 * @return a String representing the card's name (e.g., "Hydrogen 5")
	 */
	@Override
	public String getName() {
		return getType() + " " + value;
	}
}
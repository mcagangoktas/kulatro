package model;

/**
 * Defines the available card deck themes (styles) in the game.
 * Each deck type represents a distinct scientific domain, containing
 * its own unique set of element/category types used for game mechanics.
 * @author Muhammed Cagan Goktas
 */
public enum DeckType {
	ALCHEMY("Alchemy Set", new String[]{"Fire", "Water", "Earth", "Air"}),
	ELEMENT("Element Set", new String[]{"Hydrogen", "Oxygen", "Nitrogen", "Carbon Dioxide"}),
	QUANTUM("Quantum Set", new String[]{"Quark", "Boson", "Gluon", "Photon"});

	private final String displayName;
	private final String[] types;

	/**
     * Constructs a DeckType enum constant with a corresponding display name and its internal types.
     * @param name the string title of the deck
     * @param types the specific elements/categories belonging to this theme
     */
	DeckType(String name, String[] types) {
		this.displayName = name;
		this.types = types;
	}

	/**
     * Retrieves the specific element/category types included in this deck style.
     * @return an array of Strings representing the sub-types
     */
	public String[] getTypes() {return types;}
	
	/**
    * Retrieves the display name of the deck style.
    * @return the string display name
    */
	public String getDisplayName() {return displayName;}
}
package service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

import exception.GameDataException;
import model.*;
import model.special.*;

/**
 * Handles all persistent data operations including user profiles, game session 
 * serialization, event logging, and configuration loading.
 * @author Muhammed Cagan Goktas
 */
public class LogHandler {
    private static final String DATA_DIR    = "data/";
    private static final String SAVE_DIR    = "data/saves/";
    private static final String USER_FILE   = "data/users.txt";
    private static final String CONFIG_FILE = "config/special_cards.txt";
    private static final String LOG_FILE    = "data/log.txt";

    static {
        new File(DATA_DIR).mkdirs();
        new File(SAVE_DIR).mkdirs();
        new File("config").mkdirs();
    }

    /**
     * Loads special card availability rules and mappings from the configuration file.
     * @return a {@link Map} where keys are deck themes and values are lists of allowed card names
     * @throws GameDataException if the config file is missing or unreadable
     */
    public static Map<String, List<String>> loadCardDefinitions() throws GameDataException {
        Map<String, List<String>> config = new HashMap<>();
        File file = new File(CONFIG_FILE);
        if (!file.exists()) throw new GameDataException("Config file not found: " + CONFIG_FILE, null);
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split(":");
                if (parts.length >= 2) {
                    String setName  = parts[0].trim();
                    String cardName = parts[1].trim();
                    config.computeIfAbsent(setName, _ -> new ArrayList<>()).add(cardName);
                }
            }
        } catch (FileNotFoundException e) {
            throw new GameDataException("Config file could not be read!", e);
        }
        return config;
    }

    /**
     * Serializes all user profile data, including credentials and complete game history, to a text file.
     * @param users the {@link Map} of users to be persisted
     * @throws GameDataException if a disk I/O error occurs during saving
     */
    public static void saveUsers(Map<String, User> users) throws GameDataException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(USER_FILE))) {
            for (User user : users.values()) {
                pw.println("[user]");
                pw.println("username=" + user.getUsername());
                pw.println("password=" + user.getPassword());
                for (User.GameRecord r : user.getGameHistory()) {
                    pw.println("game=" + r.sessionName + "," + r.finalScore + "," + r.isWon + "," + r.date);
                }
                pw.println();
            }
        } catch (IOException e) {
            throw new GameDataException("Users could not be saved!", e);
        }
    }

    /**
     * Deserializes user data from the storage file into the application's runtime memory.
     * @return a {@link Map} of usernames to {@link User} objects
     * @throws GameDataException if the file exists but cannot be parsed correctly
     */
    public static Map<String, User> loadUsers() throws GameDataException {
        Map<String, User> users = new HashMap<>();
        File file = new File(USER_FILE);
        if (!file.exists()) return users;
        try (Scanner sc = new Scanner(file)) {
            User current = null;
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.equals("[user]")) {
                    current = null;
                } else if (line.startsWith("username=")) {
                    String username = val(line);
                    String passLine = sc.hasNextLine() ? sc.nextLine().trim() : "";
                    String password = passLine.startsWith("password=") ? val(passLine) : "";
                    current = new User(username, password);
                    users.put(username, current);
                } else if (line.startsWith("game=") && current != null) {
                    String[] parts = line.substring("game=".length()).split(",", 4);
                    if (parts.length >= 3) {
                        User.GameRecord record = new User.GameRecord(
                            parts[0], Integer.parseInt(parts[1]), Boolean.parseBoolean(parts[2]));
                        if (parts.length == 4) record.date = parts[3];
                        current.addGameRecord(record);
                    }
                }
            }
        } catch (IOException e) {
            throw new GameDataException("Users could not be loaded!", e);
        }
        return users;
    }
    
    /**
     * Saves the current state of a game session (deck, hand, score, round) into a unique session file.
     * @param sessionName the unique name of the session file
     * @param engineObj the active {@link GameEngine} instance to be saved
     * @throws GameDataException if the object is invalid or saving fails
     */
    public static void saveGameSession(String sessionName, Object engineObj) throws GameDataException {
        if (!(engineObj instanceof GameEngine))
            throw new GameDataException("Invalid engine object", null);
        GameEngine engine = (GameEngine) engineObj;
        File file = new File(SAVE_DIR + sessionName + ".txt");

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            Player player = engine.getPlayer();
            ScoreManager sm = engine.getScoreManager();
            RoundManager rm = engine.getRoundManager();
            Deck deck = engine.getDeck();

            pw.println("[session]");
            pw.println("sessionName=" + engine.getSessionName());
            pw.println("username=" + player.getUsername());
            pw.println("deck=" + engine.getDeck().getStyle().name());
            pw.println("difficulty=" + rm.getDifficulty().name());
            pw.println("round=" + rm.getCurrentRound());
            pw.println("totalScore=" + sm.getTotalScore());

            List<Integer> scores = sm.getRoundScores();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < scores.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(scores.get(i));
            }
            pw.println("roundScores=" + sb);
            pw.println("usedDiscards=" + player.getUsedDiscards());
            pw.println("roundDiscards=" + player.getRoundDiscards());

            SpecialCard sc = engine.getSpecialCard();
            pw.println("specialCard="  + (sc != null ? sc.getName() : "none"));

            pw.println("[hand]");
            for (Card c : player.getHand()) {
                if (c instanceof NumberCard) {
                    NumberCard nc = (NumberCard) c;
                    pw.println(nc.getType() + "," + nc.getValue() + "," + nc.isLocked() + "," + nc.getScoreMultiplier());
                }
            }
            
            pw.println("[deck]");
            pw.println("remaining=" + deck.getRemainingCount());
            
            pw.println("[discard]");
            for (Card c : deck.getDiscardPile()) {
                if (c instanceof NumberCard) {
                    NumberCard nc = (NumberCard) c;
                    pw.println(nc.getType() + "," + nc.getValue() + "," + nc.isLocked() + "," + nc.getScoreMultiplier());
                }
            }
        } catch (IOException e) {
            throw new GameDataException("Game session could not be saved: " + sessionName, e);
        }
    }

    /**
     * Reconstructs a full {@link GameEngine} instance from a saved session file.
     * @param sessionName the name of the file to load
     * @return a fully restored {@link GameEngine} object
     * @throws GameDataException if the file is missing or corrupted
     */
    public static Object loadGameSession(String sessionName) throws GameDataException {
        File file = new File(SAVE_DIR + sessionName + ".txt");
        if (!file.exists()) {
            throw new GameDataException("Save file not found: " + sessionName, null);
        }

        try (Scanner sc = new Scanner(file)) {
            String username = "", deck = "ALCHEMY", difficulty = "EASY", sessionNameVal = sessionName;
            int round = 1, totalScore = 0, usedDiscards = 0, roundDiscards = 0, remaining = -1;
            List<Integer> roundScores = new ArrayList<>();
            String specialCardName = "none";
            List<Card> hand = new ArrayList<>();
            List<Card> discardPile = new ArrayList<>();
            String section = "";

            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                if (line.startsWith("[")) { section = line; continue; }

                if (section.equals("[session]")) {
                    if (line.startsWith("sessionName=")) sessionNameVal = val(line);
                    else if (line.startsWith("username=")) username = val(line);
                    else if (line.startsWith("deck=")) deck = val(line);
                    else if (line.startsWith("difficulty=")) difficulty = val(line);
                    else if (line.startsWith("round=")) round = Integer.parseInt(val(line));
                    else if (line.startsWith("totalScore=")) totalScore = Integer.parseInt(val(line));
                    else if (line.startsWith("roundScores=")) {
                        String rs = val(line);
                        if (!rs.isEmpty())
                            for (String s : rs.split(",")) roundScores.add(Integer.parseInt(s.trim()));
                    }
                    else if (line.startsWith("usedDiscards=")) usedDiscards = Integer.parseInt(val(line));
                    else if (line.startsWith("roundDiscards=")) roundDiscards = Integer.parseInt(val(line));
                    else if (line.startsWith("specialCard=")) specialCardName = val(line);

                } else if (section.equals("[deck]")) {
                    if (line.startsWith("remaining=")) remaining = Integer.parseInt(val(line));
                } else if (section.equals("[hand]")) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        DeckType dt = DeckType.valueOf(deck);
                        NumberCard nc = new NumberCard(parts[0].trim(), dt, Integer.parseInt(parts[1].trim()));
                        if (parts.length >= 3) nc.setLocked(Boolean.parseBoolean(parts[2].trim()));
                        if (parts.length >= 4) nc.setScoreMultiplier(Double.parseDouble(parts[3].trim()));
                        hand.add(nc);
                    }
                } else if (section.equals("[discard]")) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        DeckType dt = DeckType.valueOf(deck);
                        NumberCard nc = new NumberCard(parts[0].trim(), dt, Integer.parseInt(parts[1].trim()));
                        if (parts.length >= 3) nc.setLocked(Boolean.parseBoolean(parts[2].trim()));
                        if (parts.length >= 4) nc.setScoreMultiplier(Double.parseDouble(parts[3].trim()));
                        discardPile.add(nc);
                    }
                }
            }

            Player player = new Player(username);
            DeckType deckType = DeckType.valueOf(deck);
            Difficulty diff   = Difficulty.valueOf(difficulty);
            GameEngine engine = new GameEngine(player, deckType, diff);

            while (engine.getRoundManager().getCurrentRound() < round)
                engine.getRoundManager().nextRound();

            engine.getScoreManager().restoreState(totalScore, roundScores);
            engine.getPlayer().clearHand();
            for (Card c : hand) engine.getPlayer().addCardToHand(c);
            engine.getPlayer().restoreDiscards(usedDiscards, roundDiscards);
            
            engine.getDeck().restoreDiscardPile(discardPile);
            
            if (remaining >= 0) {
                engine.getDeck().restoreRemainingCount(remaining);
            }
            
            if (!specialCardName.equals("none"))
                engine.setSpecialCard(createSpecialCard(specialCardName, deckType));

            return engine;

        } catch (IOException e) {
            throw new GameDataException("Game session could not be loaded: " + sessionName, e);
        }
    }

    /**
     * Helper factory method to instantiate a specific {@link SpecialCard} by its name.
     * @param name the internal name of the special card
     * @param style the deck theme the card belongs to
     * @return the concrete instance of the special card, or null if unknown
     */
    private static SpecialCard createSpecialCard(String name, DeckType style) {
        switch (name) {
            case "Philosopher's Stone":  return new PhilosophersStone();
            case "Transmutation":        return new Transmutation();
            case "Elemental Fusion":     return new ElementalFusion();
            case "Catalyst":             return new Catalyst();
            case "Periodic Boost":       return new PeriodicBoost();
            case "Noble Gas":            return new NobleGas();
            case "Isotope Decay":        return new IsotopeDecay();
            case "Electron Bond":        return new ElectronBond();
            case "Quantum Entanglement": return new QuantumEntanglement();
            case "Superposition":        return new Superposition();
            case "Gluon Bind":           return new GluonBind();
            case "Photon Burst":         return new PhotonBurst();
            default: return null;
        }
    }

    /**
     * Scans the save directory and attempts to load all available game sessions.
     * @return a {@link List} of restored {@link GameEngine} objects
     */
    public static List<GameEngine> getAllSavedGames() {
        List<GameEngine> sessions = new ArrayList<>();
        File folder = new File(SAVE_DIR);
        File[] files = folder.listFiles((_, name) -> name.endsWith(".txt"));
        if (files != null) {
            for (File f : files) {
                String name = f.getName().replaceAll("\\.(txt|sav)$", "");
                try {
                    sessions.add((GameEngine) loadGameSession(name));
                } catch (Exception e) {
                    System.err.println("Could not load: " + f.getName());
                }
            }
        }
        return sessions;
    }

    /**
     * Appends a timestamped system event message to the global log file.
     * @param username the user associated with the event
     * @param message the description of the action or event
     */
    public static void logEvent(String username, String message) {
        try (Formatter formatter = new Formatter(new FileOutputStream(LOG_FILE, true))) {
            LocalDateTime now = LocalDateTime.now();
            formatter.format("[%tF %tT] User: %s | Action: %s%n", now, now, username, message);
        } catch (IOException e) {
            System.err.println("Log write error: " + e.getMessage());
        }
    }

    /**
     * Utility method to extract the value from a "key=value" string format.
     */
    private static String val(String line) {
        int idx = line.indexOf('=');
        return idx >= 0 ? line.substring(idx + 1) : "";
    }
}
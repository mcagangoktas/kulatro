# 🃏 Kulatro

A science-themed card game built with **Java Swing** as a semester project. Players build scoring hands from themed decks — Alchemy, Element, or Quantum — and race to beat escalating score thresholds across 4 rounds.

---

## 🎮 Gameplay Overview

Each round the player draws cards from a themed deck, builds a hand, and tries to reach the target score. Scoring rewards matched sets (pairs, runs, full sets of a type). A randomly assigned **Special Card** can be activated once per game to dramatically swing the outcome.

Fail to hit the threshold in any round → game over.

---

## 🧪 Deck Themes

Pick one of three science-themed decks at the start:

| Deck | Card Types |
|------|-----------|
| 🔥 **Alchemy** | Fire · Water · Earth · Air |
| ⚗️ **Element** | Hydrogen · Oxygen · Nitrogen · Carbon Dioxide |
| ⚛️ **Quantum** | Quark · Boson · Gluon · Photon |

Each deck also comes with **4 unique Special Cards** matching its theme.

---

## ✨ Special Cards

**🔥 Alchemy**
| Card | Effect |
|------|--------|
| Philosopher's Stone | Doubles the score of the current hand *(one-time)* |
| Transmutation | Swap one card in hand with a random card from the deck |
| Elemental Fusion | If you have ≥2 cards of the same type, they count as 4 for scoring |
| Catalyst | Increases the multiplier of any "pair" scoring by +1 |

**⚗️ Element**
| Card | Effect |
|------|--------|
| Periodic Boost | Adds +2 to the numeric value of all cards in hand |
| Noble Gas | Locks one card (can't be discarded); its value counts twice |
| Isotope Decay | Discard entire hand, draw 4 new cards — no discard penalty *(one-time)* |
| Electron Bond | Creates a scoring pair between two different card types |

**⚛️ Quantum**
| Card | Effect |
|------|--------|
| Quantum Entanglement | Triples the score of one selected card |
| Superposition | Plays two scoring patterns simultaneously; takes the better result |
| Gluon Bind | Merges two cards into one (combined value, max 9); draws a replacement |
| Photon Burst | Reveals next 3 deck cards; optionally swap one into your hand |

---

## 🎯 Difficulty & Score Thresholds

| Difficulty | Round 1 | Round 2 | Round 3 | Round 4 |
|------------|:-------:|:-------:|:-------:|:-------:|
| Easy | 30 | 40 | 50 | 60 |
| Medium | 60 | 80 | 100 | 120 |
| Hard | 90 | 120 | 150 | 180 |

---

## 🏗️ Project Structure

```
kulatro/
├── src/
│   ├── model/                   # Domain objects
│   │   ├── Card.java            # Abstract base for all cards
│   │   ├── NumberCard.java      # Numbered cards (1–9)
│   │   ├── SpecialCard.java     # Abstract base for special cards
│   │   ├── Deck.java            # Deck: draw, discard, reshuffle
│   │   ├── DeckType.java        # Enum: ALCHEMY, ELEMENT, QUANTUM
│   │   ├── Difficulty.java      # Enum: EASY, MEDIUM, HARD
│   │   ├── Player.java          # Player hand and in-session state
│   │   ├── User.java            # Persistent profile + game history
│   │   └── special/             # One class per special card
│   │       ├── Catalyst.java
│   │       ├── ElectronBond.java
│   │       ├── ElementalFusion.java
│   │       ├── GluonBind.java
│   │       ├── IsotopeDecay.java
│   │       ├── NobleGas.java
│   │       ├── PeriodicBoost.java
│   │       ├── PhilosophersStone.java
│   │       ├── PhotonBurst.java
│   │       ├── QuantumEntanglement.java
│   │       ├── Superposition.java
│   │       └── Transmutation.java
│   ├── service/                 # Business logic (no UI dependencies)
│   │   ├── GameEngine.java      # Core game loop and turn management
│   │   ├── ScoreManager.java    # Scoring rules, multipliers, thresholds
│   │   ├── RoundManager.java    # Round progression and win/loss checks
│   │   ├── AuthService.java     # File-based user login & registration
│   │   ├── LogHandler.java      # Game event logging
│   │   └── SoundManager.java    # Background music and SFX
│   ├── view/                    # Java Swing UI
│   │   ├── MainFrame.java       # Root JFrame — hosts all panels
│   │   ├── ViewNavigator.java   # Interface for panel switching
│   │   ├── BasePanel.java       # Shared background + font base class
│   │   ├── StartPanel.java      # Title / launch screen
│   │   ├── LoginPanel.java      # Login and registration
│   │   ├── MainMenuPanel.java   # Post-login main menu
│   │   ├── SetupPanel.java      # Deck + difficulty selection
│   │   ├── ActualGamePanel.java # Main in-game screen
│   │   ├── WinScreen.java       # Round win / game over screen
│   │   ├── LoadPanel.java       # Save game loader
│   │   ├── LeaderboardPanel.java# High score leaderboard
│   │   └── SettingsPanel.java   # Sound and display settings
│   └── exception/               # Custom typed exceptions
│       ├── CardGameException.java
│       ├── DeckEmptyException.java
│       ├── GameDataException.java
│       └── InvalidHandException.java
├── resources/
│   ├── images/
│   │   ├── number_cards/        # 108 card face images (12 types × 9 values)
│   │   ├── special_cards/       # 12 special card images
│   │   └── *.png                # Per-screen background images
│   └── sounds/
│       ├── background_music.wav
│       └── click.wav
├── config/
│   └── special_cards.txt        # Special card definitions
└── data/
    └── saves/                   # Auto-created at runtime for save slots
```

---

## 🚀 Getting Started

### Prerequisites
- Java 11 or higher
- Eclipse IDE *(recommended — `.classpath` / `.project` files included)*

### Run in Eclipse
1. Clone the repo:
   ```bash
   git clone https://github.com/YOUR_USERNAME/kulatro.git
   ```
2. **File → Import → Existing Projects into Workspace**
3. Select the `kulatro/` folder
4. Right-click project → **Run As → Java Application** → select `view.MainFrame`

### Run from the command line
```bash
cd kulatro

# Compile
javac -d bin -sourcepath src $(find src -name "*.java")

# Run  (working directory must be kulatro/ for resources to resolve)
java -cp bin view.MainFrame
```

---

## 🏛️ Architecture

The project follows a **3-layer MVC** structure:

| Layer | Package | Responsibility |
|-------|---------|----------------|
| Model | `model/` | Pure data classes + game rules; fully `Serializable` for save/load |
| Service | `service/` | Stateful logic (game loop, scoring, auth); zero UI dependencies |
| View | `view/` | One `JPanel` subclass per screen; navigated via `ViewNavigator` |

Game state is persisted via Java object serialization. User accounts and scores are stored in plain text files managed by `AuthService`.

---

## 📄 Report

Full design and implementation report: [`mgoktas24Report.docx`](mgoktas24Report.docx)

---

## 👤 Author

**Muhammed Çağan Göktaş**

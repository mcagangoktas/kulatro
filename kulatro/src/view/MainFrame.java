/************** Pledge of Honor ******************************************
I hereby certify that I have completed this programming project on my own without
any help from anyone else. The effort in the project thus belongs completely to me.
I did not search for a solution, or I did not consult any program written by others
or did not copy any program from other sources. I read and followed the guidelines
provided in the project description.
READ AND SIGN BY WRITING YOUR NAME SURNAME AND STUDENT ID
SIGNATURE: <Muhammed Cagan Goktas, 90082>
*************************************************************************/

package view;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import service.AuthService;
import service.GameEngine;
import service.SoundManager;

/**
 * The primary application window that serves as the central orchestrator for the UI.
 * It manages screen transitions using {@link CardLayout} and maintains references 
 * to global services and active game sessions.
 * @author Muhammed Cagan Goktas
 */
public class MainFrame extends JFrame implements ViewNavigator {
	private static final long serialVersionUID = 1001L;
	private JPanel cardPanel;
	private CardLayout cardLayout;
	private GameEngine currentGameEngine;
	private ActualGamePanel actualGamePanel;
	private LoadPanel loadPanel;
	private LeaderboardPanel leaderboardPanel;
	private AuthService authService;
	private WinScreen winScreen;
	
	/**
	 * Initializes the main frame, sets up global services, registers all 
	 * navigable panels, and displays the initial login screen.
	 */
	public MainFrame() {
		cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
		
        this.authService = new AuthService();
        
        cardPanel.add(new LoginPanel(this, authService), "Login");
        leaderboardPanel = new LeaderboardPanel(this, authService);
        cardPanel.add(leaderboardPanel, "Leaderboard");
		cardPanel.add(new MainMenuPanel(this), "Menu");
		cardPanel.add(new StartPanel(this), "StartPanel");
		cardPanel.add(new SettingsPanel(this), "Settings");
		cardPanel.add(new SetupPanel(this), "Setup");
		actualGamePanel = new ActualGamePanel(this);
		cardPanel.add(actualGamePanel, "TheGame");
		loadPanel = new LoadPanel(this);
		cardPanel.add(loadPanel, "Load");
		winScreen = new WinScreen(this);
        cardPanel.add(winScreen, "WinScreen");
 
		
		add(cardPanel);
		
		setTitle("KULATRO");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        
        SoundManager.getInstance().setSoundOn(false);

        cardLayout.show(cardPanel, "Login");
	}
	
	/**
	 * Switches the visible panel based on the provided target name. 
	 * Automatically refreshes dynamic panels like Load and Leaderboard before showing them.
	 * @param target the unique string identifier for the target panel
	 */
	@Override
    public void navigateTo(String target) {
        if (target.equals("Load")) loadPanel.refreshSaveList();
        if (target.equals("Leaderboard")) leaderboardPanel.refresh();
        cardLayout.show(cardPanel, target);
        cardPanel.revalidate();
        cardPanel.repaint();
    }

	/**
	 * Returns the global authentication service instance.
	 * @return the active {@link AuthService}
	 */
	public AuthService getAuthService() {return authService;}
	
	/**
	 * Returns the post-game summary screen reference.
	 * @return the {@link WinScreen} instance
	 */
	public WinScreen getWinScreen() {return winScreen;}
	
	/**
	 * Updates the shared game engine reference for the current session.
	 * @param engine the active {@link GameEngine}
	 */
	@Override
	public void setGameEngine(GameEngine engine) {this.currentGameEngine = engine;}
	
	/**
	 * Retrieves the current game engine managing the active session logic.
	 * @return the {@link GameEngine} instance
	 */
	@Override
    public GameEngine getGameEngine() {return this.currentGameEngine;}
	
	/**
	 * Returns the specialized gameplay panel.
	 * @return the {@link ActualGamePanel} as a {@link BasePanel}
	 */
	@Override
	public BasePanel getActualGamePanel() {return actualGamePanel;}
	
	/**
	 * Main entry point for the application. Launches the GUI on the Event Dispatch Thread.
	 * @param args command-line arguments (not used)
	 */
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}

package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import model.User;
import model.User.GameRecord;
import service.AuthService;
import service.SoundManager;

/**
 * Displays the game leaderboard by pulling user history from the authentication 
 * service and presenting it in a formatted, scrollable table sorted by high scores.
 * @author Muhammed Cagan Goktas
 */
public class LeaderboardPanel extends BasePanel {
    private static final long serialVersionUID = 1005L;
    private AuthService authService;
    private JScrollPane scrollPane;

    /**
     * Constructs the leaderboard panel and hooks it up to the shared authentication service.
     * @param navigator the {@link ViewNavigator} instance for screen transitions
     * @param authService the centralized {@link AuthService} database reference
     */
    public LeaderboardPanel(ViewNavigator navigator, AuthService authService) {
        super(navigator, "main_menu_bg");
        this.authService = authService;
    }

    /**
     * Builds the main structural components of the panel, including the title header, 
     * table container, and a return menu button.
     */
    @Override
    protected void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));

        JLabel title = new JLabel("LEADERBOARD");
        title.setFont(new Font("Lucida Calligraphy", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);
        
        scrollPane = new JScrollPane();
        scrollPane.setBackground(new Color(20, 20, 20));
        scrollPane.getViewport().setBackground(new Color(20, 20, 20));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        add(scrollPane, BorderLayout.CENTER);

        JButton backBtn = createStyledButton("BACK TO MENU", 250);
        backBtn.addActionListener(_ -> {
        	SoundManager.getInstance().playClickSound();
        	navigator.navigateTo("Menu");
        });
        JPanel south = new JPanel();
        south.setOpaque(false);
        south.add(backBtn);
        add(south, BorderLayout.SOUTH);
    }

    /**
     * Regenerates the table data frame from the database and binds a styled, 
     * fresh {@link JTable} view component inside the scrollable area.
     */
    public void refresh() {
        String[] columns = {"USERNAME", "LAST SESSION", "SCORE", "STATUS", "RESULT"};
        Object[][] data = buildTableData();
 
        JTable table = new JTable(data, columns);
        table.setBackground(new Color(20, 20, 20));
        table.setForeground(Color.WHITE);
        table.setFont(new Font("Consolas", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.getTableHeader().setBackground(new Color(40, 40, 40));
        table.getTableHeader().setForeground(Color.LIGHT_GRAY);
        table.getTableHeader().setFont(new Font("Consolas", Font.BOLD, 13));
        table.setGridColor(Color.DARK_GRAY);
        table.setSelectionBackground(new Color(60, 60, 60));
 
        scrollPane.setViewportView(table);
        scrollPane.revalidate();
        scrollPane.repaint();
    }
    
    /**
     * Extracts statistical game milestones across all registered accounts and 
     * sorts rows numerically based on the highest achieved score.
     * @return a multi-dimensional Object array representing rows and columns for the table model
     */
    private Object[][] buildTableData() {
        Map<String, User> users = authService.getUserDatabase();
        List<Object[]> rows = new ArrayList<>();

        for (User user : users.values()) {
            List<GameRecord> history = user.getGameHistory();

            if (history.isEmpty()) {
                rows.add(new Object[]{user.getUsername(), "-", "-", "No games", "-"});
            } else {
                GameRecord last = history.get(history.size() - 1);
                String status = "Finished";
                String result = last.isWon ? "WON" : "LOST";
                rows.add(new Object[]{user.getUsername(), last.sessionName, last.finalScore, status, result});
            }
        }

        rows.sort((a, b) -> {
            if (a[2].equals("-")) return 1;
            if (b[2].equals("-")) return -1;
            return Integer.compare((int) b[2], (int) a[2]);
        });

        return rows.toArray(new Object[0][]);
    }
}
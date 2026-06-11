package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import service.GameEngine;
import service.ScoreManager;

/**
 * Displays the final game results, providing a detailed statistical breakdown 
 * of each round compared against its specific difficulty threshold.
 * @author Muhammed Cagan Goktas
 */
public class WinScreen extends BasePanel {
    private static final long serialVersionUID = 1011L;

    /**
     * Constructs the victory/defeat screen using the standard menu background.
     * @param navigator the {@link ViewNavigator} used for returning to menus
     */
    public WinScreen(ViewNavigator navigator) {
        super(navigator, "main_menu_bg");
    }

    /**
     * Sets the initial layout manager for the panel.
     */
    @Override
    protected void setupUI() {
        setLayout(new BorderLayout(10, 10));
    }
    
    /**
     * Dynamically rebuilds the screen based on the final state of the {@link GameEngine}.
     * Calculates averages, compares scores to thresholds, and renders a stylized performance table.
     * @param engine the {@link GameEngine} containing the completed session data
     */
    public void refresh(GameEngine engine) {
        removeAll();
        setLayout(new BorderLayout(10, 20));
        setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        ScoreManager sm = engine.getScoreManager();
        List<Integer> roundScores = sm.getRoundScores();
        int[] thresholds = sm.getCurrentThresholds();
        int totalScore = sm.getTotalScore();
        int thresholdSum = 0;
        for (int t : thresholds) thresholdSum += t;
        int thresholdAvg = thresholdSum / 4;
        double playerAvg = totalScore / 4.0;
        boolean won = sm.checkGameWin();

        String titleText = won ? "MISSION SUCCESS" : "MISSION FAILURE";
        Color titleColor = won ? new Color(0, 220, 100) : new Color(220, 60, 60);
        JLabel title = new JLabel(titleText, SwingConstants.CENTER);
        title.setFont(new Font("Lucida Calligraphy", Font.BOLD, 48));
        title.setForeground(titleColor);
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        String[] cols = {"", "YOUR SCORE", "TARGET", "RESULT"};
        Object[][] data = new Object[4][4];
        int roundsWon = 0;
        for (int i = 0; i < 4; i++) {
            int score = i < roundScores.size() ? roundScores.get(i) : 0;
            int target = thresholds[i];
            boolean roundWon = score >= target;
            if (roundWon) roundsWon++;
            data[i][0] = "Round " + (i + 1);
            data[i][1] = score;
            data[i][2] = target;
            data[i][3] = roundWon ? "WIN" : "LOSS";
        }

        JTable table = new JTable(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                String result = (String) getValueAt(row, 3);
                c.setForeground(result.equals("WIN") ? new Color(0, 220, 100) : new Color(220, 60, 60));
                c.setBackground(new Color(20, 20, 20));
                return c;
            }
        };
        table.setBackground(new Color(20, 20, 20));
        table.setForeground(Color.WHITE);
        table.setFont(new Font("Consolas", Font.PLAIN, 15));
        table.setRowHeight(35);
        table.getTableHeader().setBackground(new Color(40, 40, 40));
        table.getTableHeader().setForeground(Color.LIGHT_GRAY);
        table.getTableHeader().setFont(new Font("Consolas", Font.BOLD, 14));
        table.setGridColor(new Color(50, 50, 50));
        table.setShowHorizontalLines(true);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setOpaque(false);
        scroll.getViewport().setBackground(new Color(20, 20, 20));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        scroll.setPreferredSize(new Dimension(500, 160));
        centerPanel.add(scroll);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        centerPanel.add(makeSummaryRow("Total Score", String.valueOf(totalScore), Color.WHITE));
        centerPanel.add(makeSummaryRow("Target Score Total", String.valueOf(thresholdSum), Color.LIGHT_GRAY));
        centerPanel.add(makeSummaryRow("Your Average", String.format("%.1f", playerAvg), Color.WHITE));
        centerPanel.add(makeSummaryRow("Target Average", String.valueOf(thresholdAvg), Color.LIGHT_GRAY));
        centerPanel.add(makeSummaryRow("Rounds Won", roundsWon + " / 4", Color.WHITE));
        centerPanel.add(makeSummaryRow("Overall Result", won ? "WIN" : "LOSS", titleColor));

        add(centerPanel, BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.setOpaque(false);

        JButton menuBtn = createStyledButton("MAIN MENU", 180);
        menuBtn.addActionListener(_ -> navigator.navigateTo("Menu"));

        JButton leaderBtn = createStyledButton("LEADERBOARD", 180);
        leaderBtn.addActionListener(_ -> navigator.navigateTo("Leaderboard"));

        south.add(menuBtn);
        south.add(leaderBtn);
        add(south, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    /**
     * Helper method to create a standardized horizontal summary row for statistics.
     * @param label the description of the metric
     * @param value the actual numeric or string value
     * @param valueColor the font color for the value component
     * @return a {@link JPanel} row containing the labeled metric
     */
    private JPanel makeSummaryRow(String label, String value, Color valueColor) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel lbl = new JLabel(label);
        lbl.setForeground(Color.GRAY);
        lbl.setFont(new Font("Consolas", Font.PLAIN, 14));

        JLabel val = new JLabel(value, SwingConstants.RIGHT);
        val.setForeground(valueColor);
        val.setFont(new Font("Consolas", Font.BOLD, 14));

        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.EAST);
        return row;
    }
}
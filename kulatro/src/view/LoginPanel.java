package view;

import java.awt.*;
import javax.swing.*;
import exception.GameDataException;
import service.AuthService;

/**
 * Provides the user authentication interface, allowing players to register 
 * new accounts or log into existing ones via the {@link AuthService}.
 * @author Muhammed Cagan Goktas
 */
public class LoginPanel extends BasePanel {
    private static final long serialVersionUID = 1009L;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private AuthService authService;

    /**
     * Initializes the login screen and sets up the authentication dependency.
     * @param navigator the {@link ViewNavigator} for switching to main menus
     * @param authService the service handling user credentials and persistence
     */
    public LoginPanel(ViewNavigator navigator, AuthService authService) {
        super(navigator, "main_menu_bg");
        this.authService = authService;
    }

    /**
     * Constructs the login layout, including stylized input fields, 
     * action buttons, and dynamic status labels for feedback.
     */
    @Override
    protected void setupUI() {
        add(Box.createVerticalGlue());

        JLabel title = new JLabel("KULATRO");
        title.setFont(new Font("Lucida Calligraphy", Font.BOLD, 60));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        add(Box.createRigidArea(new Dimension(0, 50)));

        usernameField = createInputField("USERNAME");
        passwordField = new JPasswordField();
        stylePasswordField(passwordField);

        add(new FieldLabel("USERNAME"));
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(usernameField);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(new FieldLabel("PASSWORD"));
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(passwordField);
        add(Box.createRigidArea(new Dimension(0, 30)));

        JLabel errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(errorLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));

        JButton loginBtn = createStyledButton("LOGIN", 250);
        loginBtn.addActionListener(_ -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword());
            if (authService.login(user, pass)) {
                navigator.navigateTo("Leaderboard");
            } else {
                errorLabel.setText("Invalid username or password.");
            }
        });
        add(loginBtn);

        add(Box.createRigidArea(new Dimension(0, 10)));

        JButton registerBtn = createStyledButton("REGISTER", 250);
        registerBtn.addActionListener(_ -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword());
            if (user.isEmpty() || pass.isEmpty()) {
                errorLabel.setText("Username and password cannot be empty.");
                return;
            }
            try {
                authService.register(user, pass);
                errorLabel.setForeground(new Color(0, 200, 0));
                errorLabel.setText("Registered! You can now log in.");
            } catch (GameDataException e) {
                errorLabel.setForeground(Color.RED);
                errorLabel.setText(e.getMessage());
            }
        });
        add(registerBtn);

        add(Box.createVerticalGlue());
    }

    /**
     * Creates a standardized text field for user input.
     * @param placeholder internal reference for the field's purpose
     * @return a customized {@link JTextField}
     */
    private JTextField createInputField(String placeholder) {
        JTextField field = new JTextField();
        styleField(field);
        return field;
    }

    /**
     * Applies uniform visual styles to text input components.
     * @param field the {@link JTextField} to be styled
     */
    private void styleField(JTextField field) {
        field.setBackground(new Color(30, 30, 30));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)));
        Dimension size = new Dimension(300, 35);
        field.setMaximumSize(size);
        field.setPreferredSize(size);
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    /**
     * Redirects password fields to the main styling logic to maintain consistency.
     * @param field the {@link JPasswordField} to be styled
     */
    private void stylePasswordField(JPasswordField field) {
        styleField(field);
    }

    /**
     * A helper label class designed for consistent input field tagging.
     */
    private static class FieldLabel extends JLabel {
        private static final long serialVersionUID = 100000L;

		FieldLabel(String text) {
            super(text);
            setForeground(Color.GRAY);
            setFont(new Font("Consolas", Font.PLAIN, 12));
            setAlignmentX(Component.CENTER_ALIGNMENT);
        }
    }
}
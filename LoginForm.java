import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;

public class LoginForm extends JFrame {

    private ModernUIComponents.ModernTextField usernameField;
    private ModernUIComponents.ModernPasswordField passwordField;
    private JLabel statusLabel;
    private JLabel strengthLabel;
    private boolean isLoginMode = true;
    private ModernUIComponents.ModernButton actionBtn;
    private ModernUIComponents.ModernButton switchModeBtn;
    private JLabel titleLabel;

    public LoginForm() {
        setTitle("Apex Finance - Secure Portal");
        setSize(420, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setResizable(false);

        // Core background panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(ModernUIComponents.COLOR_BG);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        add(mainPanel);

        // Header Panel (Branding / Logo)
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        headerPanel.setOpaque(false);
        titleLabel = new JLabel("APEX FINANCE", SwingConstants.CENTER);
        titleLabel.setFont(ModernUIComponents.FONT_TITLE);
        titleLabel.setForeground(ModernUIComponents.COLOR_TEXT_MAIN);
        
        JLabel subtitleLabel = new JLabel("Your Gate to Secure Digital Banking", SwingConstants.CENTER);
        subtitleLabel.setFont(ModernUIComponents.FONT_SMALL);
        subtitleLabel.setForeground(ModernUIComponents.COLOR_TEXT_MUTED);

        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form Panel (Centered Card)
        ModernUIComponents.RoundedPanel cardPanel = new ModernUIComponents.RoundedPanel(20, ModernUIComponents.COLOR_CARD);
        cardPanel.setLayout(null);
        cardPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel uLabel = new JLabel("Username");
        uLabel.setFont(ModernUIComponents.FONT_BOLD);
        uLabel.setForeground(ModernUIComponents.COLOR_TEXT_MUTED);
        uLabel.setBounds(20, 25, 200, 20);
        cardPanel.add(uLabel);

        usernameField = new ModernUIComponents.ModernTextField("Enter your username");
        usernameField.setBounds(20, 50, 300, 40);
        cardPanel.add(usernameField);

        JLabel pLabel = new JLabel("Password");
        pLabel.setFont(ModernUIComponents.FONT_BOLD);
        pLabel.setForeground(ModernUIComponents.COLOR_TEXT_MUTED);
        pLabel.setBounds(20, 105, 200, 20);
        cardPanel.add(pLabel);

        passwordField = new ModernUIComponents.ModernPasswordField("••••••••");
        passwordField.setBounds(20, 130, 300, 40);
        cardPanel.add(passwordField);

        // Password strength meter (only shown in registration mode)
        strengthLabel = new JLabel("");
        strengthLabel.setFont(ModernUIComponents.FONT_SMALL);
        strengthLabel.setForeground(ModernUIComponents.COLOR_DANGER);
        strengthLabel.setBounds(20, 175, 300, 20);
        cardPanel.add(strengthLabel);

        // Action Button
        actionBtn = new ModernUIComponents.ModernButton("Log In", ModernUIComponents.COLOR_PRIMARY, ModernUIComponents.COLOR_PRIMARY_HOVER);
        actionBtn.setBounds(20, 215, 300, 45);
        cardPanel.add(actionBtn);

        // Status Label (Feedback message)
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(ModernUIComponents.FONT_SMALL);
        statusLabel.setBounds(20, 270, 300, 20);
        cardPanel.add(statusLabel);

        mainPanel.add(cardPanel, BorderLayout.CENTER);

        // Footer / Mode Switcher Panel
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        footerPanel.setOpaque(false);

        JLabel promptLabel = new JLabel("Don't have an account?");
        promptLabel.setFont(ModernUIComponents.FONT_BODY);
        promptLabel.setForeground(ModernUIComponents.COLOR_TEXT_MUTED);

        switchModeBtn = new ModernUIComponents.ModernButton("Register Now", new Color(0, 0, 0, 0), new Color(255, 255, 255, 15));
        switchModeBtn.setFont(ModernUIComponents.FONT_BOLD);
        switchModeBtn.setForeground(ModernUIComponents.COLOR_ACCENT);
        switchModeBtn.setPreferredSize(new Dimension(110, 30));

        footerPanel.add(promptLabel);
        footerPanel.add(switchModeBtn);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        // Listeners
        actionBtn.addActionListener(e -> handleAction());
        switchModeBtn.addActionListener(e -> toggleMode(promptLabel));

        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { checkPasswordStrength(); }
            public void removeUpdate(DocumentEvent e) { checkPasswordStrength(); }
            public void changedUpdate(DocumentEvent e) { checkPasswordStrength(); }
        });

        setVisible(true);
    }

    private void checkPasswordStrength() {
        if (isLoginMode) {
            strengthLabel.setText("");
            return;
        }
        String password = new String(passwordField.getPassword());
        if (password.isEmpty()) {
            strengthLabel.setText("");
        } else if (password.length() < 6) {
            strengthLabel.setText("Password too short (min 6 chars)");
            strengthLabel.setForeground(ModernUIComponents.COLOR_DANGER);
        } else {
            boolean hasUppercase = false;
            boolean hasDigit = false;
            for (char c : password.toCharArray()) {
                if (Character.isUpperCase(c)) hasUppercase = true;
                if (Character.isDigit(c)) hasDigit = true;
            }
            if (!hasUppercase || !hasDigit) {
                strengthLabel.setText("Weak: Add an uppercase letter and a number");
                strengthLabel.setForeground(ModernUIComponents.COLOR_DANGER);
            } else {
                strengthLabel.setText("Strong Password ✓");
                strengthLabel.setForeground(ModernUIComponents.COLOR_SUCCESS);
            }
        }
    }

    private void toggleMode(JLabel promptLabel) {
        isLoginMode = !isLoginMode;
        statusLabel.setText("");
        strengthLabel.setText("");
        
        if (isLoginMode) {
            titleLabel.setText("APEX FINANCE");
            actionBtn.setText("Log In");
            promptLabel.setText("Don't have an account?");
            switchModeBtn.setText("Register Now");
            usernameField.setText("");
            passwordField.setText("");
        } else {
            titleLabel.setText("CREATE ACCOUNT");
            actionBtn.setText("Sign Up");
            promptLabel.setText("Already registered?");
            switchModeBtn.setText("Log In");
            usernameField.setText("");
            passwordField.setText("");
        }
    }

    private void handleAction() {
        statusLabel.setText("");
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showStatus("Please fill in all fields.", false);
            return;
        }

        if (isLoginMode) {
            performLogin(username, password);
        } else {
            performRegistration(username, password);
        }
    }

    private void performLogin(String username, String password) {
        try (Connection con = DBConnector.getConnection()) {
            String sql = "SELECT id, password_hash, salt FROM users WHERE username = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String passwordHash = rs.getString("password_hash");
                String salt = rs.getString("salt");

                String computedHash = SecurityUtils.hashPassword(password, salt);
                if (computedHash.equals(passwordHash)) {
                    showStatus("Login successful! Loading dashboard...", true);
                    Timer timer = new Timer(800, e -> {
                        dispose();
                        new Dashboard(id, username);
                    });
                    timer.setRepeats(false);
                    timer.start();
                } else {
                    showStatus("Invalid password. Try again.", false);
                }
            } else {
                showStatus("User not found. Sign up below!", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showStatus("Database connection error.", false);
        }
    }

    private void performRegistration(String username, String password) {
        if (!SecurityUtils.isValidUsername(username)) {
            showStatus("Username must be 3-20 alphanumeric characters.", false);
            return;
        }

        if (!SecurityUtils.isPasswordStrong(password)) {
            showStatus("Password does not meet requirements.", false);
            return;
        }

        try (Connection con = DBConnector.getConnection()) {
            String salt = SecurityUtils.generateSalt();
            String hash = SecurityUtils.hashPassword(password, salt);

            String sql = "INSERT INTO users(username, password_hash, salt, balance) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, hash);
            pst.setString(3, salt);
            pst.setDouble(4, 500.00); // Starter bonus balance of ₹500 to delight new users!

            pst.executeUpdate();
            
            showStatus("Registered successfully! Logging you in...", true);
            
            // Automatically log them in after a short delay
            Timer timer = new Timer(1200, e -> {
                // Get the user ID
                try {
                    PreparedStatement getID = con.prepareStatement("SELECT id FROM users WHERE username = ?");
                    getID.setString(1, username);
                    ResultSet rs = getID.executeQuery();
                    if (rs.next()) {
                        int id = rs.getInt("id");
                        dispose();
                        new Dashboard(id, username);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    toggleMode(new JLabel()); // Fallback to login screen
                }
            });
            timer.setRepeats(false);
            timer.start();

        } catch (SQLIntegrityConstraintViolationException e) {
            showStatus("Username is already taken.", false);
        } catch (Exception e) {
            e.printStackTrace();
            showStatus("Registration failed. Try again.", false);
        }
    }

    private void showStatus(String message, boolean isSuccess) {
        statusLabel.setText(message);
        statusLabel.setForeground(isSuccess ? ModernUIComponents.COLOR_SUCCESS : ModernUIComponents.COLOR_DANGER);
    }

    public static void main(String[] args) {
        // Set Look and feel of System if possible
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        
        SwingUtilities.invokeLater(LoginForm::new);
    }
}

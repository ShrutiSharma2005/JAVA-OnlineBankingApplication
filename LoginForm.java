// LoginForm.java
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class LoginForm extends JFrame {
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginBtn, registerBtn;

    public LoginForm() {
        setTitle("Online Bank Login");
        setSize(300, 180);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel uLabel = new JLabel("Username:");
        uLabel.setBounds(20, 20, 80, 25);
        add(uLabel);

        usernameField = new JTextField();
        usernameField.setBounds(100, 20, 150, 25);
        add(usernameField);

        JLabel pLabel = new JLabel("Password:");
        pLabel.setBounds(20, 60, 80, 25);
        add(pLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 60, 150, 25);
        add(passwordField);

        loginBtn = new JButton("Login");
        loginBtn.setBounds(50, 100, 80, 25);
        add(loginBtn);

        registerBtn = new JButton("Register");
        registerBtn.setBounds(140, 100, 100, 25);
        add(registerBtn);

        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> register());

        setVisible(true);
    }

    void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection con = DBConnector.getConnection()) {
            PreparedStatement pst = con.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                dispose();
                new Dashboard(userId, username);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void register() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection con = DBConnector.getConnection()) {
            PreparedStatement pst = con.prepareStatement("INSERT INTO users(username, password, balance) VALUES (?, ?, ?)");
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setDouble(3, 0.0);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "User registered! Please log in.");
        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this, "Username already exists.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LoginForm();
    }
}

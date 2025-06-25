import javax.swing.*;
import java.awt.event.*;

public class LoginForm extends JFrame {
    JTextField userField;
    JPasswordField passField;
    JButton loginBtn;

    public LoginForm() {
        setTitle("Bank Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(20, 20, 80, 25);
        add(userLabel);

        userField = new JTextField();
        userField.setBounds(100, 20, 150, 25);
        add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(20, 50, 80, 25);
        add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(100, 50, 150, 25);
        add(passField);

        loginBtn = new JButton("Login");
        loginBtn.setBounds(100, 80, 80, 25);
        add(loginBtn);

        loginBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            // Here you can connect JDBC login check
            JOptionPane.showMessageDialog(this, "Login attempt: " + user);
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginForm();
    }
}

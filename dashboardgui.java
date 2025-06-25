import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class Dashboard extends JFrame {
    int userId;
    String username;
    JLabel balanceLabel;
    JTextArea logArea;

    public Dashboard(int userId, String username) {
        this.userId = userId;
        this.username = username;

        setTitle("Welcome, " + username);
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel welcome = new JLabel("Hello, " + username);
        welcome.setBounds(20, 10, 200, 25);
        add(welcome);

        balanceLabel = new JLabel("Balance: ₹0.00");
        balanceLabel.setBounds(20, 40, 200, 25);
        add(balanceLabel);

        JButton depositBtn = new JButton("Deposit");
        depositBtn.setBounds(20, 80, 100, 30);
        add(depositBtn);

        JButton withdrawBtn = new JButton("Withdraw");
        withdrawBtn.setBounds(130, 80, 100, 30);
        add(withdrawBtn);

        JButton viewLogBtn = new JButton("View Transactions");
        viewLogBtn.setBounds(240, 80, 130, 30);
        add(viewLogBtn);

        logArea = new JTextArea();
        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setBounds(20, 130, 350, 200);
        add(scroll);

        depositBtn.addActionListener(e -> deposit());
        withdrawBtn.addActionListener(e -> withdraw());
        viewLogBtn.addActionListener(e -> viewTransactions());

        refreshBalance();

        setVisible(true);
    }

    void refreshBalance() {
        try (Connection con = DBConnector.getConnection()) {
            PreparedStatement pst = con.prepareStatement("SELECT balance FROM users WHERE id=?");
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                balanceLabel.setText("Balance: ₹" + balance);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void deposit() {
        String input = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
        if (input == null) return;
        try {
            double amount = Double.parseDouble(input);
            if (amount <= 0) throw new NumberFormatException();

            try (Connection con = DBConnector.getConnection()) {
                con.setAutoCommit(false);

                PreparedStatement pst1 = con.prepareStatement("UPDATE users SET balance = balance + ? WHERE id=?");
                pst1.setDouble(1, amount);
                pst1.setInt(2, userId);
                pst1.executeUpdate();

                PreparedStatement pst2 = con.prepareStatement("INSERT INTO transactions(user_id, type, amount) VALUES (?, 'Deposit', ?)");
                pst2.setInt(1, userId);
                pst2.setDouble(2, amount);
                pst2.executeUpdate();

                con.commit();
                JOptionPane.showMessageDialog(this, "Deposited ₹" + amount);
                refreshBalance();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid amount.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void withdraw() {
        String input = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
        if (input == null) return;
        try {
            double amount = Double.parseDouble(input);
            if (amount <= 0) throw new NumberFormatException();

            try (Connection con = DBConnector.getConnection()) {
                con.setAutoCommit(false);

                PreparedStatement check = con.prepareStatement("SELECT balance FROM users WHERE id=?");
                check.setInt(1, userId);
                ResultSet rs = check.executeQuery();
                if (rs.next()) {
                    double balance = rs.getDouble("balance");
                    if (balance < amount) {
                        JOptionPane.showMessageDialog(this, "Insufficient funds.");
                        return;
                    }
                }

                PreparedStatement pst1 = con.prepareStatement("UPDATE users SET balance = balance - ? WHERE id=?");
                pst1.setDouble(1, amount);
                pst1.setInt(2, userId);
                pst1.executeUpdate();

                PreparedStatement pst2 = con.prepareStatement("INSERT INTO transactions(user_id, type, amount) VALUES (?, 'Withdraw', ?)");
                pst2.setInt(1, userId);
                pst2.setDouble(2, amount);
                pst2.executeUpdate();

                con.commit();
                JOptionPane.showMessageDialog(this, "Withdrawn ₹" + amount);
                refreshBalance();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid amount.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void viewTransactions() {
        logArea.setText("");
        try (Connection con = DBConnector.getConnection()) {
            PreparedStatement pst = con.prepareStatement("SELECT * FROM transactions WHERE user_id=? ORDER BY timestamp DESC");
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String type = rs.getString("type");
                double amount = rs.getDouble("amount");
                String time = rs.getTimestamp("timestamp").toString();

                logArea.append(type + ": ₹" + amount + " at " + time + "\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

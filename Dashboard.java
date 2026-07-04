import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Dashboard extends JFrame {

    private int userId;
    private String username;

    // Balance & Goal States
    private double balance = 0.0;
    private String goalName = "";
    private double goalTarget = 0.0;
    private double goalCurrent = 0.0;

    // UI Components
    private JLabel balanceValLabel;
    private JLabel goalTitleLabel;
    private JLabel goalProgressText;
    private ModernUIComponents.ProgressBarCustom goalProgressBar;
    
    // Transactions & Analytics
    private JPanel transactionListPanel;
    private JPanel chartDrawPanel;
    
    // AI Chat Components
    private JTextArea aiChatArea;
    private JTextField aiInputField;

    public Dashboard(int userId, String username) {
        this.userId = userId;
        this.username = username;

        setTitle("Apex Finance - Dashboard (" + username + ")");
        setSize(940, 640);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main background layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ModernUIComponents.COLOR_BG);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        add(mainPanel);

        // 1. LEFT PANEL - Wallet Control Center
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(320, 600));

        // Wallet Balance Card
        ModernUIComponents.RoundedPanel balanceCard = new ModernUIComponents.RoundedPanel(15, ModernUIComponents.COLOR_PRIMARY);
        balanceCard.setLayout(new BorderLayout());
        balanceCard.setMaximumSize(new Dimension(320, 150));
        balanceCard.setPreferredSize(new Dimension(320, 150));
        balanceCard.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel cardTitle = new JLabel("TOTAL WALLET BALANCE");
        cardTitle.setFont(ModernUIComponents.FONT_SMALL);
        cardTitle.setForeground(new Color(224, 231, 255));
        balanceValLabel = new JLabel("₹0.00");
        balanceValLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        balanceValLabel.setForeground(Color.WHITE);
        
        JLabel accountInfo = new JLabel("User: " + username + "  |  Secure Account");
        accountInfo.setFont(ModernUIComponents.FONT_SMALL);
        accountInfo.setForeground(new Color(224, 231, 255));

        balanceCard.add(cardTitle, BorderLayout.NORTH);
        balanceCard.add(balanceValLabel, BorderLayout.CENTER);
        balanceCard.add(accountInfo, BorderLayout.SOUTH);
        leftPanel.add(balanceCard);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Operations Panel
        ModernUIComponents.RoundedPanel opsCard = new ModernUIComponents.RoundedPanel(15, ModernUIComponents.COLOR_CARD);
        opsCard.setLayout(new GridLayout(4, 1, 0, 10));
        opsCard.setMaximumSize(new Dimension(320, 240));
        opsCard.setPreferredSize(new Dimension(320, 240));
        opsCard.setBorder(new EmptyBorder(15, 15, 15, 15));

        ModernUIComponents.ModernButton depBtn = new ModernUIComponents.ModernButton("Deposit Funds", ModernUIComponents.COLOR_PRIMARY, ModernUIComponents.COLOR_PRIMARY_HOVER);
        ModernUIComponents.ModernButton witBtn = new ModernUIComponents.ModernButton("Withdraw Cash", new Color(45, 55, 72), new Color(74, 85, 104));
        ModernUIComponents.ModernButton transferBtn = new ModernUIComponents.ModernButton("P2P Fund Transfer", ModernUIComponents.COLOR_ACCENT, new Color(147, 51, 234));
        ModernUIComponents.ModernButton exitBtn = new ModernUIComponents.ModernButton("Secure Logout", ModernUIComponents.COLOR_DANGER, new Color(220, 38, 38));

        opsCard.add(depBtn);
        opsCard.add(witBtn);
        opsCard.add(transferBtn);
        opsCard.add(exitBtn);
        leftPanel.add(opsCard);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Savings Goals Widget
        ModernUIComponents.RoundedPanel goalsCard = new ModernUIComponents.RoundedPanel(15, ModernUIComponents.COLOR_CARD);
        goalsCard.setLayout(null);
        goalsCard.setMaximumSize(new Dimension(320, 160));
        goalsCard.setPreferredSize(new Dimension(320, 160));
        goalsCard.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel goalHeader = new JLabel("SAVINGS GOAL");
        goalHeader.setFont(ModernUIComponents.FONT_BOLD);
        goalHeader.setForeground(ModernUIComponents.COLOR_TEXT_MUTED);
        goalHeader.setBounds(15, 10, 120, 20);
        goalsCard.add(goalHeader);

        ModernUIComponents.ModernButton setGoalBtn = new ModernUIComponents.ModernButton("Setup");
        setGoalBtn.setBounds(230, 8, 70, 24);
        setGoalBtn.setFont(ModernUIComponents.FONT_SMALL);
        goalsCard.add(setGoalBtn);

        goalTitleLabel = new JLabel("No Goal Defined");
        goalTitleLabel.setFont(ModernUIComponents.FONT_BODY);
        goalTitleLabel.setForeground(ModernUIComponents.COLOR_TEXT_MAIN);
        goalTitleLabel.setBounds(15, 38, 200, 20);
        goalsCard.add(goalTitleLabel);

        goalProgressBar = new ModernUIComponents.ProgressBarCustom();
        goalProgressBar.setBounds(15, 65, 280, 12);
        goalsCard.add(goalProgressBar);

        goalProgressText = new JLabel("₹0.00 / ₹0.00 (0%)");
        goalProgressText.setFont(ModernUIComponents.FONT_SMALL);
        goalProgressText.setForeground(ModernUIComponents.COLOR_TEXT_MUTED);
        goalProgressText.setBounds(15, 82, 200, 20);
        goalsCard.add(goalProgressText);

        ModernUIComponents.ModernButton saveFundsBtn = new ModernUIComponents.ModernButton("Save Money", ModernUIComponents.COLOR_SUCCESS, new Color(22, 163, 74));
        saveFundsBtn.setBounds(15, 110, 130, 32);
        saveFundsBtn.setFont(ModernUIComponents.FONT_SMALL);
        goalsCard.add(saveFundsBtn);

        ModernUIComponents.ModernButton releaseFundsBtn = new ModernUIComponents.ModernButton("Withdraw Goal", new Color(75, 85, 99), new Color(107, 114, 128));
        releaseFundsBtn.setBounds(165, 110, 130, 32);
        releaseFundsBtn.setFont(ModernUIComponents.FONT_SMALL);
        goalsCard.add(releaseFundsBtn);

        leftPanel.add(goalsCard);
        mainPanel.add(leftPanel, BorderLayout.WEST);

        // 2. RIGHT PANEL - Content Area Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(ModernUIComponents.FONT_BOLD);
        tabbedPane.setBackground(ModernUIComponents.COLOR_BG);
        tabbedPane.setForeground(ModernUIComponents.COLOR_TEXT_MUTED);

        // TAB A - Transactions & Spending Analytics
        JPanel analyticsPanel = new JPanel(null);
        analyticsPanel.setBackground(ModernUIComponents.COLOR_BG);

        // Transactions Container
        JLabel transactionsTitle = new JLabel("RECENT TRANSACTIONS");
        transactionsTitle.setFont(ModernUIComponents.FONT_SUBTITLE);
        transactionsTitle.setForeground(ModernUIComponents.COLOR_TEXT_MAIN);
        transactionsTitle.setBounds(15, 10, 250, 25);
        analyticsPanel.add(transactionsTitle);

        ModernUIComponents.RoundedPanel feedCard = new ModernUIComponents.RoundedPanel(15, ModernUIComponents.COLOR_CARD);
        feedCard.setBounds(15, 45, 545, 230);
        feedCard.setLayout(new BorderLayout());
        transactionListPanel = new JPanel();
        transactionListPanel.setLayout(new BoxLayout(transactionListPanel, BoxLayout.Y_AXIS));
        transactionListPanel.setBackground(ModernUIComponents.COLOR_CARD);
        JScrollPane scrollFeed = new JScrollPane(transactionListPanel);
        scrollFeed.setBorder(null);
        scrollFeed.getVerticalScrollBar().setUnitIncrement(12);
        feedCard.add(scrollFeed, BorderLayout.CENTER);
        analyticsPanel.add(feedCard);

        // Spending Analytics Container
        JLabel chartTitleLabel = new JLabel("SPENDING BY CATEGORY");
        chartTitleLabel.setFont(ModernUIComponents.FONT_SUBTITLE);
        chartTitleLabel.setForeground(ModernUIComponents.COLOR_TEXT_MAIN);
        chartTitleLabel.setBounds(15, 290, 250, 25);
        analyticsPanel.add(chartTitleLabel);

        ModernUIComponents.RoundedPanel chartCard = new ModernUIComponents.RoundedPanel(15, ModernUIComponents.COLOR_CARD);
        chartCard.setBounds(15, 325, 545, 230);
        chartCard.setLayout(new BorderLayout());
        
        // Custom Paint Panel for line/bar chart
        chartDrawPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawSpendingChart(g);
            }
        };
        chartDrawPanel.setBackground(ModernUIComponents.COLOR_CARD);
        chartCard.add(chartDrawPanel, BorderLayout.CENTER);
        analyticsPanel.add(chartCard);

        tabbedPane.addTab("Analytics & Feed", analyticsPanel);

        // TAB B - Simulated AI Financial Coach
        JPanel aiCoachPanel = new JPanel(new BorderLayout(10, 10));
        aiCoachPanel.setBackground(ModernUIComponents.COLOR_BG);
        aiCoachPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel aiHeaderLabel = new JLabel("APEX AI FINANCIAL WEALTH COACH");
        aiHeaderLabel.setFont(ModernUIComponents.FONT_SUBTITLE);
        aiHeaderLabel.setForeground(ModernUIComponents.COLOR_TEXT_MAIN);
        aiCoachPanel.add(aiHeaderLabel, BorderLayout.NORTH);

        aiChatArea = new JTextArea();
        aiChatArea.setEditable(false);
        aiChatArea.setFont(ModernUIComponents.FONT_BODY);
        aiChatArea.setBackground(ModernUIComponents.COLOR_CARD);
        aiChatArea.setForeground(ModernUIComponents.COLOR_TEXT_MAIN);
        aiChatArea.setLineWrap(true);
        aiChatArea.setWrapStyleWord(true);
        aiChatArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane chatScroll = new JScrollPane(aiChatArea);
        chatScroll.setBorder(BorderFactory.createLineBorder(ModernUIComponents.COLOR_BORDER, 1));
        aiCoachPanel.add(chatScroll, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setOpaque(false);

        aiInputField = new JTextField();
        aiInputField.setFont(ModernUIComponents.FONT_BODY);
        aiInputField.setBackground(ModernUIComponents.COLOR_CARD);
        aiInputField.setForeground(ModernUIComponents.COLOR_TEXT_MAIN);
        aiInputField.setCaretColor(ModernUIComponents.COLOR_TEXT_MAIN);
        aiInputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ModernUIComponents.COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        ModernUIComponents.ModernButton sendBtn = new ModernUIComponents.ModernButton("Ask Coach", ModernUIComponents.COLOR_PRIMARY, ModernUIComponents.COLOR_PRIMARY_HOVER);
        sendBtn.setPreferredSize(new Dimension(110, 40));

        inputPanel.add(aiInputField, BorderLayout.CENTER);
        inputPanel.add(sendBtn, BorderLayout.EAST);
        aiCoachPanel.add(inputPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("AI Wealth Coach", aiCoachPanel);

        // Wrap tabs with padding
        JPanel rightContainer = new JPanel(new BorderLayout());
        rightContainer.setOpaque(false);
        rightContainer.setBorder(new EmptyBorder(0, 15, 0, 0));
        rightContainer.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(rightContainer, BorderLayout.CENTER);

        // Core Event Handlers
        depBtn.addActionListener(e -> handleDeposit());
        witBtn.addActionListener(e -> handleWithdraw());
        transferBtn.addActionListener(e -> handleTransfer());
        setGoalBtn.addActionListener(e -> handleSetGoal());
        saveFundsBtn.addActionListener(e -> handleSaveGoalFunds(true));
        releaseFundsBtn.addActionListener(e -> handleSaveGoalFunds(false));
        exitBtn.addActionListener(e -> handleLogout());
        sendBtn.addActionListener(e -> handleAICoachQuery());
        aiInputField.addActionListener(e -> handleAICoachQuery());

        // Startup Load
        refreshData();
        appendAIChat("Coach: Hello " + username + "! I am your personal financial AI coach. I can help analyze your expenses, suggest savings structures, and give financial guidance. Type 'analyze my spending' to get a customized budget diagnostic!");

        setVisible(true);
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to securely log out?", 
                "Confirm Logout", 
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginForm();
        }
    }

    private void refreshData() {
        // 1. Fetch User Data
        try (Connection con = DBConnector.getConnection()) {
            String sql = "SELECT balance, savings_goal_name, savings_goal_target, savings_goal_current FROM users WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble("balance");
                goalName = rs.getString("savings_goal_name");
                goalTarget = rs.getDouble("savings_goal_target");
                goalCurrent = rs.getDouble("savings_goal_current");

                balanceValLabel.setText(String.format("₹%,.2f", balance));

                if (goalName == null || goalName.trim().isEmpty()) {
                    goalTitleLabel.setText("No Savings Goal Defined");
                    goalProgressText.setText("₹0.00 / ₹0.00 (0%)");
                    goalProgressBar.setProgress(0.0);
                } else {
                    goalTitleLabel.setText(goalName);
                    double percent = goalTarget > 0 ? (goalCurrent / goalTarget) : 0.0;
                    goalProgressText.setText(String.format("₹%,.2f / ₹%,.2f (%d%%)", 
                            goalCurrent, goalTarget, (int)(percent * 100)));
                    goalProgressBar.setProgress(percent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. Fetch Recent Transactions
        transactionListPanel.removeAll();
        try (Connection con = DBConnector.getConnection()) {
            String sql = "SELECT type, amount, category, description, timestamp FROM transactions WHERE user_id = ? ORDER BY timestamp DESC LIMIT 15";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            
            boolean hasTransactions = false;
            while (rs.next()) {
                hasTransactions = true;
                String type = rs.getString("type");
                double amt = rs.getDouble("amount");
                String cat = rs.getString("category");
                String desc = rs.getString("description");
                Timestamp ts = rs.getTimestamp("timestamp");

                JPanel row = new JPanel(new BorderLayout(15, 0));
                row.setOpaque(false);
                row.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, ModernUIComponents.COLOR_BORDER),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));

                // Left icon indicator based on type
                JLabel typeIcon = new JLabel(type.contains("Deposit") || type.contains("In") ? "▲" : "▼");
                typeIcon.setFont(new Font("Segoe UI", Font.BOLD, 14));
                typeIcon.setForeground(type.contains("Deposit") || type.contains("In") ? ModernUIComponents.COLOR_SUCCESS : ModernUIComponents.COLOR_DANGER);
                row.add(typeIcon, BorderLayout.WEST);

                // Central info
                JPanel details = new JPanel(new GridLayout(2, 1, 2, 2));
                details.setOpaque(false);
                JLabel descLabel = new JLabel((desc == null || desc.isEmpty()) ? type : desc);
                descLabel.setFont(ModernUIComponents.FONT_BOLD);
                descLabel.setForeground(ModernUIComponents.COLOR_TEXT_MAIN);
                
                JLabel catTimeLabel = new JLabel(cat + " • " + ts.toString().substring(0, 16));
                catTimeLabel.setFont(ModernUIComponents.FONT_SMALL);
                catTimeLabel.setForeground(ModernUIComponents.COLOR_TEXT_MUTED);

                details.add(descLabel);
                details.add(catTimeLabel);
                row.add(details, BorderLayout.CENTER);

                // Right amount
                String prefix = type.contains("Deposit") || type.contains("In") ? "+" : "-";
                JLabel amtLabel = new JLabel(prefix + String.format("₹%,.2f", amt));
                amtLabel.setFont(ModernUIComponents.FONT_BOLD);
                amtLabel.setForeground(type.contains("Deposit") || type.contains("In") ? ModernUIComponents.COLOR_SUCCESS : ModernUIComponents.COLOR_DANGER);
                row.add(amtLabel, BorderLayout.EAST);

                transactionListPanel.add(row);
            }

            if (!hasTransactions) {
                JLabel emptyLabel = new JLabel("No transactions logged yet.", SwingConstants.CENTER);
                emptyLabel.setFont(ModernUIComponents.FONT_BODY);
                emptyLabel.setForeground(ModernUIComponents.COLOR_TEXT_MUTED);
                emptyLabel.setBorder(BorderFactory.createEmptyBorder(40, 10, 40, 10));
                transactionListPanel.add(emptyLabel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        transactionListPanel.revalidate();
        transactionListPanel.repaint();

        // 3. Repaint Chart Panel
        chartDrawPanel.repaint();
    }

    private void handleDeposit() {
        String input = JOptionPane.showInputDialog(this, "Enter amount to deposit:", "Deposit Funds", JOptionPane.QUESTION_MESSAGE);
        if (input == null) return;
        try {
            double amount = Double.parseDouble(input);
            if (amount <= 0) throw new NumberFormatException();

            // Select Category
            String[] categories = {"Investment", "Others"};
            String cat = (String) JOptionPane.showInputDialog(this, "Select Deposit Category:", 
                    "Category Selection", JOptionPane.QUESTION_MESSAGE, null, categories, categories[0]);
            if (cat == null) cat = "Others";

            try (Connection con = DBConnector.getConnection()) {
                con.setAutoCommit(false);
                
                // Add to balance
                PreparedStatement pst = con.prepareStatement("UPDATE users SET balance = balance + ? WHERE id = ?");
                pst.setDouble(1, amount);
                pst.setInt(2, userId);
                pst.executeUpdate();

                // Log transaction
                PreparedStatement logPst = con.prepareStatement(
                        "INSERT INTO transactions(user_id, type, amount, category, description) VALUES (?, 'Deposit', ?, ?, ?)");
                logPst.setInt(1, userId);
                logPst.setDouble(2, amount);
                logPst.setString(3, cat);
                logPst.setString(4, "Deposited cash via counter");
                logPst.executeUpdate();

                con.commit();
                JOptionPane.showMessageDialog(this, String.format("Deposited ₹%,.2f successfully!", amount), 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid positive number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleWithdraw() {
        String input = JOptionPane.showInputDialog(this, "Enter amount to withdraw:", "Withdraw Funds", JOptionPane.QUESTION_MESSAGE);
        if (input == null) return;
        try {
            double amount = Double.parseDouble(input);
            if (amount <= 0) throw new NumberFormatException();

            if (amount > balance) {
                JOptionPane.showMessageDialog(this, "Insufficient balance in your wallet.", "Overdraft Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Select Category
            String[] categories = {"Food", "Shopping", "Utilities", "Entertainment", "Others"};
            String cat = (String) JOptionPane.showInputDialog(this, "Select Expense Category:", 
                    "Category Selection", JOptionPane.QUESTION_MESSAGE, null, categories, categories[0]);
            if (cat == null) cat = "Others";

            try (Connection con = DBConnector.getConnection()) {
                con.setAutoCommit(false);
                
                // Deduct from balance
                PreparedStatement pst = con.prepareStatement("UPDATE users SET balance = balance - ? WHERE id = ?");
                pst.setDouble(1, amount);
                pst.setInt(2, userId);
                pst.executeUpdate();

                // Log transaction
                PreparedStatement logPst = con.prepareStatement(
                        "INSERT INTO transactions(user_id, type, amount, category, description) VALUES (?, 'Withdraw', ?, ?, ?)");
                logPst.setInt(1, userId);
                logPst.setDouble(2, amount);
                logPst.setString(3, cat);
                logPst.setString(4, "ATM cash withdrawal");
                logPst.executeUpdate();

                con.commit();
                JOptionPane.showMessageDialog(this, String.format("Withdrawn ₹%,.2f successfully!", amount), 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid positive number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleTransfer() {
        // Custom panels for robust user inputs
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 10));
        JTextField toField = new JTextField();
        JTextField amtField = new JTextField();
        JComboBox<String> catBox = new JComboBox<>(new String[]{"Food", "Shopping", "Utilities", "Investment", "Entertainment", "Others"});
        JTextField descField = new JTextField();

        panel.add(new JLabel("Recipient Username:"));
        panel.add(toField);
        panel.add(new JLabel("Amount (₹):"));
        panel.add(amtField);
        panel.add(new JLabel("Category:"));
        panel.add(catBox);
        panel.add(new JLabel("Description / Notes:"));
        panel.add(descField);

        int result = JOptionPane.showConfirmDialog(this, panel, "P2P Money Transfer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        String recipient = toField.getText().trim();
        String amtStr = amtField.getText().trim();
        String category = (String) catBox.getSelectedItem();
        String desc = descField.getText().trim();

        if (recipient.equalsIgnoreCase(username)) {
            JOptionPane.showMessageDialog(this, "You cannot transfer money to yourself.", "Invalid Recipient", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double amount = Double.parseDouble(amtStr);
            if (amount <= 0) throw new NumberFormatException();

            if (amount > balance) {
                JOptionPane.showMessageDialog(this, "Insufficient balance.", "Transfer Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection con = DBConnector.getConnection()) {
                con.setAutoCommit(false);

                // 1. Verify Recipient and obtain ID
                PreparedStatement recPst = con.prepareStatement("SELECT id FROM users WHERE username = ?");
                recPst.setString(1, recipient);
                ResultSet rs = recPst.executeQuery();
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "User '" + recipient + "' not found.", "Transfer Failed", JOptionPane.ERROR_MESSAGE);
                    con.rollback();
                    return;
                }
                int recId = rs.getInt("id");

                // 2. Deduct Sender
                PreparedStatement dedPst = con.prepareStatement("UPDATE users SET balance = balance - ? WHERE id = ?");
                dedPst.setDouble(1, amount);
                dedPst.setInt(2, userId);
                dedPst.executeUpdate();

                // 3. Credit Recipient
                PreparedStatement credPst = con.prepareStatement("UPDATE users SET balance = balance + ? WHERE id = ?");
                credPst.setDouble(1, amount);
                credPst.setInt(2, recId);
                credPst.executeUpdate();

                // 4. Log Transaction (Sender)
                String senderDesc = "Transferred to " + recipient + (desc.isEmpty() ? "" : ": " + desc);
                PreparedStatement logSendPst = con.prepareStatement(
                        "INSERT INTO transactions(user_id, type, amount, category, description) VALUES (?, 'Transfer Out', ?, ?, ?)");
                logSendPst.setInt(1, userId);
                logSendPst.setDouble(2, amount);
                logSendPst.setString(3, category);
                logSendPst.setString(4, senderDesc);
                logSendPst.executeUpdate();

                // 5. Log Transaction (Recipient)
                String recDesc = "Received from " + username + (desc.isEmpty() ? "" : ": " + desc);
                PreparedStatement logRecPst = con.prepareStatement(
                        "INSERT INTO transactions(user_id, type, amount, category, description) VALUES (?, 'Transfer In', ?, ?, ?)");
                logRecPst.setInt(1, recId);
                logRecPst.setDouble(2, amount);
                logRecPst.setString(3, category);
                logRecPst.setString(4, recDesc);
                logRecPst.executeUpdate();

                con.commit();
                JOptionPane.showMessageDialog(this, String.format("Transferred ₹%,.2f to %s successfully!", amount, recipient), 
                        "Transfer Complete", JOptionPane.INFORMATION_MESSAGE);
                refreshData();

            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid positive number for amount.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSetGoal() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 10));
        JTextField nameField = new JTextField(goalName);
        JTextField targetField = new JTextField(goalTarget > 0 ? String.valueOf(goalTarget) : "");

        panel.add(new JLabel("Savings Goal Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Target Amount (₹):"));
        panel.add(targetField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Define Savings Goal", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        String name = nameField.getText().trim();
        String targetStr = targetField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Goal name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double target = Double.parseDouble(targetStr);
            if (target <= 0) throw new NumberFormatException();

            try (Connection con = DBConnector.getConnection()) {
                PreparedStatement pst = con.prepareStatement(
                        "UPDATE users SET savings_goal_name = ?, savings_goal_target = ?, savings_goal_current = 0 WHERE id = ?");
                pst.setString(1, name);
                pst.setDouble(2, target);
                pst.setInt(3, userId);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Savings Goal configured successfully!", "Goal Setup", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid positive number for target.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSaveGoalFunds(boolean isAdding) {
        if (goalName == null || goalName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please setup a savings goal first.", "No Active Goal", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String labelText = isAdding ? "Enter amount to save towards '" + goalName + "':" 
                                    : "Enter amount to release from '" + goalName + "' to wallet:";
        String input = JOptionPane.showInputDialog(this, labelText, isAdding ? "Add Savings" : "Release Savings", JOptionPane.QUESTION_MESSAGE);
        if (input == null) return;

        try {
            double amount = Double.parseDouble(input);
            if (amount <= 0) throw new NumberFormatException();

            try (Connection con = DBConnector.getConnection()) {
                con.setAutoCommit(false);

                if (isAdding) {
                    // Check main balance
                    if (amount > balance) {
                        JOptionPane.showMessageDialog(this, "Insufficient main balance.", "Goal Savings Failed", JOptionPane.ERROR_MESSAGE);
                        con.rollback();
                        return;
                    }
                    // Update user fields
                    PreparedStatement pst = con.prepareStatement(
                            "UPDATE users SET balance = balance - ?, savings_goal_current = savings_goal_current + ? WHERE id = ?");
                    pst.setDouble(1, amount);
                    pst.setDouble(2, amount);
                    pst.setInt(3, userId);
                    pst.executeUpdate();

                    // Log transaction
                    PreparedStatement logPst = con.prepareStatement(
                            "INSERT INTO transactions(user_id, type, amount, category, description) VALUES (?, 'Savings', ?, 'Investment', ?)");
                    logPst.setInt(1, userId);
                    logPst.setDouble(2, amount);
                    logPst.setString(3, "Saved towards: " + goalName);
                    logPst.executeUpdate();
                } else {
                    // Check goal balance
                    if (amount > goalCurrent) {
                        JOptionPane.showMessageDialog(this, "Goal current savings cannot cover this amount.", "Release Failed", JOptionPane.ERROR_MESSAGE);
                        con.rollback();
                        return;
                    }
                    // Update user fields
                    PreparedStatement pst = con.prepareStatement(
                            "UPDATE users SET balance = balance + ?, savings_goal_current = savings_goal_current - ? WHERE id = ?");
                    pst.setDouble(1, amount);
                    pst.setDouble(2, amount);
                    pst.setInt(3, userId);
                    pst.executeUpdate();

                    // Log transaction
                    PreparedStatement logPst = con.prepareStatement(
                            "INSERT INTO transactions(user_id, type, amount, category, description) VALUES (?, 'Savings Release', ?, 'Investment', ?)");
                    logPst.setInt(1, userId);
                    logPst.setDouble(2, amount);
                    logPst.setString(3, "Released from: " + goalName);
                    logPst.executeUpdate();
                }

                con.commit();
                JOptionPane.showMessageDialog(this, "Transaction completed successfully!", "Updated Savings", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid positive number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawSpendingChart(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = chartDrawPanel.getWidth();
        int h = chartDrawPanel.getHeight();

        // 1. Gather category aggregates
        Map<String, Double> spends = new HashMap<>();
        double totalSpend = 0.0;
        
        try (Connection con = DBConnector.getConnection()) {
            String sql = "SELECT category, SUM(amount) as total FROM transactions WHERE user_id = ? AND type IN ('Withdraw', 'Transfer Out', 'Savings') GROUP BY category";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String cat = rs.getString("category");
                double val = rs.getDouble("total");
                spends.put(cat, val);
                totalSpend += val;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (totalSpend == 0.0) {
            g2.setColor(ModernUIComponents.COLOR_TEXT_MUTED);
            g2.setFont(ModernUIComponents.FONT_BODY);
            FontMetrics fm = g2.getFontMetrics();
            String msg = "No spending records found to construct chart.";
            g2.drawString(msg, (w - fm.stringWidth(msg)) / 2, h / 2);
            g2.dispose();
            return;
        }

        // Draw horizontal multi-segmented bar chart
        int barY = 40;
        int barH = 25;
        int barX = 30;
        int barW = w - 60;

        // Draw Category Color Keys
        Map<String, Color> colors = new HashMap<>();
        colors.put("Food", new Color(244, 63, 94)); // Red
        colors.put("Shopping", new Color(249, 115, 22)); // Orange
        colors.put("Utilities", new Color(14, 165, 233)); // Blue
        colors.put("Investment", new Color(16, 185, 129)); // Green
        colors.put("Entertainment", new Color(236, 72, 153)); // Pink
        colors.put("Others", ModernUIComponents.COLOR_TEXT_MUTED); // Gray

        // Draw segments
        double currentX = barX;
        for (Map.Entry<String, Double> entry : spends.entrySet()) {
            double share = entry.getValue() / totalSpend;
            int segmentW = (int) (barW * share);
            g2.setColor(colors.getOrDefault(entry.getKey(), colors.get("Others")));
            g2.fillRect((int) currentX, barY, segmentW, barH);
            currentX += segmentW;
        }

        // Segment Border Overlay (pill shape or rounded rect border)
        g2.setColor(ModernUIComponents.COLOR_BORDER);
        g2.drawRect(barX, barY, barW, barH);

        // Draw Category breakdown list with amounts
        int listY = 90;
        int listX = 40;
        int colW = 230;
        int i = 0;
        
        g2.setFont(ModernUIComponents.FONT_SMALL);
        for (Map.Entry<String, Double> entry : spends.entrySet()) {
            Color c = colors.getOrDefault(entry.getKey(), colors.get("Others"));
            int itemX = listX + (i % 2) * colW;
            int itemY = listY + (i / 2) * 32;

            // Draw colored dot
            g2.setColor(c);
            g2.fillOval(itemX, itemY - 7, 8, 8);

            // Draw category label & percentage
            g2.setColor(ModernUIComponents.COLOR_TEXT_MAIN);
            double pct = (entry.getValue() / totalSpend) * 100;
            String label = String.format("%s: ₹%,.2f (%d%%)", entry.getKey(), entry.getValue(), (int) pct);
            g2.drawString(label, itemX + 15, itemY);
            i++;
        }

        g2.dispose();
    }

    private void handleAICoachQuery() {
        String query = aiInputField.getText().trim();
        if (query.isEmpty()) return;

        appendAIChat("You: " + query);
        aiInputField.setText("");

        // Process Response in Background for smooth UI response
        Timer timer = new Timer(600, e -> {
            String answer = getAICoachResponse(query.toLowerCase());
            appendAIChat("Coach: " + answer);
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void appendAIChat(String text) {
        aiChatArea.append(text + "\n\n");
        aiChatArea.setCaretPosition(aiChatArea.getDocument().getLength());
    }

    private String getAICoachResponse(String query) {
        // Specific analysis query
        if (query.contains("analyze") || query.contains("spending") || query.contains("budget") || query.contains("expense")) {
            return generateSpendingAnalysisReport();
        }

        // Savings goal queries
        if (query.contains("saving") || query.contains("goal") || query.contains("save")) {
            if (goalName == null || goalName.trim().isEmpty()) {
                return "You haven't defined a savings goal yet! Setting up visual savings goals encourages consistency. Click the 'Setup' button on the left to set up a target like a 'Dream Car' or 'Emergency Fund'.";
            }
            double percentage = goalTarget > 0 ? (goalCurrent / goalTarget) * 100 : 0;
            return String.format("You are currently saving towards '%s'. You've saved ₹%,.2f out of your ₹%,.2f target (%.1f%% complete). " +
                    "To reach it faster, try setting up a micro-savings schedule: transfer ₹50 from your main wallet to this goal every time you deposit funds!", 
                    goalName, goalCurrent, goalTarget, percentage);
        }

        // P2P or transfers
        if (query.contains("transfer") || query.contains("send") || query.contains("p2p")) {
            return "P2P Transfers allow instant money sending to other accounts using secure SQL Transactions. Just click the 'P2P Fund Transfer' button, type the recipient's username, the amount, and optionally categorise it.";
        }

        // Generic financial tips keywords
        if (query.contains("tip") || query.contains("invest") || query.contains("advice")) {
            String[] tips = {
                "The 50/30/20 Rule: Allocate 50% of income to Needs, 30% to Wants, and 20% to Savings. Let's start tracking your categories in the dashboard!",
                "Inflation Shield: Leaving large sums in cash loses value over time. Consider investing your idle savings into dynamic mutual funds or high-yield deposit instruments.",
                "Emergency Reserve: Always keep 3-6 months of basic living expenses in a completely liquid wallet, like your savings goal card."
            };
            return tips[(int) (Math.random() * tips.length)];
        }

        if (query.contains("hello") || query.contains("hi") || query.contains("hey")) {
            return "Hi there! I am ready to review your financial portfolio. You can ask me to 'analyze my spending' or write a query like 'how to save money?'.";
        }

        return "Interesting query! I can help you budget better. If you want a tailored breakdown of your cash inflows and outflows, ask me to 'analyze my spending'!";
    }

    private String generateSpendingAnalysisReport() {
        double withdrawSum = 0.0;
        double transferOutSum = 0.0;
        double depositSum = 0.0;
        double totalExpense = 0.0;
        
        Map<String, Double> categoryTotals = new HashMap<>();
        String highestCategory = "N/A";
        double maxCatValue = 0.0;

        try (Connection con = DBConnector.getConnection()) {
            // Aggregate Types
            PreparedStatement pst = con.prepareStatement(
                    "SELECT type, SUM(amount) as total FROM transactions WHERE user_id = ? GROUP BY type");
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String type = rs.getString("type");
                double val = rs.getDouble("total");
                if (type.equals("Deposit") || type.equals("Transfer In")) {
                    depositSum += val;
                } else if (type.equals("Withdraw")) {
                    withdrawSum += val;
                    totalExpense += val;
                } else if (type.equals("Transfer Out")) {
                    transferOutSum += val;
                    totalExpense += val;
                }
            }

            // Category breakdown for expenses
            PreparedStatement pstCat = con.prepareStatement(
                    "SELECT category, SUM(amount) as total FROM transactions " +
                    "WHERE user_id = ? AND type IN ('Withdraw', 'Transfer Out') GROUP BY category");
            pstCat.setInt(1, userId);
            ResultSet rsCat = pstCat.executeQuery();
            while (rsCat.next()) {
                String cat = rsCat.getString("category");
                double val = rsCat.getDouble("total");
                categoryTotals.put(cat, val);
                if (val > maxCatValue) {
                    maxCatValue = val;
                    highestCategory = cat;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Apologies, I encountered an issue accessing your transaction database.";
        }

        if (totalExpense == 0.0) {
            return String.format("Analysis Report:\n• Inflows: ₹%,.2f\n• Outflows: ₹0.00\n\nYou haven't made any debit transactions yet. This is a great time to start building your emergency fund! Deposit some starter capital and track it in your Savings Goal widget.", depositSum);
        }

        double savingRatio = depositSum > 0 ? ((depositSum - totalExpense) / depositSum) * 100 : 0.0;

        StringBuilder report = new StringBuilder();
        report.append("=== APEX FIN-PORTFOLIO AUDIT ===\n");
        report.append(String.format("• Total Deposits/Inflows: ₹%,.2f\n", depositSum));
        report.append(String.format("• Total Outflows/Expenses: ₹%,.2f (ATM: ₹%,.2f, Transfers: ₹%,.2f)\n", totalExpense, withdrawSum, transferOutSum));
        report.append(String.format("• Current Savings Rate: %.1f%%\n\n", savingRatio));

        if (!highestCategory.equals("N/A")) {
            report.append(String.format("Critical Insight: Your primary expense is in '%s' (₹%,.2f), which accounts for %.1f%% of your total spending.\n\n", 
                    highestCategory, maxCatValue, (maxCatValue / totalExpense) * 100));
        }

        if (savingRatio < 20) {
            report.append("Recommendation: Your savings rate is below the recommended 20% threshold. I advise setting up a savings budget. Move ₹100 from your main account into your active Savings Goal right now to build financial momentum!");
        } else {
            report.append("Recommendation: Excellent! You are saving more than 20% of your deposits. Keep investing and log any savings goals in the left panel to maximize your capital compounding.");
        }

        return report.toString();
    }
}

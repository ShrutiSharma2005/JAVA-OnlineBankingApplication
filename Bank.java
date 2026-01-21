import java.util.*;
import java.io.*;

public class Bank {
    private List<User> users = new ArrayList<>();
    private User currentUser;

    public Bank() {
        loadUsers();
    }

    private void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                users.add(new User(parts[0], parts[1], Double.parseDouble(parts[2])));
            }
        } catch (IOException e) {
            System.out.println("No users found.");
        }
    }

    private void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt"))) {
            for (User user : users) {
                bw.write(user.getUsername() + "," + user.getPassword() + "," + user.getBalance());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users.");
        }
    }

    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public boolean register(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false; // User already exists
            }
        }
        users.add(new User(username, password, 0.0));
        saveUsers();
        return true;
    }

    public void showBalance() {
        System.out.println("Current Balance: Rs. " + currentUser.getBalance());
    }

    public void deposit(double amount) {
        currentUser.deposit(amount);
        logTransaction("Deposit", amount);
        System.out.println("Rs. " + amount + " deposited.");
        saveUsers();
    }

    public void withdraw(double amount) {
        if (currentUser.withdraw(amount)) {
            logTransaction("Withdraw", amount);
            System.out.println("Rs. " + amount + " withdrawn.");
        } else {
            System.out.println("Insufficient balance.");
        }
        saveUsers();
    }

    public boolean transfer(String targetUsername, double amount) {
        if (targetUsername.equals(currentUser.getUsername())) {
            System.out.println("Cannot transfer to yourself.");
            return false;
        }
        
        User targetUser = null;
        for (User user : users) {
            if (user.getUsername().equals(targetUsername)) {
                targetUser = user;
                break;
            }
        }

        if (targetUser == null) {
            System.out.println("Beneficiary user not found.");
            return false;
        }

        if (currentUser.withdraw(amount)) {
            targetUser.deposit(amount);
            logTransaction(currentUser.getUsername(), "Transfer to " + targetUsername, amount);
            logTransaction(targetUser.getUsername(), "Transfer from " + currentUser.getUsername(), amount);
            System.out.println("Success! Transferred Rs. " + amount + " to " + targetUsername);
            saveUsers();
            return true;
        } else {
            System.out.println("Insufficient balance.");
            return false;
        }
    }

    public void changePassword(String newPassword) {
        currentUser.setPassword(newPassword);
        System.out.println("Password changed successfully.");
        saveUsers();
    }

    private void logTransaction(String type, double amount) {
        logTransaction(currentUser.getUsername(), type, amount);
    }

    private void logTransaction(String username, String type, double amount) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("transactions.txt", true))) {
            bw.write(username + " - " + type + ": Rs. " + amount);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Failed to log transaction.");
        }
    }

    public void viewTransactions() {
        try (BufferedReader br = new BufferedReader(new FileReader("transactions.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(currentUser.getUsername())) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("No transaction history.");
        }
    }
}



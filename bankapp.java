import java.util.Scanner;

public class BankApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Bank bank = new Bank();

        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.print("Choose option: ");
        int authChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        boolean isAuthenticated = false;
        if (authChoice == 2) {
            if (bank.register(username, password)) {
                System.out.println("User registered successfully.");
                isAuthenticated = bank.login(username, password);
            } else {
                System.out.println("Registration failed. Username may already exist.");
            }
        } else {
            isAuthenticated = bank.login(username, password);
        }

        if (isAuthenticated) {
            int choice;
            do {
                System.out.println("\nWelcome " + username);
                System.out.println("1. Check Balance");
                System.out.println("2. Deposit Money");
                System.out.println("3. Withdraw Money");
                System.out.println("4. View Transactions");
                System.out.println("5. Transfer Money");
                System.out.println("6. Change Password");
                System.out.println("7. Exit");
                System.out.print("Choose an option: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1 -> bank.showBalance();
                    case 2 -> {
                        System.out.print("Enter amount to deposit: ");
                        bank.deposit(scanner.nextDouble());
                    }
                    case 3 -> {
                        System.out.print("Enter amount to withdraw: ");
                        bank.withdraw(scanner.nextDouble());
                    }
                    case 4 -> bank.viewTransactions();
                    case 5 -> {
                        System.out.print("Enter beneficiary username: ");
                        String target = scanner.nextLine();
                        System.out.print("Enter amount to transfer: ");
                        double amount = scanner.nextDouble();
                        bank.transfer(target, amount);
                    }
                    case 6 -> {
                         System.out.print("Enter new password: ");
                         String newPass = scanner.nextLine();
                         bank.changePassword(newPass);
                    }
                    case 7 -> System.out.println("Thank you for using our service.");
                    default -> System.out.println("Invalid option.");
                }
            } while (choice != 7);
        } else {
            System.out.println("Invalid credentials!");
        }
        scanner.close();
    }
}

import java.util.Scanner;

public class BankApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Bank bank = new Bank();

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (bank.login(username, password)) {
            int choice;
            do {
                System.out.println("\nWelcome " + username);
                System.out.println("1. Check Balance");
                System.out.println("2. Deposit Money");
                System.out.println("3. Withdraw Money");
                System.out.println("4. View Transactions");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                choice = scanner.nextInt();

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
                    case 5 -> System.out.println("Thank you for using our service.");
                    default -> System.out.println("Invalid option.");
                }
            } while (choice != 5);
        } else {
            System.out.println("Invalid credentials!");
        }
        scanner.close();
    }
}

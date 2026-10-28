
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

class Transaction {
    private String type;
    private double amount;
    private LocalDateTime dateTime;
    private double balanceAfter;

    public Transaction(String type, double amount, double balanceAfter) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.dateTime = LocalDateTime.now();
    }

    public void display() {
        DateTimeFormatter format =
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        System.out.printf(
                "%-12s Rs. %-10.2f Balance: Rs. %-10.2f %s%n",
                type, amount, balanceAfter, dateTime.format(format)
        );
    }
}

class BankAccount {
    private String accountNumber;
    private String ownerName;
    private double balance;
    private ArrayList<Transaction> transactions;

    public BankAccount(String accountNumber, String ownerName,
                       double openingBalance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = openingBalance;
        this.transactions = new ArrayList<>();

        if (openingBalance > 0) {
            transactions.add(
                    new Transaction("OPENING", openingBalance, balance)
            );
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Amount must be greater than zero.");
            return;
        }

        balance += amount;
        transactions.add(new Transaction("DEPOSIT", amount, balance));

        System.out.printf("Deposit successful! Balance: Rs. %.2f%n",
                          balance);
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Amount must be greater than zero.");
        } else if (amount > balance) {
            System.out.println("Insufficient balance!");
        } else {
            balance -= amount;
            transactions.add(new Transaction("WITHDRAW", amount, balance));

            System.out.printf("Withdrawal successful! Balance: Rs. %.2f%n",
                              balance);
        }
    }

    public void displayAccount() {
        System.out.println("\nAccount Number: " + accountNumber);
        System.out.println("Account Holder: " + ownerName);
        System.out.printf("Current Balance: Rs. %.2f%n", balance);
    }

    public void showTransactions() {
        System.out.println("\n===== TRANSACTION HISTORY =====");

        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        for (Transaction transaction : transactions) {
            transaction.display();
        }
    }
}

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<BankAccount> accounts = new ArrayList<>();

    private static String readText(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    private static double readAmount(String message) {
        while (true) {
            try {
                double amount = Double.parseDouble(readText(message));

                if (Double.isFinite(amount)) {
                    return amount;
                }

                System.out.println("Enter a valid finite number.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static BankAccount findAccount(String accountNumber) {
        for (BankAccount account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }

        return null;
    }

    private static void createAccount() {
        String number = readText("Enter account number: ");

        if (number.isEmpty() || findAccount(number) != null) {
            System.out.println("Account number is empty or already exists.");
            return;
        }

        String name = readText("Enter account holder name: ");

        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        double openingBalance = readAmount("Enter opening balance: Rs. ");

        if (openingBalance < 0) {
            System.out.println("Opening balance cannot be negative.");
            return;
        }

        accounts.add(new BankAccount(number, name, openingBalance));
        System.out.println("Account created successfully!");
    }

    private static BankAccount selectAccount() {
        String number = readText("Enter account number: ");
        BankAccount account = findAccount(number);

        if (account == null) {
            System.out.println("Account not found.");
        }

        return account;
    }

    private static void depositMoney() {
        BankAccount account = selectAccount();

        if (account != null) {
            account.deposit(readAmount("Enter deposit amount: Rs. "));
        }
    }

    private static void withdrawMoney() {
        BankAccount account = selectAccount();

        if (account != null) {
            account.withdraw(readAmount("Enter withdrawal amount: Rs. "));
        }
    }

    private static void viewAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }

        for (BankAccount account : accounts) {
            account.displayAccount();
        }
    }

    private static void showHistory() {
        BankAccount account = selectAccount();

        if (account != null) {
            account.showTransactions();
        }
    }

    public static void main(String[] args) {
        int choice;

        do {
            System.out.println("\n===== BANK TRANSACTION MANAGER =====");
            System.out.println("1. Create Account");
            System.out.println("2. View All Accounts");
            System.out.println("3. Deposit Money");
            System.out.println("4. Withdraw Money");
            System.out.println("5. Transaction History");
            System.out.println("6. Exit");

            try {
                choice = Integer.parseInt(readText("Enter choice: "));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid menu number.");
                continue;
            }

            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    viewAccounts();
                    break;
                case 3:
                    depositMoney();
                    break;
                case 4:
                    withdrawMoney();
                    break;
                case 5:
                    showHistory();
                    break;
                case 6:
                    System.out.println("Thank you for using the bank manager!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 6);

        scanner.close();
    }
}

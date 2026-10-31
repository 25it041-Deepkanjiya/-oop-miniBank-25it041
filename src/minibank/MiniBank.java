package minibank;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import minibank.exception.BankException;
import minibank.model.Account;
import minibank.model.CurrentAccount;
import minibank.model.Customer;
import minibank.model.FixedDepositAccount;
import minibank.model.SavingsAccount;
import minibank.model.Transaction;
import minibank.service.AccountWorker;
import minibank.service.BankService;
import minibank.util.AnnotationValidator;
import minibank.util.StatePersister;
import minibank.util.TransactionLog;

/**
 * Console-based MiniBank Application.
 * Object-Oriented Programming (Java) Laboratory Project
 * 
 * Student: Deep Kanjiya
 * Roll No: 25IT041
 */
public class MiniBank {
    private static final Scanner console = new Scanner(System.in);
    private static final BankService bankService = new BankService();

    public static void main(String[] args) {
        try {
            // Restore saved account state if available
            bankService.replaceAll(StatePersister.load());

            System.out.println("==================================================");
            System.out.println("            WELCOME TO MINIBANK SYSTEM            ");
            System.out.println("          Developed by: Deep Kanjiya (25IT041)    ");
            System.out.println("==================================================");

            boolean active = true;
            while (active) {
                renderMenu();
                int selectedOption = promptInt("Select an option (1-9): ");
                try {
                    switch (selectedOption) {
                        case 1 -> openNewAccount();
                        case 2 -> searchAccountDetails();
                        case 3 -> depositMoney();
                        case 4 -> withdrawMoney();
                        case 5 -> performTransfer();
                        case 6 -> displayAllAccounts();
                        case 7 -> displayStatement();
                        case 8 -> executeThreadingDemo();
                        case 9 -> {
                            persistBankState();
                            active = false;
                            System.out.println("\nSession closed. Thank you for using MiniBank!");
                        }
                        default -> System.out.println("[Warning] Please select a valid option from 1 to 9.");
                    }
                } catch (BankException | IllegalArgumentException ex) {
                    System.out.println("[Notice] " + ex.getMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println("[Initialization Error] Failed to load previous state: " + ex.getMessage());
        }
    }

    private static void renderMenu() {
        System.out.println("\n----------------- MAIN MENU -----------------");
        System.out.println("  1. Open New Bank Account");
        System.out.println("  2. View Account Details");
        System.out.println("  3. Deposit Money");
        System.out.println("  4. Withdraw Money");
        System.out.println("  5. Transfer Funds");
        System.out.println("  6. List All Accounts");
        System.out.println("  7. Transaction Statement");
        System.out.println("  8. Thread-Safety Demo (ExecutorService)");
        System.out.println("  9. Save & Exit");
        System.out.println("---------------------------------------------");
    }

    private static void openNewAccount() {
        System.out.println("\n--- Create Account ---");
        System.out.print("Customer Full Name: ");
        String fullName = console.nextLine().trim();

        System.out.print("Phone Number: ");
        String phone = console.nextLine().trim();

        System.out.print("Residential Address: ");
        String address = console.nextLine().trim();

        System.out.print("Desired Account Number: ");
        String accNum = console.nextLine().trim();

        System.out.println("Choose Account Type: [1] Savings | [2] Current | [3] Fixed Deposit");
        int typeChoice = promptInt("Enter choice (1-3): ");

        double initialDeposit = promptDouble("Initial Opening Balance (Rs.): ");
        if (initialDeposit < 0) {
            throw new IllegalArgumentException("Opening balance cannot be a negative amount.");
        }

        Customer customer = new Customer(fullName, phone, address);
        Account account = switch (typeChoice) {
            case 1 -> new SavingsAccount(accNum, customer, initialDeposit);
            case 2 -> new CurrentAccount(accNum, customer, initialDeposit);
            case 3 -> new FixedDepositAccount(accNum, customer, initialDeposit);
            default -> throw new IllegalArgumentException("Invalid account type selection.");
        };

        // Validate annotations on account and customer
        List<String> validationErrors = AnnotationValidator.validate(account);
        if (!validationErrors.isEmpty()) {
            throw new IllegalArgumentException(String.join(", ", validationErrors));
        }

        bankService.openAccount(account);
        System.out.println("[Success] Account #" + accNum + " created successfully for " + fullName + ".");
    }

    private static void searchAccountDetails() throws BankException {
        System.out.println("\n--- Account Inquiry ---");
        String accNum = promptString("Enter Account Number: ");
        Account account = bankService.findAccount(accNum);
        System.out.println("Account Info  : " + account);
        System.out.println("Customer Info : " + account.getCustomer());
    }

    private static void depositMoney() throws BankException {
        System.out.println("\n--- Deposit Funds ---");
        String accNum = promptString("Enter Account Number: ");
        double amount = promptDouble("Enter Amount to Deposit (Rs.): ");
        bankService.deposit(accNum, amount);
        System.out.println("[Success] Rs. " + amount + " deposited into account #" + accNum + ".");
    }

    private static void withdrawMoney() throws BankException {
        System.out.println("\n--- Withdraw Funds ---");
        String accNum = promptString("Enter Account Number: ");
        double amount = promptDouble("Enter Amount to Withdraw (Rs.): ");
        bankService.withdraw(accNum, amount);
        System.out.println("[Success] Rs. " + amount + " withdrawn from account #" + accNum + ".");
    }

    private static void performTransfer() throws BankException {
        System.out.println("\n--- Funds Transfer ---");
        String sourceAcc = promptString("Sender Account Number: ");
        String destinationAcc = promptString("Beneficiary Account Number: ");
        double amount = promptDouble("Transfer Amount (Rs.): ");
        bankService.transfer(sourceAcc, destinationAcc, amount);
        System.out.println("[Success] Successfully transferred Rs. " + amount + " from #" + sourceAcc + " to #" + destinationAcc + ".");
    }

    private static void displayAllAccounts() {
        System.out.println("\n--- Registered Accounts List ---");
        List<Account> accounts = bankService.getAllAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts currently registered.");
        } else {
            accounts.stream().sorted().forEach(System.out::println);
        }
    }

    private static void displayStatement() {
        System.out.println("\n--- Bank Transaction History ---");
        List<Transaction> transactions = bankService.getHistory();
        if (transactions.isEmpty()) {
            System.out.println("No transactions recorded yet.");
        } else {
            for (Transaction tx : transactions) {
                System.out.println(tx);
            }
        }
    }

    private static void executeThreadingDemo() throws BankException {
        System.out.println("\n--- Multi-Threading Synchronization Demonstration ---");
        List<Account> accounts = bankService.getAllAccounts();
        if (accounts.isEmpty()) {
            System.out.println("Please create an account first to test concurrent deposits.");
            return;
        }

        Account target = accounts.get(0);
        System.out.println("Testing account #" + target.getAccountNumber() + " | Current Balance: Rs. " + target.getBalance());
        System.out.println("Submitting 5 deposit jobs (Rs. 10 each) to a thread pool with 3 threads...");

        ExecutorService pool = Executors.newFixedThreadPool(3);
        for (int i = 1; i <= 5; i++) {
            pool.submit(new AccountWorker(target, 10.0));
        }

        pool.shutdown();
        try {
            if (pool.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("All threads finished processing.");
                System.out.println("Final Account Balance: Rs. " + target.getBalance());
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            System.out.println("Thread execution was interrupted.");
        }
    }

    private static void persistBankState() {
        try {
            StatePersister.save(bankService.getAllAccounts());
            TransactionLog.save(bankService.getHistory());
            System.out.println("[Storage] Accounts and transaction logs successfully saved to data/");
        } catch (IOException ex) {
            System.out.println("[Storage Error] Unable to persist data: " + ex.getMessage());
        }
    }

    private static String promptString(String prompt) {
        System.out.print(prompt);
        return console.nextLine().trim();
    }

    private static int promptInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(promptString(prompt));
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter an integer: ");
            }
        }
    }

    private static double promptDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(promptString(prompt));
            } catch (NumberFormatException e) {
                System.out.print("Invalid amount. Please enter a valid decimal number: ");
            }
        }
    }
}

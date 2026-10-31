package minibank.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import minibank.annotation.Id;
import minibank.annotation.Positive;
import minibank.exception.InsufficientFundsException;
import minibank.exception.InvalidAmountException;

/**
 * Abstract base class for all bank accounts.
 * Author: Deep Kanjiya (25IT041)
 */
public abstract class Account implements Serializable, Comparable<Account> {
    private static final long serialVersionUID = 1L;

    @Id
    private String accountNumber;
    private Customer customer;

    @Positive
    protected double balance;

    private List<String> statementEntries = new ArrayList<>();

    public Account(String accountNumber, Customer customer, double openingBalance) {
        this.accountNumber = accountNumber;
        this.customer = customer;
        this.balance = openingBalance;
        this.statementEntries.add("Account activated with initial deposit: Rs. " + openingBalance);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public synchronized double getBalance() {
        return balance;
    }

    public synchronized List<String> getTransactions() {
        return new ArrayList<>(statementEntries);
    }

    public synchronized void deposit(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than zero.");
        }
        balance += amount;
        statementEntries.add("Credit: Rs. " + amount + " | Current Balance: Rs. " + balance);
    }

    public synchronized void withdraw(double amount) throws InvalidAmountException, InsufficientFundsException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than zero.");
        }
        if (!canWithdraw(amount)) {
            throw new InsufficientFundsException("Insufficient funds or withdrawal not permissible for this account type.");
        }
        balance -= amount;
        statementEntries.add("Debit: Rs. " + amount + " | Current Balance: Rs. " + balance);
    }

    public abstract boolean canWithdraw(double amount);

    public abstract String getAccountType();

    public synchronized void addTransaction(String message) {
        statementEntries.add(message);
    }

    @Override
    public int compareTo(Account other) {
        return Double.compare(this.getBalance(), other.getBalance());
    }

    @Override
    public String toString() {
        return String.format("A/C: %-10s | %-15s | Holder: %-15s | Balance: Rs. %.2f",
                accountNumber, getAccountType(), customer.getName(), getBalance());
    }
}

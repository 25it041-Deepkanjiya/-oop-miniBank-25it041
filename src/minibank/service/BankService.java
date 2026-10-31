package minibank.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import minibank.exception.AccountNotFoundException;
import minibank.exception.BankException;
import minibank.exception.InvalidAmountException;
import minibank.model.Account;
import minibank.model.Transaction;
import minibank.repository.Repository;

/**
 * Service managing core banking operations and thread-safe account transfers.
 * Author: Deep Kanjiya (25IT041)
 */
public class BankService {
    private final Repository<Account> accountRepo = new Repository<>();
    private final List<Transaction> transactionHistory = Collections.synchronizedList(new ArrayList<>());

    public void openAccount(Account account) {
        if (accountRepo.findById(account.getAccountNumber()) != null) {
            throw new IllegalArgumentException("Account number '" + account.getAccountNumber() + "' is already registered.");
        }
        accountRepo.save(account);
        transactionHistory.add(new Transaction("Opened " + account.getAccountType() + " account #" + account.getAccountNumber()));
    }

    public Account findAccount(String number) throws AccountNotFoundException {
        Account account = accountRepo.findById(number);
        if (account == null) {
            throw new AccountNotFoundException("Account #" + number + " could not be found.");
        }
        return account;
    }

    public void deposit(String number, double amount) throws BankException {
        Account account = findAccount(number);
        account.deposit(amount);
        transactionHistory.add(new Transaction("Credited Rs. " + amount + " into #" + number));
    }

    public void withdraw(String number, double amount) throws BankException {
        Account account = findAccount(number);
        account.withdraw(amount);
        transactionHistory.add(new Transaction("Debited Rs. " + amount + " from #" + number));
    }

    public void transfer(String from, String to, double amount) throws BankException {
        if (from.equalsIgnoreCase(to)) {
            throw new InvalidAmountException("Sender and receiver accounts cannot be the same.");
        }

        Account source = findAccount(from);
        Account target = findAccount(to);

        // Lock accounts in a fixed global order to prevent deadlocks
        Account firstLock = from.compareTo(to) < 0 ? source : target;
        Account secondLock = firstLock == source ? target : source;

        synchronized (firstLock) {
            synchronized (secondLock) {
                source.withdraw(amount);
                try {
                    target.deposit(amount);
                } catch (BankException ex) {
                    // Compensate source balance if target deposit throws an exception
                    source.deposit(amount);
                    throw ex;
                }
            }
        }
        transactionHistory.add(new Transaction("Transferred Rs. " + amount + " from #" + from + " to #" + to));
    }

    public List<Account> getAllAccounts() {
        return accountRepo.findAll();
    }

    public List<Transaction> getHistory() {
        synchronized (transactionHistory) {
            return new ArrayList<>(transactionHistory);
        }
    }

    public int accountCount() {
        return accountRepo.size();
    }

    public void replaceAll(List<Account> loaded) {
        for (Account account : loaded) {
            accountRepo.save(account);
        }
    }
}

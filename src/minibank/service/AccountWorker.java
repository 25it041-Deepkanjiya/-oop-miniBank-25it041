package minibank.service;

import minibank.model.Account;

/**
 * Worker thread task for simulating concurrent deposit operations.
 * Author: Deep Kanjiya (25IT041)
 */
public class AccountWorker implements Runnable {
    private final Account account;
    private final double depositAmount;

    public AccountWorker(Account account, double depositAmount) {
        this.account = account;
        this.depositAmount = depositAmount;
    }

    @Override
    public void run() {
        try {
            account.deposit(depositAmount);
            System.out.println("[" + Thread.currentThread().getName() + "] credited Rs. " + depositAmount + " to A/C #" + account.getAccountNumber());
        } catch (Exception ex) {
            System.out.println("[" + Thread.currentThread().getName() + "] Error: " + ex.getMessage());
        }
    }
}

package minibank.model;

/**
 * Savings account implementation.
 * Author: Deep Kanjiya (25IT041)
 */
public class SavingsAccount extends Account {
    private static final long serialVersionUID = 1L;

    public SavingsAccount(String accountNumber, Customer customer, double openingBalance) {
        super(accountNumber, customer, openingBalance);
    }

    @Override
    public boolean canWithdraw(double amount) {
        // Savings accounts only permit withdrawals within existing balance
        return amount <= getBalance();
    }

    @Override
    public String getAccountType() {
        return "Savings";
    }
}

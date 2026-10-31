package minibank.model;

/**
 * Current account with overdraft facility.
 * Author: Deep Kanjiya (25IT041)
 */
public class CurrentAccount extends Account {
    private static final long serialVersionUID = 1L;
    private static final double OVERDRAFT_LIMIT = 5000.0;

    public CurrentAccount(String accountNumber, Customer customer, double openingBalance) {
        super(accountNumber, customer, openingBalance);
    }

    public static double getOverdraftLimit() {
        return OVERDRAFT_LIMIT;
    }

    @Override
    public boolean canWithdraw(double amount) {
        // Allows withdrawal with overdraft buffer
        return amount <= (getBalance() + OVERDRAFT_LIMIT);
    }

    @Override
    public String getAccountType() {
        return "Current";
    }
}

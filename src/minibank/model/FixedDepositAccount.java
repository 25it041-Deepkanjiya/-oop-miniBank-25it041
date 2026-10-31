package minibank.model;

/**
 * Fixed Deposit account where funds are locked until maturity.
 * Author: Deep Kanjiya (25IT041)
 */
public class FixedDepositAccount extends Account {
    private static final long serialVersionUID = 1L;

    public FixedDepositAccount(String accountNumber, Customer customer, double openingBalance) {
        super(accountNumber, customer, openingBalance);
    }

    @Override
    public boolean canWithdraw(double amount) {
        // Direct early withdrawals are prohibited
        return false;
    }

    @Override
    public String getAccountType() {
        return "Fixed Deposit";
    }
}

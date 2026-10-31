package minibank.exception;

/**
 * Thrown when an account has insufficient balance or overdraft for withdrawal.
 * Author: Deep Kanjiya (25IT041)
 */
public class InsufficientFundsException extends BankException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

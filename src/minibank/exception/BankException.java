package minibank.exception;

/**
 * Root exception for banking errors in MiniBank.
 * Author: Deep Kanjiya (25IT041)
 */
public class BankException extends Exception {
    public BankException(String message) {
        super(message);
    }
}

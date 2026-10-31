package minibank.exception;

/**
 * Thrown when an invalid transaction amount is supplied (e.g. <= 0).
 * Author: Deep Kanjiya (25IT041)
 */
public class InvalidAmountException extends BankException {
    public InvalidAmountException(String message) {
        super(message);
    }
}

package minibank.exception;

/**
 * Thrown when an account cannot be found in the repository.
 * Author: Deep Kanjiya (25IT041)
 */
public class AccountNotFoundException extends BankException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}

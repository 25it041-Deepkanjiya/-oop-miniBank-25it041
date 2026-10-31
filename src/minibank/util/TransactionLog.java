package minibank.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import minibank.model.Transaction;

/**
 * Appends transaction audit trails to a text file using Java NIO.
 * Author: Deep Kanjiya (25IT041)
 */
public class TransactionLog {
    private static final Path LOG_PATH = Paths.get("data", "transactions.txt");

    public static void save(List<Transaction> transactions) throws IOException {
        if (LOG_PATH.getParent() != null) {
            Files.createDirectories(LOG_PATH.getParent());
        }
        StringBuilder sb = new StringBuilder();
        for (Transaction transaction : transactions) {
            sb.append(transaction.toString()).append(System.lineSeparator());
        }
        Files.writeString(LOG_PATH, sb.toString(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}

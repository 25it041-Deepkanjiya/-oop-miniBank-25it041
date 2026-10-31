package minibank.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import minibank.model.Account;

/**
 * Handles binary serialization and deserialization of bank account state.
 * Author: Deep Kanjiya (25IT041)
 */
public class StatePersister {
    private static final Path STORAGE_PATH = Paths.get("data", "accounts.dat");

    public static void save(List<Account> accounts) throws IOException {
        if (STORAGE_PATH.getParent() != null) {
            Files.createDirectories(STORAGE_PATH.getParent());
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(STORAGE_PATH))) {
            oos.writeObject(new ArrayList<>(accounts));
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Account> load() throws IOException, ClassNotFoundException {
        if (!Files.exists(STORAGE_PATH)) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(STORAGE_PATH))) {
            return (List<Account>) ois.readObject();
        }
    }
}

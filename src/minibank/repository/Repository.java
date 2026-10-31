package minibank.repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import minibank.annotation.Id;

/**
 * Generic in-memory repository using reflection to identify @Id annotated keys.
 * Author: Deep Kanjiya (25IT041)
 */
public class Repository<T> {
    private final Map<String, T> storageMap = new ConcurrentHashMap<>();

    private String extractId(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Cannot extract ID from null entity.");
        }
        try {
            Class<?> currentClass = entity.getClass();
            while (currentClass != null) {
                for (Field field : currentClass.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Id.class)) {
                        field.setAccessible(true);
                        Object val = field.get(entity);
                        if (val == null) {
                            throw new IllegalArgumentException("Field annotated with @Id cannot be null.");
                        }
                        return String.valueOf(val);
                    }
                }
                currentClass = currentClass.getSuperclass();
            }
            throw new IllegalArgumentException("Entity class " + entity.getClass().getSimpleName() + " missing @Id field.");
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("Unable to read @Id property: " + ex.getMessage(), ex);
        }
    }

    public void save(T entity) {
        String key = extractId(entity);
        storageMap.put(key, entity);
    }

    public T findById(String id) {
        return storageMap.get(id);
    }

    public List<T> findAll() {
        return new ArrayList<>(storageMap.values());
    }

    public void delete(String id) {
        storageMap.remove(id);
    }

    @SafeVarargs
    public final void saveAll(T... entities) {
        for (T entity : entities) {
            save(entity);
        }
    }

    public int size() {
        return storageMap.size();
    }
}

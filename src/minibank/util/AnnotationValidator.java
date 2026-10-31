package minibank.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import minibank.annotation.MaxLength;
import minibank.annotation.Positive;

/**
 * Reflection utility to validate entity constraints via custom annotations.
 * Author: Deep Kanjiya (25IT041)
 */
public class AnnotationValidator {
    public static List<String> validate(Object object) {
        List<String> errors = new ArrayList<>();
        if (object == null) {
            return errors;
        }

        Class<?> currentClass = object.getClass();
        while (currentClass != null) {
            for (Field field : currentClass.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    Object value = field.get(object);

                    // Check @Positive annotation on numeric fields
                    if (field.isAnnotationPresent(Positive.class)) {
                        if (value instanceof Number num && num.doubleValue() < 0) {
                            errors.add(field.getName() + " cannot be negative (current: " + value + ")");
                        }
                    }

                    // Check @MaxLength annotation on strings
                    if (field.isAnnotationPresent(MaxLength.class)) {
                        if (value instanceof String str) {
                            int limit = field.getAnnotation(MaxLength.class).value();
                            if (str.length() > limit) {
                                errors.add(field.getName() + " exceeds maximum allowed length of " + limit + " characters");
                            }
                        }
                    }
                } catch (IllegalAccessException ex) {
                    errors.add("Could not inspect field: " + field.getName());
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        return errors;
    }
}

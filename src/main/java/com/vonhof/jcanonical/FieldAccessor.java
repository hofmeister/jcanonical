package com.vonhof.jcanonical;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class FieldAccessor<T,U> {

    private static final Logger logger = LoggerFactory.getLogger(FieldAccessor.class);

    private final T target;
    private final String path;

    protected FieldAccessor(T target, String path) {
        this.target = target;
        this.path = path;
    }

    public U get() {
        try {
            return getValue();
        } catch (NoSuchFieldException e) {
            logger.error("Field not found ", e);
        } catch (Exception e) {
            logger.error("Failed to access path ", e);
        }

        return null;
    }

    public T set(U value) {
        try {
            return setValue(value);
        } catch (Exception e) {
            logger.error("Failed to access path", e);
        }

        return null;
    }

    protected U getValue() throws NoSuchFieldException, IllegalAccessException {
        String[] parts = path.split("/");

        Object pointer = target;

        for(String part : parts) {
            String fieldName = part.substring(0,1).toLowerCase()+part.substring(1);
            Field field = pointer.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            pointer = field.get(pointer);
        }

        return (U) pointer;
    }

    protected T setValue(U value) throws NoSuchFieldException, IllegalAccessException {
        String[] parts = path.split("/");

        Object pointer = target;

        for(int i = 0 ; i < parts.length ; i++) {
            String part = parts[i];
            String fieldName = part.substring(0,1).toLowerCase()+part.substring(1);
            Field field = pointer.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            if (i == (parts.length-1)) {
                field.set(pointer, value);
            } else {
                pointer = field.get(pointer);
            }
        }

        return target;
    }
}

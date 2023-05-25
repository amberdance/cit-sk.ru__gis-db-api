package ru.hard2code.gisdbapi.exception;

import org.springframework.util.ClassUtils;

public class EntityNotFoundException extends RuntimeException {

    private static final String MESSAGE_DEFAULT = "Requested %s entity was " +
            "not found";

    private static final String MESSAGE_CONCRETE_ENTITY = "Unable to find " +
            "entity %s with id %d";


    public EntityNotFoundException() {
        super(String.format(MESSAGE_DEFAULT, ""));
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(Class<?> clazz) {
        super(String.format(MESSAGE_DEFAULT, ClassUtils.getShortName(clazz)));
    }

    public EntityNotFoundException(Class<?> clazz, long id) {
        super(String.format(MESSAGE_CONCRETE_ENTITY,
                ClassUtils.getShortName(clazz), id));
    }

}

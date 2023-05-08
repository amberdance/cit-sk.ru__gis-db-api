package ru.hard2code.gisdbapi.exception;

import ru.hard2code.gisdbapi.model.AbstractEntity;

public class EntityNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Cannot find entity %s with id %d";

    public EntityNotFoundException() {
        super("Requested entity was not found");
    }


    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(AbstractEntity abstractEntity) {
        super(String.format(MESSAGE, abstractEntity.getClass().getSimpleName(), abstractEntity.getId()));
    }

    public EntityNotFoundException(Class<? extends AbstractEntity> type, long id) {
        super(String.format(MESSAGE, type.getSimpleName(), id));
    }
}

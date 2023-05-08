package ru.hard2code.gisdbapi.exception;

import org.springframework.util.ClassUtils;
import ru.hard2code.gisdbapi.model.AbstractEntity;

public class EntityNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Unable to find entity %s with id %d";

    public EntityNotFoundException() {
        super("Requested entity was not found");
    }


    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(AbstractEntity abstractEntity) {
        super(String.format(MESSAGE, ClassUtils.getShortName(abstractEntity.getClass()), abstractEntity.getId()));
    }

    public EntityNotFoundException(Class<? extends AbstractEntity> clazz, long id) {
        super(String.format(MESSAGE, ClassUtils.getShortName(clazz), id));
    }
}

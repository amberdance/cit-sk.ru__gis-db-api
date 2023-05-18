package ru.hard2code.gisdbapi.util;

import jakarta.validation.ConstraintViolationException;

public class ValidationTemplate {
    private ValidationTemplate() {
        throw new UnsupportedOperationException();
    }

    public static String getValidationErrorString(ConstraintViolationException exception) {
        var validationErrorMessage = new StringBuilder("Validation failed for: ");

        exception.getConstraintViolations()
                .forEach(cs -> validationErrorMessage.append(cs.getPropertyPath())
                        .append("-")
                        .append(cs.getMessage())
                        .append(";"));

        validationErrorMessage.deleteCharAt(validationErrorMessage.length() - 1);

        return validationErrorMessage.toString();
    }
}

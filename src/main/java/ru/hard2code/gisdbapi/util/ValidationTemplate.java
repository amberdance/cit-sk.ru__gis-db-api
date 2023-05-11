package ru.hard2code.gisdbapi.util;

import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class ValidationTemplate {
    private ValidationTemplate() {
        throw new UnsupportedOperationException();
    }

    public static String getValidationErrorString(Set<ConstraintViolation<?>> constraintViolations) {
        var validationErrorMessage = new StringBuilder("Validation failed for: ");

        constraintViolations.forEach(cs -> validationErrorMessage.append(cs.getPropertyPath())
                .append("-")
                .append(cs.getMessage())
                .append(";"));

        validationErrorMessage.deleteCharAt(validationErrorMessage.length() - 1);

        return validationErrorMessage.toString();
    }
}

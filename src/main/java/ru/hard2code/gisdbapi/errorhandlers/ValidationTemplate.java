package ru.hard2code.gisdbapi.errorhandlers;

import jakarta.validation.ConstraintViolationException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationTemplate {


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

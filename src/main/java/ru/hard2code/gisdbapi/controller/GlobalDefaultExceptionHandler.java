package ru.hard2code.gisdbapi.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.util.RestErrorResponse;

import java.util.Map;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundError(EntityNotFoundException e, HttpServletRequest request) {
        return new RestErrorResponse(request, HttpStatus.NOT_FOUND).defaultError(e);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationError(ConstraintViolationException e, HttpServletRequest request) {
        return new RestErrorResponse(request, HttpStatus.BAD_REQUEST).validationError(e);
    }
}
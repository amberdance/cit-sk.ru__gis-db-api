package ru.hard2code.gisdbapi.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.util.RestErrorResponse;

import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalDefaultExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundError(EntityNotFoundException e, HttpServletRequest request) {
        log.error(e.getLocalizedMessage());
        return new RestErrorResponse(request, HttpStatus.NOT_FOUND).defaultError(e);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationError(ConstraintViolationException e, HttpServletRequest request) {
        log.error(e.getLocalizedMessage());
        return new RestErrorResponse(request, HttpStatus.BAD_REQUEST).validationError(e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleDefaultError(Exception e, HttpServletRequest request) {
        log.error(e.getLocalizedMessage());
        return new RestErrorResponse(request, HttpStatus.INTERNAL_SERVER_ERROR).defaultError(e);
    }
}

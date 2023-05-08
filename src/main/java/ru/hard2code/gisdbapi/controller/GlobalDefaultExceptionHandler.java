package ru.hard2code.gisdbapi.controller;


import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(EntityNotFoundException e) {
        return new ResponseEntity<>(new ErrorInfo(e, HttpStatus.NOT_FOUND).getError(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ConstraintViolationException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequest(Exception e) {
        return new ResponseEntity<>(new ErrorInfo(e, HttpStatus.BAD_REQUEST).getError(), HttpStatus.BAD_REQUEST);
    }


    private static class ErrorInfo {
        private final Map<String, Object> error = new LinkedHashMap<>();

        public ErrorInfo(Throwable e, HttpStatus httpStatus) {
            error.put("code", httpStatus.value());
            error.put("status", httpStatus.getReasonPhrase());
            error.put("message", e.getMessage());
        }

        public Map<String, Object> getError() {
            return error;
        }
    }
}
package ru.hard2code.gisdbapi.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class RestErrorResponse {

    private static final String ERROR_KEY = "error";
    private static final String MESSAGE_KEY = "message";
    private static final String PATH_KEY = "path";
    private static final String STATUS_KEY = "status";
    private static final String TIMESTAMP_KEY = "timestamp";
    private final HttpServletRequest request;
    private final HttpStatus httpStatus;


    private static final Map<String, Object> error = new LinkedHashMap<>() {{
        put(ERROR_KEY, null);
        put(MESSAGE_KEY, null);
        put(PATH_KEY, null);
        put(STATUS_KEY, null);
        put(TIMESTAMP_KEY, LocalDateTime.now());
    }};

    public RestErrorResponse(HttpServletRequest request, HttpStatus httpStatus) {
        this.request = request;
        this.httpStatus = httpStatus;
    }

    public ResponseEntity<Map<String, Object>> defaultError(Throwable ex) {
        error.put(MESSAGE_KEY, ex.getLocalizedMessage());
        fillRequiredFields();

        return new ResponseEntity<>(error, httpStatus);
    }

    public ResponseEntity<Map<String, Object>> validationError(ConstraintViolationException ex) {
        error.put(MESSAGE_KEY, ValidationTemplate.getValidationErrorString(ex.getConstraintViolations()));
        fillRequiredFields();

        return new ResponseEntity<>(error, httpStatus);
    }


    private void fillRequiredFields() {
        error.put(ERROR_KEY, httpStatus.getReasonPhrase());
        error.put(PATH_KEY, request.getRequestURI());
        error.put(STATUS_KEY, httpStatus.value());
    }


}
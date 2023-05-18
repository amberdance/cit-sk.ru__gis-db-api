package ru.hard2code.gisdbapi.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.hard2code.gisdbapi.errorhandlers.RestErrorResponseHandler;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.exception.ForbiddenException;

import java.util.Map;

@ControllerAdvice
public class GlobalDefaultExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundError(EntityNotFoundException e, HttpServletRequest request) {
        return new RestErrorResponseHandler(request, HttpStatus.NOT_FOUND).defaultError(e);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationError(ConstraintViolationException e, HttpServletRequest request) {
        return new RestErrorResponseHandler(request, HttpStatus.BAD_REQUEST).validationError(e);
    }

    @ExceptionHandler({AccessDeniedException.class, ForbiddenException.class})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleForbiddenError(AccessDeniedException e,
                                                                    HttpServletRequest request) {
        return new RestErrorResponseHandler(request, HttpStatus.FORBIDDEN).defaultError(e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleDefaultError(Exception e, HttpServletRequest request) {
        logger.error(e.getLocalizedMessage());
        return new RestErrorResponseHandler(request,
                HttpStatus.INTERNAL_SERVER_ERROR).defaultError(e);
    }
}

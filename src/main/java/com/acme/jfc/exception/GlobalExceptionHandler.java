package com.acme.jfc.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

//@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException exception,
            WebRequest request) {
        //log.error("Failed to find the requested element", itemNotFoundException);
        return buildErrorResponse(exception, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException exception,
            WebRequest request) {
        //log.error("Failed: ", argumentException);
        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException exception,
            WebRequest request) {
        //log.error("Failed: ", argumentException);
        //return buildErrorResponse(exception, HttpStatus.BAD_REQUEST, request);
        return buildErrorResponse(exception, exception.getLocalizedMessage(),HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
            WebRequest request) {
        //log.error("Failed: ", argumentException);
        StringBuilder msg = new StringBuilder("Fallo de validación detectado. Compruebe los valores introducidos: ");
        msg.append("[Field=").append(exception.getFieldError().getField())
            .append("] ")
            .append("[RejectedValue=").append(exception.getFieldError().getRejectedValue())
            .append("] ")
            .append("[Error=").append(exception.getFieldError().getDefaultMessage())
            .append("]");
        return buildErrorResponse(exception, msg.toString(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception,
            WebRequest request) {
        //log.error("Failed set: ", argumentException);
        return buildErrorResponse(exception, "JSON parse error: Cannot deserialize one of the values (not accepted).", HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(Exception exception, WebRequest request) {
        //log.error("Unknown error occurred", exception);
        return buildErrorResponse(exception, "Unknown error occurred.", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception exception,
            HttpStatus httpStatus,
            WebRequest request) {
        return buildErrorResponse(exception, exception.getMessage(), httpStatus, request);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception exception,
            String message,
            HttpStatus httpStatus,
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    public ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        return buildErrorResponse(ex, status, request);
    }

}

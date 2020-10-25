package learn.field_agent.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.MethodNotAllowedException;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleException(DataIntegrityViolationException ex) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse("Data integrity error, please check your request and try again."),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) throws Exception {
        if (ex instanceof HttpMediaTypeNotSupportedException) {
            throw ex;
        }

        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse("Something went wrong on our end and your request failed. We're so sorry. :("),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

package com.nam.advice;

import com.nam.exception.MessageException;
import com.nam.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorDetail> UserExceptionHandle(UserException e, WebRequest req) {
        ErrorDetail errorDetail = new ErrorDetail(e.getMessage(), req.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessageException.class)
    public ResponseEntity<ErrorDetail> MessageExceptionHandle(MessageException e, WebRequest req) {
        ErrorDetail errorDetail = new ErrorDetail(e.getMessage(), req.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetail> MethodArgumentNotValidExceptionHandle(MethodArgumentNotValidException e, WebRequest req) {
        String error = e.getBindingResult().getFieldError().getDefaultMessage();
        ErrorDetail errorDetail = new ErrorDetail("Validation Error", error, LocalDateTime.now());
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDetail> NoHandlerFoundException(NoHandlerFoundException e, WebRequest req) {
        ErrorDetail errorDetail = new ErrorDetail("Endpoint not found", e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetail> OtherExceptionHandler(Exception e, WebRequest req) {
        ErrorDetail errorDetail = new ErrorDetail(e.getMessage(), req.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

}

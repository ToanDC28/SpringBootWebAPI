package com.example.ClassRepo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ClassDoesNotExistException.class)
    public ResponseEntity<?> handleAccountPermissionException(ClassDoesNotExistException exception,
                                                             WebRequest request){
        ErrorDetail error= new ErrorDetail(new Date(), exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity(error, HttpStatus.NOT_ACCEPTABLE);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exception,
                                                              WebRequest request){
        ErrorDetail error= new ErrorDetail(new Date(), exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity(error, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException exception,
                                                WebRequest request){
        ErrorDetail error= new ErrorDetail(new Date(), exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(SlotDoesNotExistException.class)
    public ResponseEntity<?> handleInvalidCredentialException(SlotDoesNotExistException exception,
                                                WebRequest request){
        ErrorDetail error= new ErrorDetail(new Date(), exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                              WebRequest request){
        ErrorDetail error= new ErrorDetail(new Date(), exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }



}

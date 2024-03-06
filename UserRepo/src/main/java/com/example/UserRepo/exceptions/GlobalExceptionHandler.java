package com.example.UserRepo.exceptions;

import com.example.UserRepo.entity.Token;
import com.example.UserRepo.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccountPermissionException.class)
    public ResponseEntity<?> handleAccountPermissionException(AccountPermissionException exception,
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
        return new ResponseEntity(error, HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException exception,
                                                WebRequest request){
        ErrorDetail error= new ErrorDetail(new Date(), exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<?> handleInvalidCredentialException(InvalidCredentialsException exception,
                                                WebRequest request){
        ErrorDetail error= new ErrorDetail(new Date(), exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<?> handleTokenExpiredxception(TokenExpiredException exception,
                                                WebRequest request){
        ErrorDetail error= new ErrorDetail(new Date(), exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity(error, HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserDoesNotExistException exception,
                                                WebRequest request){
        ErrorDetail error= new ErrorDetail(new Date(), exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<?> handleDateTimeException(DateTimeException exception,
                                                         WebRequest request){
        ErrorDetail error= new ErrorDetail(new Date(), exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                     WebRequest request){
        ErrorDetail error= new ErrorDetail(new Date(), exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ValidateCodeFailException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ValidateCodeFailException exception,
                                                             WebRequest request){
        ErrorDetail error= new ErrorDetail(new Date(), exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }


}

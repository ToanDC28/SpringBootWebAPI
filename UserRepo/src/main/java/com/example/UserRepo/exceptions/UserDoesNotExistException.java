package com.example.UserRepo.exceptions;

public class UserDoesNotExistException extends Exception{
    @Override
    public String getMessage() {
        return "User does not exist";
    }

    public UserDoesNotExistException(String message) {
        super(message);
    }

    public UserDoesNotExistException() {
    }
}

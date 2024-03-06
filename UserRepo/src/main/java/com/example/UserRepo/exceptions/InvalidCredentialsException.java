package com.example.UserRepo.exceptions;

public class InvalidCredentialsException extends Exception{
    @Override
    public String getMessage() {
        return "Wrong username or password";
    }
}

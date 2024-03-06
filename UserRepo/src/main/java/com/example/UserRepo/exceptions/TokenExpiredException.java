package com.example.UserRepo.exceptions;

public class TokenExpiredException extends Exception {
    @Override
    public String getMessage() {
        return "Token is unexpired.";
    }
}

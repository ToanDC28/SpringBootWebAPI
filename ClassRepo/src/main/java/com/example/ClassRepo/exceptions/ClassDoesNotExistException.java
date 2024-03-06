package com.example.ClassRepo.exceptions;

public class ClassDoesNotExistException extends Exception{

    @Override
    public String getMessage() {
        return "Class does not exist";
    }

}

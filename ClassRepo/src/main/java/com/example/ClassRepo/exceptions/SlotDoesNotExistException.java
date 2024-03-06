package com.example.ClassRepo.exceptions;

public class SlotDoesNotExistException extends Exception{

    @Override
    public String getMessage() {
        return "Slot does not exist";
    }
}

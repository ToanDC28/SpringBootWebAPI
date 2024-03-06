package com.fams.syllabus_repository.exceptions;

public class TrainingContentNotFoundException extends RuntimeException{
    public TrainingContentNotFoundException(String message){
        super(message);
    }
}

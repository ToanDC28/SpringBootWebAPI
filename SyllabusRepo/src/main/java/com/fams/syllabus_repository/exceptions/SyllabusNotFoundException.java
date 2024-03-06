package com.fams.syllabus_repository.exceptions;

public class SyllabusNotFoundException extends RuntimeException{
    public SyllabusNotFoundException(String message){
        super(message);
    }
}

package com.fams.syllabus_repository.exceptions;

public class MaterialNotFoundException extends RuntimeException{
    public MaterialNotFoundException(String message){
        super(message);
    }
}

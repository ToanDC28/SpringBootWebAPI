package com.fams.syllabus_repository.exceptions;

import lombok.Data;

@Data
public class ResourceNotFoundException extends Exception{

    public ResourceNotFoundException(String message){
        super(message);
    }
}

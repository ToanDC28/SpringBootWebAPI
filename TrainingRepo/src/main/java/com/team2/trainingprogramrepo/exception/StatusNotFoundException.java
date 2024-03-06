package com.team2.trainingprogramrepo.exception;

import java.util.Date;

public class StatusNotFoundException extends RuntimeException{
    public StatusNotFoundException(String message) {
        super(message);
    }
}

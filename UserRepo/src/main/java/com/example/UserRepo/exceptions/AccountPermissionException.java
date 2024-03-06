package com.example.UserRepo.exceptions;

import javax.security.auth.login.AccountException;

public class AccountPermissionException extends AccountException {
    public AccountPermissionException(String message){
        super(message);
    }
}

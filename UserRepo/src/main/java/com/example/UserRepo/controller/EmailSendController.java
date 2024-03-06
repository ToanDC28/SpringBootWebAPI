package com.example.UserRepo.controller;

import com.example.UserRepo.exceptions.UserDoesNotExistException;
import com.example.UserRepo.exceptions.ValidateCodeFailException;
import com.example.UserRepo.reponses.ResponseObject;
import com.example.UserRepo.request.ChangePasswordByMailRequest;
import com.example.UserRepo.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.SendFailedException;

@RestController
@RequestMapping("api/user/mail")
public class EmailSendController {

    @Autowired
    private SendMailService mailService;

    @PostMapping("/send")
    public ResponseEntity<ResponseObject> sendMailToUser(@RequestBody ChangePasswordByMailRequest request) throws UserDoesNotExistException, SendFailedException {
        mailService.sendMailToUser(request);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .code(HttpStatus.OK.value())
                        .message("success")
                        .build()
        );

    }
    @PostMapping("/validation")
    public ResponseEntity<ResponseObject> validationCode(@RequestBody ChangePasswordByMailRequest request) throws UserDoesNotExistException, SendFailedException, ValidateCodeFailException {
        mailService.validateCode(request);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .code(HttpStatus.OK.value())
                        .message("success")
                        .build()
        );

    }

}

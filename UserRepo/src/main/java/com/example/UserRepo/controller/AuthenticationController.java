package com.example.UserRepo.controller;

import com.example.UserRepo.service.*;
import com.example.UserRepo.request.RegisterRequest;
import com.example.UserRepo.entity.User;
import com.example.UserRepo.exceptions.InvalidCredentialsException;
import com.example.UserRepo.exceptions.ResourceNotFoundException;
import com.example.UserRepo.exceptions.UserDoesNotExistException;
import com.example.UserRepo.reponses.ResponseObject;
import com.example.UserRepo.repository.UserRepository;
import com.example.UserRepo.request.AuthenticationRequest;
import com.example.UserRepo.request.refreshRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    private final JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private SendMailService sendMailService;

    private static Logger logger = LogManager.getLogger(AuthenticationController.class);
    @PostMapping("/register")
    public ResponseEntity<ResponseObject> register(
            @RequestBody RegisterRequest request, @RequestHeader String username
    ) throws Exception {
        userService.validateUser(username, "user");
        userService.isRegisterFormatValid(request);
        if (userRepository.existsUserByEmail(request.getEmail())) {
            logger.error("User with " + request.getEmail() + " is available : register fail");
            throw new Exception("User with " + request.getEmail() + " is available");
        }
        String pass = userService.passwordGenerate();
        User newUser = authenticationService.register(request, pass);
        sendMailService.sendPasswordAndUsernameToUser(request, pass);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .code(HttpStatus.OK.value())
                        .message("success")
                        .data(newUser)
                        .build()
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseObject> refreshToken(@RequestBody refreshRequest request, @RequestHeader String username) {
        ResponseObject responseObject = new ResponseObject();
        userService.validateUser(username, "");
        try {
            if (request != null) {
                responseObject.setCode(HttpStatus.OK.value());
                responseObject.setMessage("Token has been refreshed");
                responseObject.setData(jwtService.refreshToken(request.getToken()));
                logger.info("Token of " + username + " has been refreshed");
                return ResponseEntity.ok(responseObject);
            }
            logger.error("Token " + username + " is not existed in request");
            responseObject.setCode(HttpStatus.UNAUTHORIZED.value());
            responseObject.setMessage("Token is not existed in request");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(responseObject);

        } catch (Exception e) {
            logger.error(e);
            responseObject.setCode(HttpStatus.UNAUTHORIZED.value());
            responseObject.setMessage(e.getMessage());
            responseObject.setData(null);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(responseObject);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> signIn(@RequestBody AuthenticationRequest request) {
        try {
            String t = tokenService.getTokenForUser(
                    authenticationService.login(request.getEmail(), request.getPassword())
            );
            logger.info(request.getEmail() + " login to server");
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .code(HttpStatus.OK.value())
                            .message("success")
                            .data(t)
                            .build()
            );
        } catch (UserDoesNotExistException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ResponseObject.builder()
                            .code(HttpStatus.UNAUTHORIZED.value())
                            .message(e.getMessage())
                            .build()
            );
        } catch (InvalidCredentialsException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ResponseObject.builder()
                            .code(HttpStatus.UNAUTHORIZED.value())
                            .message(e.getMessage())
                            .build()
            );
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(
                    ResponseObject.builder()
                            .code(HttpStatus.GATEWAY_TIMEOUT.value())
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @DeleteMapping("/logout")
    public ResponseEntity<ResponseObject> signOut(@RequestBody refreshRequest request, @RequestHeader String username) {
        userService.validateUser(username, "");
        if(tokenService.checkExistToken(request.getToken())){
            tokenService.deleteToken(request.getToken());
            logger.info("User " + username + " logout successfully");
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .code(HttpStatus.OK.value())
                            .message("Log out successfully")
                            .build()
            );
        }else {
            logger.error("Token" + request.getToken() + " is not valid.");
            throw new ResourceNotFoundException("Token is not valid");
        }
    }
}
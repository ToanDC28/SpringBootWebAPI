package com.example.UserRepo.service;

import com.example.UserRepo.entity.ResetPassCode;
import com.example.UserRepo.entity.SendMailDetail;
import com.example.UserRepo.entity.User;
import com.example.UserRepo.enums.Action;
import com.example.UserRepo.enums.Status;
import com.example.UserRepo.exceptions.BadRequestException;
import com.example.UserRepo.exceptions.ResourceNotFoundException;
import com.example.UserRepo.exceptions.ValidateCodeFailException;
import com.example.UserRepo.repository.ResetPassRepository;
import com.example.UserRepo.repository.SendMailInfoRepository;
import com.example.UserRepo.repository.UserRepository;
import com.example.UserRepo.request.ChangePasswordByMailRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class ResetPassService {
    @Autowired
    private ResetPassRepository resetPassRepository;
    private static Logger logger = LogManager.getLogger(ResetPassService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private SendMailInfoRepository sendMailInfoRepository;

    private final PasswordEncoder passwordEncoder;
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> taskFuture;

    public ResetPassCode saveResetCode(String code, String email) {
        User user = userRepository.findByEmail(email).get();
        LocalTime currentTime = LocalTime.now();
        LocalTime endTime = currentTime.plusMinutes(5);

        Time startTime = Time.valueOf(currentTime);
        Time endTimeValue = Time.valueOf(endTime);
        ResetPassCode resetPassCode = ResetPassCode.builder()
                .email(user.getEmail())
                .code(code)
                .startTime(startTime)
                .endTime(endTimeValue)
                .status(true)
                .build();
        sendMailInfoRepository.save(SendMailDetail.builder()
                .email(email)
                .data(code)
                .action(Action.RESET_PASSWORD)
                .status(Status.WAITING)
                .date(LocalDate.now())
                .build());
        scheduler = Executors.newSingleThreadScheduledExecutor();
        long delay = calculateDelay(currentTime, endTime);
        taskFuture = scheduler.schedule(() -> closeCode(email, code), delay, TimeUnit.MILLISECONDS);
        return resetPassRepository.save(resetPassCode);
    }

    private long calculateDelay(LocalTime currentTime, LocalTime endTime) {
        long currentMillis = currentTime.toNanoOfDay() / 1000000;
        long endMillis = endTime.toNanoOfDay() / 1000000;
        long delay = endMillis - currentMillis;
        return delay;
    }

    public void closeCode(String email, String code) {
        ResetPassCode passcode = resetPassRepository.findByCode(code).get();
        if (passcode != null) {
            SendMailDetail mailDetail = sendMailInfoRepository.findSendMailDetailByEmailAndData(email, code);
            if (mailDetail.getStatus().equals(Status.WAITING)) {
                mailDetail.setStatus(Status.FAILED);
                sendMailInfoRepository.save(mailDetail);
            }
            passcode.setStatus(false);
            resetPassRepository.save(passcode);
        }
        cancelScheduledTask();
    }

    public void cancelScheduledTask() {
        logger.info("End Schedule of resetting password");
        if (taskFuture != null) {
            taskFuture.cancel(false);
        }
    }

    public void validateCode(ChangePasswordByMailRequest request) throws ValidateCodeFailException {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                ()-> new ResourceNotFoundException("User " + request.getEmail() + " is not exist.")
        );
        ResetPassCode code = resetPassRepository.findByCode(request.getCode()).orElseThrow(
                ()-> new ResourceNotFoundException("The code is not exist.")
        );
        if (user.getEmail().equals(request.getEmail()) && code.getEmail().equals(user.getEmail())) {
            if (code.getId() != null) {
                if (!code.isStatus()) {
                    logger.error("The valid time of code " + code + " is over time now.");
                    throw new ValidateCodeFailException("The valid time of code is over time now.");
                }
                if (!userService.validatePassword(request.getPassword())) {
                    throw new BadRequestException("Format password is not correct");
                }
                code.setStatus(false);
                resetPassRepository.save(code);
                cancelScheduledTask();
                SendMailDetail mailDetail = sendMailInfoRepository.findSendMailDetailByEmailAndData(request.getEmail(), request.getCode());
                if (mailDetail != null) {
                    mailDetail.setStatus(Status.SUCCESS);
                    sendMailInfoRepository.save(mailDetail);
                }
            }
            logger.info("Start reset password for user " + request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(user);
        } else {
            logger.error("User email " + request.getEmail() + " does not match.");
            throw new ValidateCodeFailException("User email does not match.");
        }
    }
}

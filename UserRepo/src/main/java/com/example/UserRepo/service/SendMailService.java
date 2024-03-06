package com.example.UserRepo.service;

import com.example.UserRepo.controller.AuthenticationController;
import com.example.UserRepo.entity.ResetPassCode;
import com.example.UserRepo.entity.SendMailDetail;
import com.example.UserRepo.entity.User;
import com.example.UserRepo.enums.Action;
import com.example.UserRepo.enums.Status;
import com.example.UserRepo.exceptions.UserDoesNotExistException;
import com.example.UserRepo.exceptions.ValidateCodeFailException;
import com.example.UserRepo.repository.SendMailInfoRepository;
import com.example.UserRepo.repository.UserRepository;
import com.example.UserRepo.request.ChangePasswordByMailRequest;
import com.example.UserRepo.request.RegisterRequest;
import net.bytebuddy.utility.RandomString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.SendFailedException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;

@Service
public class SendMailService {

    private static Logger logger = LogManager.getLogger(AuthenticationController.class);
    @Autowired
    private UserRepository userRepository;
    @Value("${spring.mail.username}")
    private String fromEmail;
    @Autowired
    private JavaMailSender sender;
    @Autowired
    private ResetPassService resetPassService;
    @Autowired
    private SendMailInfoRepository sendMailInfoRepository;

    public void sendMailToUser(ChangePasswordByMailRequest request) throws UserDoesNotExistException, SendFailedException {
        if (!userRepository.existsUserByEmail(request.getEmail()) || request.getEmail() == null) {
            logger.error("User" + request.getEmail() + " are not existing: can not send mail.");
            throw new UserDoesNotExistException("User" + request.getEmail() + " are not existing");
        }
        String code = null;
        try {
            MimeMessage mimeMessage = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(fromEmail);
            helper.setTo(request.getEmail());
            code = RandomString.make(8);
            helper.setSubject("Mail Reset Password");
            helper.setText("Hi, "
                    + "\nFAMS received your reset password request.\n"
                    + "Here is the code for authorization: \n"
                    + "• Code: " + code
                    + "\nThe code will close in 5 minute."

            );
            sender.send(mimeMessage);

            resetPassService.saveResetCode(code, request.getEmail());

        } catch (Exception e) {
            logger.error(e);
            sendMailInfoRepository.save(SendMailDetail.builder()
                    .email(request.getEmail())
                    .data(code)
                    .action(Action.RESET_PASSWORD)
                    .status(Status.FAILED)
                    .date(LocalDate.now())
                    .build());
            throw new SendFailedException();
        }

    }

    public void validateCode(ChangePasswordByMailRequest request) throws ValidateCodeFailException {
        resetPassService.validateCode(request);
    }

    public void sendPasswordAndUsernameToUser(RegisterRequest request, String password) throws SendFailedException {
        try {
            MimeMessage mimeMessage = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(fromEmail);
            helper.setTo(request.getEmail());
            helper.setSubject("[FAMS] - The account was created successfully.");
            helper.setText("Hi " + request.getName() + ",\n"
                    + "\nYour account FAMS was created successfully to login our system\n"
                    + "Please login to system follow by: \n"
                    + "• Username: " + request.getEmail()
                    + "\n"
                    + "• Password: " + password
                    + "\n\nPlease change your password when login at first-time for safe."

            );
            sender.send(mimeMessage);
            sendMailInfoRepository.save(SendMailDetail.builder()
                    .email(request.getEmail())
                    .data(null)
                    .action(Action.SEND_PASSWORD_REGISTER)
                    .status(Status.WAITING)
                    .date(LocalDate.now())
                    .build());
        } catch (Exception e) {
            logger.error(e);
            sendMailInfoRepository.save(SendMailDetail.builder()
                    .email(request.getEmail())
                    .data(null)
                    .action(Action.SEND_PASSWORD_REGISTER)
                    .status(Status.FAILED)
                    .date(LocalDate.now())
                    .build());
            throw new SendFailedException();
        }
    }
}

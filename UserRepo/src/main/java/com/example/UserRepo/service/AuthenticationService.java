package com.example.UserRepo.service;

import com.example.UserRepo.entity.Role;
import com.example.UserRepo.entity.SendMailDetail;
import com.example.UserRepo.entity.User;
import com.example.UserRepo.enums.Action;
import com.example.UserRepo.enums.Gender;
import com.example.UserRepo.enums.Status;
import com.example.UserRepo.enums.UserRole;
import com.example.UserRepo.exceptions.*;
import com.example.UserRepo.repository.SendMailInfoRepository;
import com.example.UserRepo.repository.UserRepository;
import com.example.UserRepo.request.RegisterRequest;
import com.example.UserRepo.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final Properties properties;
    private final UserService userService;
    @Autowired
    private SendMailInfoRepository sendMailInfoRepository;

    public User register(RegisterRequest request, String pass) {
        Gender gen = userService.getGender(request.getGender());
        UserRole role = userService.getUserRole(request.getRole());
        Role userRole = roleService.getRoleByRoleName(role);
        SimpleDateFormat sdf = new SimpleDateFormat(properties.getProperty("date"));
        Date dob = new Date();
        Date currentDate = new Date();
        Date createAt = null;
        try {
            dob = sdf.parse(request.getDob());
            String formattedDate = sdf.format(currentDate);
            createAt = sdf.parse(formattedDate);
            if(dob.after(createAt)){
                throw new DateTimeException("The Date of Birth is not valid.");
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        var user = User.builder()
                .name(request.getName())
                .avatar("")
                .password(passwordEncoder.encode(pass))
                .email(request.getEmail())
                .phone(request.getPhone())
                .dob(dob)
                .gender(gen)
                .createdBy(request.getCreatedBy())
                .createdAt(createAt)
                .role(userRole)
                .status(Status.NEW)
                .build();
        return repository.save(user);
    }
    public User login(String email, String password) throws UserDoesNotExistException, InvalidCredentialsException, AccountPermissionException {
        User userFetched = repository.findByEmail(email)
                .orElseThrow(UserDoesNotExistException::new);

        if (!passwordEncoder.matches(password, userFetched.getPassword())) {
            throw new InvalidCredentialsException();
        }
        if(userFetched.getStatus().equals(Status.DEACTIVE)){
            throw new AccountPermissionException("The Account is banned");
        }
        if (userFetched.getStatus().equals(Status.NEW)){
            SendMailDetail mailDetail = sendMailInfoRepository.findSendMailDetailByEmailAndAction(email, Action.SEND_PASSWORD_REGISTER);
            if(mailDetail != null){
                mailDetail.setStatus(Status.SUCCESS);
                sendMailInfoRepository.save(mailDetail);
            }
            userFetched.setStatus(Status.ACTIVE);
            repository.save(userFetched);
        }

        return userFetched;
    }
}

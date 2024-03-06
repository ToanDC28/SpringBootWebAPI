package com.example.UserRepo.service;

import com.example.UserRepo.config.SecurityEscape;
import com.example.UserRepo.entity.SendMailDetail;
import com.example.UserRepo.enums.*;
import com.example.UserRepo.exceptions.*;
import com.example.UserRepo.repository.SendMailInfoRepository;
import com.example.UserRepo.request.*;
import com.example.UserRepo.dto.PageRequestDto;
import com.example.UserRepo.dto.UserDto;
import com.example.UserRepo.entity.Role;
import com.example.UserRepo.entity.User;
import com.example.UserRepo.reponses.UserProfileResponse;
import com.example.UserRepo.repository.RoleRepository;
import com.example.UserRepo.repository.UserRepository;
import com.example.UserRepo.s3.S3Bucket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static Logger logger = LogManager.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private S3Sevice s3Sevice;
    @Autowired
    private S3Bucket s3Bucket;
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private Properties properties;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SendMailInfoRepository sendMailInfoRepository;

    public User updateUser(UserUpdateRequest user){
        SecurityEscape.checkUpdateRequest(user);
        User existedUser = userRepository.findById(user.getId()).orElseThrow(
                ()-> new ResourceNotFoundException("User is not found")
        );
        if (user.getName() != "") {
            existedUser.setName(user.getName());
        }
        if (user.getPhone() != "") {
            if (!user.getPhone().startsWith("0") || user.getPhone().length() < 10 || user.getPhone().length() > 12) {
                throw new BadRequestException("Phone number is not valid");
            }
            existedUser.setPhone(user.getPhone());
        }
        if (user.getDob() != "") {
            SimpleDateFormat sdf = new SimpleDateFormat(properties.getProperty("date"));
            try {
                Date currentDate = new Date();
                String formattedDate = sdf.format(currentDate);
                currentDate = sdf.parse(formattedDate);
                Date dob = sdf.parse(user.getDob());
                if(dob.after(currentDate)){
                    throw new BadRequestException("The Date of birth is not valid");
                }
                existedUser.setDob(dob);
            } catch (ParseException e) {
                logger.error(e + ": can not parse Date.");
                throw new RuntimeException(e);
            }
        }
        if (user.getGender() != "") {
            Gender gen = getGender(user.getGender());
            existedUser.setGender(gen);
        }

        return userRepository.save(existedUser);
    }

    public Gender getGender(String gender){
        if(gender.equalsIgnoreCase("male")){
            return Gender.MALE;
        } else if (gender.equalsIgnoreCase("female")) {
            return Gender.FEMALE;
        }else {
            throw new BadRequestException("Gender is not found");
        }
    }

    public UserRole getUserRole(String role){
        if(role.equalsIgnoreCase(UserRole.TRAINER.name())){
            return UserRole.TRAINER;
        } else if (role.equalsIgnoreCase(UserRole.TRAINEE.name())) {
            return UserRole.TRAINEE;
        } else if (role.equalsIgnoreCase(UserRole.ADMIN.name())) {
            return UserRole.ADMIN;
        } else if (role.equalsIgnoreCase(UserRole.SUPER_ADMIN.name())) {
            throw new BadRequestException("Super Admin role is unique");
        }else {
            throw new BadRequestException("Role name is not found");
        }
    }

    public boolean validatePassword(String password) {
        String format = properties.getProperty("pass");
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }


    public void validateUser(String username, String options) {
        User user = userRepository.findByEmail(username).orElseThrow(
                () -> new ResourceNotFoundException("User " + username + " is not found")
        );
        switch (options) {
            case "user":
                validateUserPermission(user);
                break;
            case "syllabus":
                validateSyllabusPermission(user);
                break;
            case "class":
                validateClassPermission(user);
                break;
            case "training":
                validateTrainingPermission(user);
                break;
            default:
                break;
        }
    }

    private void validateSyllabusPermission(User user) {
        if (user.getRole().getPermission().getSyllabus().equals(UserPermission.VIEW)
                || user.getRole().getPermission().getSyllabus().equals(UserPermission.ACCESS_DENIED)
        ) {
            throw new BadRequestException("User " + user.getEmail() + " does not have permission");
        }
    }

    private void validateUserPermission(User user) {
        if (!user.getRole().getRoleName().equals(UserRole.SUPER_ADMIN)) {
            if (user.getRole().getPermission().getUserManagement().equals(UserPermission.VIEW)
                    || user.getRole().getPermission().getUserManagement().equals(UserPermission.ACCESS_DENIED)
            ) {
                throw new BadRequestException("User " + user.getEmail() + " does not have permission");
            }
        }
    }

    private void validateClassPermission(User user) {
        if (user.getRole().getPermission().getClassManagement().equals(UserPermission.VIEW)
                || user.getRole().getPermission().getClassManagement().equals(UserPermission.ACCESS_DENIED)
        ) {
            throw new BadRequestException("User " + user.getEmail() + " does not have permission");
        }
    }

    private void validateTrainingPermission(User user) {
        if (user.getRole().getPermission().getTrainingProgram().equals(UserPermission.VIEW)
                || user.getRole().getPermission().getTrainingProgram().equals(UserPermission.ACCESS_DENIED)
        ) {
            throw new BadRequestException("User " + user.getEmail() + " does not have permission");
        }
    }

    public void isRegisterFormatValid(RegisterRequest request) {
        SecurityEscape.checkRegister(request);
        Pattern pattern = Pattern.compile(properties.getProperty("email"));

        if (!pattern.matcher(request.getEmail()).matches()) {
            throw new BadRequestException("The email: " + request.getEmail() + " is not valid");
        }
        if (!request.getPhone().startsWith("0") || request.getPhone().length() < 10 || request.getPhone().length() > 12) {
            throw new BadRequestException("Phone number is not valid");
        }
        if (request.getName() == "" || request.getCreatedBy() == "" || request.getGender() == "" || request.getRole() == "" || request.getDob() == "") {
            throw new BadRequestException("All field must be fill");
        }
    }

    public User UpdateStatus(StatusUpdateRequest request) throws UserDoesNotExistException {
        User existingUser = userRepository.findById(request.getUserId()).get();
        if (existingUser.getId() != null) {
            if (request.getStatus().equalsIgnoreCase(Status.ACTIVE.name()))
                existingUser.setStatus(Status.ACTIVE);
            if (request.getStatus().equalsIgnoreCase(Status.DEACTIVE.name())) {
                if (existingUser.getRole().getRoleName() == UserRole.TRAINER || existingUser.getRole().getRoleName() == UserRole.ADMIN) {
                    int user_in_class = getUserInClass(request.getUserId());
                    if (user_in_class > 0) {
                        throw new BadRequestException("User " + existingUser.getName() + " is active in " + user_in_class + " class.");
                    }
                }
                existingUser.setStatus(Status.DEACTIVE);
            }
            logger.info("Update user's status success");
            userRepository.save(existingUser);
        } else {
            logger.error("User does not exist. Update status fail.");
            throw new UserDoesNotExistException();
        }
        return existingUser;
    }

    public User UpdateRole(RoleUpdateRequest request) throws UserDoesNotExistException {
        User existingUser = userRepository.findById(request.getUserId()).get();
        if (existingUser.getId() != null) {
            Role role = roleRepository.findByRoleId(request.getRoleID()).get();
            existingUser.setRole(role);
            userRepository.save(existingUser);
        } else {
            logger.error("User does not exist. Update role fail.");
            throw new UserDoesNotExistException();
        }
        return existingUser;
    }

    public void DeleteUser(Integer userID) {
        User existingUser = userRepository.findById(userID).get();
        if (existingUser.getStatus() == Status.NEW) {
            sendMailInfoRepository.save(SendMailDetail.builder()
                    .email(existingUser.getEmail())
                    .data(null)
                    .action(Action.SEND_PASSWORD_REGISTER)
                    .status(Status.FAILED)
                    .date(LocalDate.now())
                    .build());
        } else if (existingUser.getStatus() == Status.ACTIVE) {
            if (existingUser.getRole().getRoleName() == UserRole.TRAINER || existingUser.getRole().getRoleName() == UserRole.ADMIN) {
                int user_in_class = getUserInClass(userID);
                if (user_in_class > 0) {
                    throw new BadRequestException("User " + existingUser.getName() + " is active in " + user_in_class + " class.");
                }
            } else if (existingUser.getRole().getRoleName() == UserRole.SUPER_ADMIN) {
                throw new BadRequestException("User " + existingUser.getName() + " is super admin.");
            }

        }
        userRepository.deleteById(userID);
//        redisTemplate.delete(properties.getProperty("redis-user"));
    }

    private int getUserInClass(Integer userID) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        GetIdFromRequest getIdFromRequest = new GetIdFromRequest();
        getIdFromRequest.setId(userID);
        String url = properties.getProperty("class-url");
        String endpoint = url + "/get_user_in_class_for_service";

        HttpEntity<GetIdFromRequest> requestEntity = new HttpEntity<>(getIdFromRequest, headers);

        ResponseEntity<Integer> responseEntity = restTemplate.exchange(
                endpoint,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Integer>() {
                }
        );

        responseEntity.getBody();

        return responseEntity.getBody();
    }

    public Page<UserDto> searchUsers(String keyword, String searchType, Gender gender, Status status, String roleName, PageRequestDto dto) {
        Specification<User> spec = Specification.where(null);
        Pageable pageable = PageRequest.of(dto.getPageNo(), dto.getPageSize(), Sort.by(dto.getSort().isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC, dto.getSortByColumn()));

        if (gender != null) {
            spec = spec.and((root, query, builder) -> builder.equal(root.get("gender"), gender));
        }

        if (status != null) {
            spec = spec.and((root, query, builder) -> builder.equal(root.get("status"), status));
        }

        if (roleName != null && !roleName.isEmpty()) {
            List<String> roleNames = Arrays.asList(roleName.split(","));
            List<UserRole> userRoles = roleNames.stream().map(UserRole::valueOf).collect(Collectors.toList());
            spec = spec.and((root, query, builder) -> root.join("role").get("roleName").in(userRoles));
        }

        if (keyword != null && !keyword.isEmpty()) {
            if ("email".equalsIgnoreCase(searchType)) {
                spec = spec.and((root, query, builder) -> builder.like(root.get("email"), "%" + keyword + "%"));
            } else if ("name".equalsIgnoreCase(searchType)) {
                spec = spec.and((root, query, builder) -> builder.like(root.get("name"), "%" + keyword + "%"));
            }
        }

        Page<User> searchResults = userRepository.findAll(spec, pageable);
        List<UserDto> userDtoList = mapToUserDtoList(searchResults.getContent());

        return new PageImpl<>(userDtoList, pageable, searchResults.getTotalElements());
    }

    public Page<UserDto> getAllUsers(PageRequestDto dto) {
        Pageable pageable = PageRequest.of(dto.getPageNo(), dto.getPageSize(), Sort.by(dto.getSort().isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC, dto.getSortByColumn()));
        Page<User> userList = userRepository.findAll(pageable);
        List<UserDto> userDtoList = mapToUserDtoList(userList.getContent());

        return new PageImpl<>(userDtoList, pageable, userList.getTotalElements());
    }

    public List<UserDto> mapToUserDtoList(List<User> users) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(properties.getProperty("date"));

        return users.stream().map(user -> {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setName(user.getName());
            userDto.setEmail(user.getEmail());
            userDto.setAvatar(user.getAvatar());
            userDto.setDob(dateFormat.format(user.getDob()));
            userDto.setCreatedAt(dateFormat.format(user.getCreatedAt()));
            userDto.setPhone(user.getPhone());
            userDto.setRoleName(user.getRole().getRoleName().name());
            userDto.setStatus(user.getStatus());
            userDto.setGender(user.getGender());
            userDto.setPassword(user.getPassword());
            return userDto;
        }).collect(Collectors.toList());
    }

//    public User getUserById(Integer id) {
//        return userRepository.getUserById(id);
//    }

    public String uploadProfileImage(Integer userID, MultipartFile file) {
        if (!isImageFile(file)) {
            throw new ResourceNotFoundException("The image should be .jpg, .png");
        }
        //store image
        User user = userRepository.getUserById(userID);
        s3Sevice.deleteObject(s3Bucket.getCustomer(), user.getAvatar());
        String key = properties.getProperty("avatar") + user.getId() + "/" + UUID.randomUUID();
        try {
            s3Sevice.putObject(
                    s3Bucket.getCustomer(),
                    key,
                    file.getBytes()
            );
            user.setAvatar(key);
            userRepository.save(user);
            return key;
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    public boolean isImageFile(MultipartFile file) {
        boolean result = false;
        MediaType contentType = MediaType.parseMediaType(file.getContentType());
        if (contentType.equals(MediaType.IMAGE_PNG) || contentType.equals(MediaType.IMAGE_JPEG)) {
            result = true;
        }
        return result;
    }
    public UserProfileResponse getUserProfileById(Integer id) {
        User user = userRepository.getUserById(id);
        if (user != null) {
            return UserProfileResponse.builder()
                    .id(user.getId())
                    .avatar(properties.getProperty("image_address") + user.getAvatar())
                    .createdAt(user.getCreatedAt())
                    .createdBy(user.getCreatedBy())
                    .dob(user.getDob())
                    .email(user.getEmail())
                    .gender(user.getGender())
                    .name(user.getName())
                    .password(user.getPassword())
                    .phone(user.getPhone())
                    .role(user.getRole())
                    .status(user.getStatus())
                    .build();
        } else {
            logger.error("User is not found");
            throw new ResourceNotFoundException("User is not found");
        }
    }

    public List<UserProfileResponse> getUserFollowToSign(int i) {
        List<UserProfileResponse> list = new ArrayList<>();
        List<User> listUser = null;
        if (i == 0) {
            Role role = roleRepository.findRoleByRoleName(UserRole.TRAINER).get();
            listUser = userRepository.findUserByRoleAndStatusIs(role, Status.ACTIVE);
        } else if (i == 1) {
            Role role = roleRepository.findRoleByRoleName(UserRole.ADMIN).get();
            listUser = userRepository.findUserByRoleAndStatusIs(role, Status.ACTIVE);
        }
        if (listUser.isEmpty()) {
            list = null;
        } else {
            for (User u : listUser
            ) {
                list.add(
                        UserProfileResponse.builder()
                                .id(u.getId())
                                .avatar(u.getAvatar())
                                .createdAt(null)
                                .createdBy(null)
                                .dob(null)
                                .email(u.getEmail())
                                .gender(null)
                                .name(u.getName())
                                .password(null)
                                .phone(u.getPhone())
                                .role(u.getRole())
                                .status(u.getStatus())
                                .build()
                );
            }
        }
        return list;
    }

    public void ValidateError(BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return;
        }
        List<ObjectError> errors = bindingResult.getAllErrors();
        List<String> errorMessages = new ArrayList<>();
        for (ObjectError error : errors) {
            errorMessages.add(error.getDefaultMessage());
        }
        ResponseEntity.badRequest().body(errorMessages);
    }

    public String passwordGenerate() {
        String uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseLetters = uppercaseLetters.toLowerCase();
        String digits = "0123456789";
        String specialCharacters = "!@#$%^&*()-_=+";
        String allCharacters = uppercaseLetters + lowercaseLetters + digits + specialCharacters;
        Random random = new Random();
        StringBuilder passwordBuilder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            passwordBuilder.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }
        return passwordBuilder.toString();
    }

    public void resetPassword(ResetPassRequest request) throws AccountPermissionException {
        User user = userRepository.getUserById(request.getId());
        if (user.getId() != null) {
            if (!passwordEncoder.matches(request.getOldPass(), user.getPassword())) {
                throw new AccountPermissionException("The password not match.");
            }
            if (!validatePassword(request.getNewPass())) {
                throw new BadRequestException("Password format error");
            }
            user.setPassword(passwordEncoder.encode(request.getNewPass()));
            userRepository.save(user);
        }
    }

    public List<UserProfileResponse> getManyUserProfile(List<GetIdFromRequest> request) {
        User u = new User();
        List<UserProfileResponse> list = new ArrayList<>();
        for (GetIdFromRequest id : request
        ) {
            u = userRepository.getUserById(id.getId());
            if (u == null) {
                list.add(
                        UserProfileResponse.builder()
                                .id(null)
                                .avatar(null)
                                .createdAt(null)
                                .createdBy(null)
                                .dob(null)
                                .email(null)
                                .gender(null)
                                .name(null)
                                .password(null)
                                .phone(null)
                                .role(null)
                                .status(null)
                                .build()
                );
            } else {
                list.add(
                        UserProfileResponse.builder()
                                .id(u.getId())
                                .avatar(properties.getProperty("image_address") + u.getAvatar())
                                .createdAt(null)
                                .createdBy(null)
                                .dob(null)
                                .email(u.getEmail())
                                .gender(null)
                                .name(u.getName())
                                .password(null)
                                .phone(u.getPhone())
                                .role(u.getRole())
                                .status(u.getStatus())
                                .build()
                );
            }
        }
        return list;
    }
}

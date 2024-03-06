package com.example.UserRepo.controller;

import com.example.UserRepo.dto.PageRequestDto;
import com.example.UserRepo.dto.SearchRequestWrapper;
import com.example.UserRepo.dto.UserDto;
import com.example.UserRepo.entity.*;
import com.example.UserRepo.enums.Gender;
import com.example.UserRepo.enums.Status;
import com.example.UserRepo.exceptions.AccountPermissionException;
import com.example.UserRepo.exceptions.UserDoesNotExistException;
import com.example.UserRepo.reponses.ResponseObject;
import com.example.UserRepo.reponses.UserProfileResponse;
import com.example.UserRepo.request.*;
import com.example.UserRepo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    @Autowired
    private SendMailDetailService sendMailDetailService;


    @GetMapping("/admin/permission")
    public ResponseEntity<ResponseObject> getPermission(@RequestHeader String username) {
        userService.validateUser(username, "user");
        return ResponseEntity.ok(new ResponseObject(200, "success", roleService.getAll()));
    }

    @PostMapping("admin/updatePermission")
    public ResponseEntity<ResponseObject> setPermission(@RequestBody List<PermissionRequest> request, @RequestHeader String username) {
        userService.validateUser(username, "user");
        return permissionService.setPermission(request);
    }

    @PostMapping(value = "/updateUser")
    public ResponseEntity<ResponseObject> updateUser(@Valid @RequestBody UserUpdateRequest updatedUserRequest
            , @RequestHeader String username, BindingResult bindingResult){
        userService.ValidateError(bindingResult);
        userService.validateUser(username, "user");
        User user = userService.updateUser(updatedUserRequest);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(), "success", user));
    }
    @PostMapping("/admin/update-status")
    public ResponseEntity<ResponseObject> updateStatusUser(@RequestBody StatusUpdateRequest request, @RequestHeader String username) throws UserDoesNotExistException {
        userService.validateUser(username, "user");
        User updatedUserOptional = userService.UpdateStatus(request);
        return ResponseEntity.ok(
                com.example.UserRepo.reponses.ResponseObject.builder()
                        .code(HttpStatus.OK.value())
                        .message("success")
                        .data(updatedUserOptional)
                        .build()
        );
    }
    @PostMapping("/admin/update-role")
    public ResponseEntity<ResponseObject> updateRoleUser(@RequestBody RoleUpdateRequest request, @RequestHeader String username) throws UserDoesNotExistException {
        userService.validateUser(username, "user");
        User updatedUserOptional = userService.UpdateRole(request);
        return ResponseEntity.ok(
                com.example.UserRepo.reponses.ResponseObject.builder()
                        .code(HttpStatus.OK.value())
                        .message("success")
                        .data(updatedUserOptional)
                        .build()
        );
    }

    @PostMapping("/admin/delete")
    public ResponseEntity<ResponseObject> DeleteUser(@RequestBody GetIdFromRequest request, @RequestHeader String username) throws UserDoesNotExistException {
        userService.validateUser(username, "user");
        userService.DeleteUser(request.getId());
        return ResponseEntity.ok(
                com.example.UserRepo.reponses.ResponseObject.builder()
                        .code(HttpStatus.OK.value())
                        .message("deleted")
                        .build()
        );
    }

    @PostMapping("admin/sortListUser")
    public ResponseEntity<ResponseObject> getAllUserPagniationList(@RequestBody PageRequestDto dto, @RequestHeader String username) {
        userService.validateUser(username, "user");
        return ResponseEntity.ok(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("success")
                .data(userService.getAllUsers(dto))
                .build()
        );
    }


    @PostMapping("admin/searchUser")
    public ResponseEntity<ResponseObject> searchUsers(@RequestBody SearchRequestWrapper requestWrapper, @RequestHeader String username) {
        userService.validateUser(username, "user");
        PageRequestDto searchRequest = requestWrapper.getSearchRequest();
        UserDto userDto = requestWrapper.getUserDto();


        String keyword = searchRequest.getKeyword();
        String searchType = searchRequest.getSearchType();
        Gender gender = userDto.getGender() != null ? userDto.getGender() : null;
        Status status = userDto.getStatus() != null ? userDto.getStatus() : null;
        String roleName = userDto.getRoleName();

        Page<UserDto> searchResults = userService.searchUsers(keyword, searchType, gender, status, roleName,searchRequest);

        return ResponseEntity.ok(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                        .message("success")
                        .data(searchResults)
                        .build()
                );

    }
    @PostMapping(value = "/upload-profile-image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<ResponseObject> updateUserProfileImage(@PathVariable("id") Integer userID, @RequestParam("file")MultipartFile file, @RequestHeader String username){
        userService.validateUser(username, "");
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .code(HttpStatus.OK.value())
                        .message("success")
                        .data(userService.uploadProfileImage(userID, file))
                        .build()
        );
    }
    @PostMapping("/getUser")
    public ResponseEntity<ResponseObject> getUser(@RequestBody GetIdFromRequest request, @RequestHeader String username){
        userService.validateUser(username, "");

        UserProfileResponse user = userService.getUserProfileById(request.getId());
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .code(HttpStatus.OK.value())
                        .message("success")
                        .data(user)
                        .build()
        );
    }
    @PostMapping("/get_profile_admin/{id}")
    public ResponseEntity<UserProfileResponse> getProfile(@PathVariable(name = "id") Integer id){
        return ResponseEntity.ok(
                userService.getUserProfileById(id)
        );
    }

    @PostMapping("/get_profile_for_service")
    public ResponseEntity<List<UserProfileResponse>> getProfile(@RequestBody List<GetIdFromRequest> request){
        return ResponseEntity.ok(
                userService.getManyUserProfile(request)
        );
    }

    @PostMapping("admin/get_trainer")
    public ResponseEntity<ResponseObject> getAllTrainer(@RequestHeader String username){
        userService.validateUser(username, "");
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .code(HttpStatus.OK.value())
                        .message("success")
                        .data(userService.getUserFollowToSign(0))
                        .build()
        );
    }
    @PostMapping("admin/get_admin")
    public ResponseEntity<ResponseObject> getAllAdmin(@RequestHeader String username){
        userService.validateUser(username, "");
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .code(HttpStatus.OK.value())
                        .message("success")
                        .data(userService.getUserFollowToSign(1))
                        .build()
        );
    }

    @PostMapping("/admin/get_mail_sended_detail")
    public ResponseEntity<ResponseObject> getMailDetail(@RequestHeader String username, @RequestBody PageRequestDto dto){
        userService.validateUser(username, "user");
        return ResponseEntity.ok(
                com.example.UserRepo.reponses.ResponseObject.builder()
                        .code(HttpStatus.OK.value())
                        .message("success")
                        .data(sendMailDetailService.getAllEmailSend(dto))
                        .build()
        );
    }

    @PostMapping("/resetPass")
    public ResponseEntity<ResponseObject> ResetPassword(@RequestBody ResetPassRequest request, @RequestHeader String username) throws AccountPermissionException {
        userService.validateUser(username, "");
        userService.resetPassword(request);
        return ResponseEntity.ok(
                com.example.UserRepo.reponses.ResponseObject.builder()
                        .code(HttpStatus.OK.value())
                        .message("success")
                        .build()
        );
    }
//    @PostMapping("admin/test")
//    public Object test(){
//
//        return redisTemplate.hasKey("mail-detail");
//    }
}

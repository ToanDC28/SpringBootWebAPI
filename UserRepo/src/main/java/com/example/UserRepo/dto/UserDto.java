package com.example.UserRepo.dto;

import com.example.UserRepo.entity.Role;
import com.example.UserRepo.enums.Gender;
import com.example.UserRepo.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer id;
    private String name;
    private String avatar;
    private String email;
    private String password;
    private String phone;
    private String dob;
    private Gender gender;
//    private List<Role> roles;
    private String roleName;
    private Status status;
    private String createdAt;
}

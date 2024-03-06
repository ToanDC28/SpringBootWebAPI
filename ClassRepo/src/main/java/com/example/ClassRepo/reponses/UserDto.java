package com.example.ClassRepo.reponses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String gender;
    private Role role;
    private String status;
    private String createdAt;
}

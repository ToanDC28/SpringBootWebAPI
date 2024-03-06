package com.team2.trainingprogramrepo.dto;

import com.team2.trainingprogramrepo.enums.Gender;
import com.team2.trainingprogramrepo.enums.Status;
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
    private Gender gender;
    private String roleName;
    private Status status;
    private String createdAt;
}

package com.example.UserRepo.reponses;

import com.example.UserRepo.entity.Role;
import com.example.UserRepo.enums.Gender;
import com.example.UserRepo.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private Integer id;
    private String name;
    private String avatar;
    private String password;
    private String email;
    private String phone;
    private Date dob;
    private Gender gender;
    private String createdBy;
    private Role role;
    private Status status;
    private Date createdAt;
}

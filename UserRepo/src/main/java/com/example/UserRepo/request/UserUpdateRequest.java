package com.example.UserRepo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateRequest {
    private Integer id;
    private String name;
    private String gender;
    private String phone;
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Pattern(regexp = "\\d{4}/\\d{2}/\\d{2}")
    private String dob;
}

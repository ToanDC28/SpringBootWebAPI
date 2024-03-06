package com.example.UserRepo.request;

import lombok.Builder;
import lombok.Data;

@Data
public class ResetPassRequest {
    private Integer id;
    private String oldPass;
    private String newPass;
}

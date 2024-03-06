package com.example.UserRepo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleUpdateRequest {
    private Integer userId;
    private Integer roleID;
}

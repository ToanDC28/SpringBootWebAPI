package com.example.UserRepo.request;

import com.example.UserRepo.entity.Permission;
import com.example.UserRepo.entity.Role;
import com.example.UserRepo.enums.UserPermission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PermissionRequest {
    private int permissionID;
    private UserPermission syllabus;
    private UserPermission trainingProgram;
    private UserPermission classManagement;
    private UserPermission learningMaterial;
    private UserPermission userManagement;
}

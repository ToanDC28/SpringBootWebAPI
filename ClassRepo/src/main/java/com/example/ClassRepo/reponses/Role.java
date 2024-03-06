package com.example.ClassRepo.reponses;

import lombok.Data;

import javax.persistence.*;

@Data
public class Role {
    private Integer roleId;
    private String roleName;
    private Permission permission;
}

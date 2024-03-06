package com.example.ClassRepo.reponses;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class Permission {
    private Integer permissionId;
    private String syllabus;
    private String trainingProgram;
    private String classManagement;
    private String learningMaterial;
    private String userManagement;
}

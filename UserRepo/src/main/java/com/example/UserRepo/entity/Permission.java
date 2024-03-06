package com.example.UserRepo.entity;

import com.example.UserRepo.enums.UserPermission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "permission")
public class Permission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer permissionId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserPermission syllabus;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserPermission trainingProgram;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserPermission classManagement;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserPermission learningMaterial;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserPermission userManagement;

}

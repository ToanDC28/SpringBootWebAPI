package com.example.UserRepo.entity;

import com.example.UserRepo.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "Role")
@Table(name = "role")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "role_id",
            nullable = false
    )
    private Integer roleId;
    @Column(
            name = "role_name",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private UserRole roleName;
    @OneToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;

}

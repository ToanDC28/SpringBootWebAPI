package com.example.UserRepo.repository;

import com.example.UserRepo.entity.Role;
import com.example.UserRepo.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleId(Integer roleId);
    Optional<Role> findRoleByRoleName(UserRole roleName);
}

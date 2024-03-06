package com.example.UserRepo.repository;

import com.example.UserRepo.entity.ResetPassCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPassRepository extends JpaRepository<ResetPassCode, Integer> {
    Optional<ResetPassCode> findByCode(String code);
    ResetPassCode findByCodeAndEmail(String code, String email);
}

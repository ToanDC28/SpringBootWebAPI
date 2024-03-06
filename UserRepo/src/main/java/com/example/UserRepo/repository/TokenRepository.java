package com.example.UserRepo.repository;

import com.example.UserRepo.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    Token findTokenByToken(String token);
    boolean existsTokenByToken(String token);
    void deleteTokenByToken(String token);
}
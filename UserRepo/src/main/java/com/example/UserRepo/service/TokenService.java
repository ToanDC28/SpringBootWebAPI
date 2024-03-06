package com.example.UserRepo.service;

import com.example.UserRepo.entity.Token;
import com.example.UserRepo.entity.User;
import com.example.UserRepo.enums.TypeToken;
import com.example.UserRepo.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private JwtService jwtService;
//    @Autowired
//    private RedisTemplate redisTemplate;

    public void deleteToken(String token){
        tokenRepository.deleteTokenByToken(token);
//        if(redisTemplate.hasKey(token)){
//            redisTemplate.delete(token);
//        }
        jwtService.cancelScheduledTask();
    }

    public boolean checkExistToken(String token){
        return tokenRepository.existsTokenByToken(token);
    }

    public String getTokenForUser(User user) {
        String t = jwtService.generateToken(user);
        Token token = new Token();
        token.setEmail(user.getEmail());
        token.setToken(t);
        token.setDuration(30 * 60 * 1000);
        token.setType(TypeToken.Bearer);
        tokenRepository.save(token);
//        redisTemplate.opsForValue().set(t, t);
        return t;
    }
}

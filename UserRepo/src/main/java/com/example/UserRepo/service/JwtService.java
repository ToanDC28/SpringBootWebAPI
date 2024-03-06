package com.example.UserRepo.service;

import com.example.UserRepo.entity.Token;
import com.example.UserRepo.entity.User;
import com.example.UserRepo.exceptions.InvalidCredentialsException;
import com.example.UserRepo.exceptions.ResourceNotFoundException;
import com.example.UserRepo.exceptions.TokenExpiredException;
import com.example.UserRepo.repository.TokenRepository;
import com.example.UserRepo.repository.UserRepository;
import com.example.UserRepo.service.TokenService;
import com.example.UserRepo.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class JwtService {
//    private static final String SECRET_KEY = "O4fFTOlJeOaUpmLtRBBraD7OzoKWvvLp26A5789CA94F3CA7836489F698741";
    @Autowired
    private Properties properties;
    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private RedisTemplate redisTemplate;
    @Autowired
    private TokenRepository tokenRepository;
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> taskFuture;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                ()->new ResourceNotFoundException("User " + userDetails.getUsername() + " not found")
        );
        extraClaims.put("uid", user.getId());
        extraClaims.put("urn", user.getName());
        extraClaims.put("role", user.getRole().getRoleName());
        extraClaims.put("syllabus", user.getRole().getPermission().getSyllabus());
        extraClaims.put("training", user.getRole().getPermission().getTrainingProgram());
        extraClaims.put("class", user.getRole().getPermission().getClassManagement());
        extraClaims.put("userManage", user.getRole().getPermission().getUserManagement());
        String token = Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
        deleteTokenAuto(token);
        return token;
    }

    private void deleteTokenAuto(String token){
        scheduler = Executors.newSingleThreadScheduledExecutor();
        long delay = 30L * 60L * 1000L;;
        taskFuture = scheduler.schedule(() -> closeCode(token), delay, TimeUnit.MILLISECONDS);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsSolver) {
        final Claims claims = extractAllClaims(token);
        return claimsSolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private Key getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode(properties.getProperty("secretKey"));
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String refreshToken(String refreshToken){
        Token existingToken = tokenRepository.findTokenByToken(refreshToken);
        if(existingToken.getId() != null){
            cancelScheduledTask();
            User user = userRepository.findByEmail(existingToken.getEmail()).orElseThrow(
                    ()->new ResourceNotFoundException("User " + existingToken.getEmail() + " not found")
            );
            String token = generateToken(user);
            existingToken.setToken(token);
            tokenRepository.save(existingToken);
            //redis
//            if(redisTemplate.hasKey(refreshToken)){
//                redisTemplate.delete(refreshToken);
//            }
//            redisTemplate.opsForValue().set(token, token);
//            deleteTokenAuto(token);
            return token;
        }else {
            throw new ResourceNotFoundException("The token is not valid");
        }
    }
    public void closeCode(String code) {
        Token token = tokenRepository.findTokenByToken(code);
        if(token != null){
            tokenRepository.deleteTokenByToken(code);
//            if(redisTemplate.hasKey(code)){
//                redisTemplate.delete(code);
//            }
        }
        cancelScheduledTask();
    }
    public void cancelScheduledTask() {
        if (taskFuture != null) {
            taskFuture.cancel(false);
        }
    }


}
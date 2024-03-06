package com.FAMS.apigateway.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.http.auth.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtUtil {
    private static final String SECRET_KEY = "O4fFTOlJeOaUpmLtRBBraD7OzoKWvvLp26A5789CA94F3CA7836489F698741";

    public String validateToken(final String token){
        Jws<Claims> claimsJws =Jwts.parserBuilder().setSigningKey(getSigninKey()).build().parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        String username = claims.getSubject();
        return username;
    }
    private Key getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

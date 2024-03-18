package com.example.userervice.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;



import static com.nimbusds.jwt.JWTClaimNames.EXPIRATION_TIME;

@Component
public class JwtUtils {

    private static String mySecret;

    @Value("${secret}")
    public void setMySecret(String mySecret) {
        JwtUtils.mySecret = mySecret;
    }

    // Generate JWT
    public static String generateToken(String email, String username) {
        Instant now = Instant.now();
        Instant expiryInstant = now.plus(30, ChronoUnit.DAYS);

        Date nowDate = Date.from(now);
        Date expiryDate = Date.from(expiryInstant);

        return Jwts.builder()
                .setSubject(email)
                .claim("username", username)
                .setIssuedAt(nowDate)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, mySecret)
                .compact();
    }

    // Validate JWT
    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(mySecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
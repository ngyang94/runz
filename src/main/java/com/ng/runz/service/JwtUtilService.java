package com.ng.runz.service;

import com.ng.runz.model.Users;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

@Service
public class JwtUtilService {
    @Value("${api.jwt.secretKey}")
    String secretkey;
    @Value("${api.jwt.expirationInMilSec}")
    String expirationInMilSec;

    public JwtUtilService(){

    }

    public String generateJwtToken(Users user){
        try {
            HashMap<String,String> claims = new HashMap<>();
            claims.put("role",user.getRoles().toString());
            return Jwts.builder()
                    .claims()
                    .add(claims)
                    .subject(user.getUsername())
                    .issuedAt(new Date())
                    .expiration(new Date(new Date().getTime()+Integer.parseInt(expirationInMilSec)))
                    .and()
                    .signWith(key())
                    .compact();
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validateToken(String jwtToken){
        try {
            Jwts.parser().verifyWith(key()).build().parseSignedClaims(jwtToken).getPayload();
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsernameFromJwtToken(String jwtToken){
        try {
            return Jwts.parser().verifyWith(key()).build().parseSignedClaims(jwtToken).getPayload().getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("error happen:"+e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private SecretKey key(){
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretkey));
    }
}

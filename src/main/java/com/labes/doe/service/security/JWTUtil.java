package com.labes.doe.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTUtil {
    private Logger log = LoggerFactory.getLogger(JWTUtil.class);

    @Value("${service.jwt.secret}")
    private String secret;

    @Value("${service.jwt.expiration}")
    private Long expiration;

    public String generateToken(String username){
        Date now =new Date();
        Map<String, Object> claim = new HashMap<>();
        claim.put("alg", "HS256");
        claim.put("typ", "JWT");

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,Base64.getEncoder().encode(secret.getBytes()))
                .setClaims(claim)
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(Base64.getEncoder().encode(secret.getBytes()))
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody();
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDate(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    public Boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDate(token);
        log.info("isTokenExpired: "+expirationDate.before(new Date()));
        return expirationDate.before(new Date());
    }

    public Boolean isTokenValidated(String token) {
        return !isTokenExpired(token);
    }
}
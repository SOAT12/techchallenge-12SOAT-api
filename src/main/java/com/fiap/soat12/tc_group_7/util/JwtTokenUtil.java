package com.fiap.soat12.tc_group_7.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final long JWT_TOKEN_VALIDITY_IN_MINUTE = 120;

    private String secret = new String(Base64.getEncoder().encode(RandomStringUtils.random(32, true, true).getBytes()));

    public void renewSecret() {
        secret = new String(Base64.getEncoder().encode(RandomStringUtils.random(32, true, true).getBytes()));
    }

    public String getSubject(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver)  {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token)  {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(toDate(LocalDateTime.now()))
                .setExpiration(toDate(LocalDateTime.now().plusMinutes(JWT_TOKEN_VALIDITY_IN_MINUTE)))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    private static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
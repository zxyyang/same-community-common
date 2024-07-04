package com.same.community.common.util.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @author Zixuan.Yang
 * @date 2023/12/8 11:54
 */
@Slf4j
@Component
public class JwtTokenUtil {

    private static final String SECRET_KEY = "bBJ16BOohx96vn7o8w8sDUNvKbJxEH";

    private static final long EXPIRATION_TIME = 30L;


    public String generateToken(String subject, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (EXPIRATION_TIME * 86400000)))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public boolean isTokenExpired(Claims claims, String token) {
        try {
            if (claims == null) {
                claims = parseToken(token);
            }

            return claims.getExpiration().before(new Date());

        } catch (Exception e) {
            log.error("Token is Expired: {}", e.getMessage());
            return false;
        }

    }

    public boolean isTokenValid(Claims claims, String token, String uid) {
        try {
            if (claims == null) {
                claims = parseToken(token);
            }

            // 判断token是否过期，并且判断是否自己签发的token
            return !isTokenExpired(claims, token) && claims.getSubject().equals(uid);

        } catch (Exception e) {
            log.error("Token is invalid: {}", e.getMessage());
            return false;
        }
    }
    public boolean isTokenExpired(String token) {
        try {
            return parseToken(token).getExpiration().before(new Date());
        } catch (Exception e) {
            log.error("Token is Expired: {}", e.getMessage());
            return false;
        }

    }

    public boolean isTokenValid(String token, String uid) {
        try {
            // 判断token是否过期，并且判断是否自己签发的token
            return !isTokenExpired(token) && parseToken(token).getSubject().equals(uid);
        } catch (Exception e) {
            log.error("Token is invalid: {}", e.getMessage());
            return false;
        }

    }

}

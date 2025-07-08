package com.example.demo.util;

import com.example.demo.model.dto.UserCert;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;
    private SecretKey signingKey;
    @PostConstruct
    public void init() {
        logger.info("================ JWTUTILS INITIALIZATION CHECK ================");

        if (secret == null || secret.isBlank()) {
            logger.error("FATAL ERROR: jwt.secret value is NULL or EMPTY. @Value injection failed!");
        } else if (secret.equals("ThisIsMySuperLongAndSimpleSecretKeyForTesting123")) {
            logger.warn("WARNING: jwt.secret is still using the default placeholder value.");
            this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            logger.info("SUCCESS: Default jwt.secret loaded and SecretKey initialized.");
        } else {
            logger.info("SUCCESS: Custom jwt.secret loaded successfully.");
            this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            logger.info("SecretKey initialized successfully.");
        }

        if (expiration == null || expiration <= 0) {
            logger.error("FATAL ERROR: jwt.expiration value is NULL or invalid!");
        } else {
            logger.info("SUCCESS: jwt.expiration loaded with value: {} ms", expiration);
        }
        logger.info("==============================================================");
    }

    private SecretKey getSigningKey(){
        // 這個方法現在會回傳在 init() 中已經初始化好的 key
        return this.signingKey;
    }
    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public <T>T extractClaim(String token, Function<Claims,T> claimsResolver){
    final Claims claims=extractAllClaims(token);
    return  claimsResolver.apply(claims);
    }
    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }
    //提取過期時間
    public Date extractExpiration(String token){
        return  extractClaim(token,Claims::getExpiration);
    }
    //驗證token過期
    public  Boolean isTokenExpired(String token){
        return  extractExpiration(token).before(new Date());
    }

    public String generationToken(UserCert userCert){
        Map<String,Object> claims=new HashMap<>();
        claims.put("userId", userCert.getUserId());
        claims.put("role", userCert.getRole());
        claims.put("emailcheck", userCert.isEmailcheck());
        return  createToken(claims,userCert.getUserName());
    }
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject) // 主題，通常是使用者名稱
                .issuedAt(new Date(System.currentTimeMillis())) // 簽發時間
                .expiration(new Date(System.currentTimeMillis() + expiration)) // 過期時間
                .signWith(getSigningKey(), Jwts.SIG.HS256) // 簽名演算法
                .compact();
    }
    public Boolean validateToken(String token, String username){
        try {
            // 在解析之前，先確保 signingKey 不是 null
            if (this.signingKey == null) {
                logger.error("FATAL: signingKey is null. Cannot validate token.");
                return false;
            }
            final String extractedUsername = extractUsername(token);
            boolean isUsernameMatch = username.equals(extractedUsername);
            boolean isTokenNotExpired = !isTokenExpired(token);
            return isUsernameMatch && isTokenNotExpired;
        } catch (Exception e) {
            // 這個日誌現在能捕捉到所有驗證錯誤，包括簽名錯誤
            logger.error("Token validation failed for user [{}]. Root cause: {}", username, e.toString());
            return false;
        }
    }
}

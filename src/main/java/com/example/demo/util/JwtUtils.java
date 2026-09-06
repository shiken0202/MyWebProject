package com.example.demo.util;

import com.example.demo.model.dto.UserCert;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // 從 application.properties 讀取長短 Token 的過期時間
    @Value("${jwt.access-token.expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration}")
    private Long refreshTokenExpiration;

    // 定義公私鑰的儲存路徑
    private static final String KEY_DIR = "keys";
    private static final String PRIVATE_KEY_PATH = KEY_DIR + "/private_key.pem";
    private static final String PUBLIC_KEY_PATH = KEY_DIR + "/public_key.pem";

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        logger.info("================ JWT 雙 Token 架構 (RSA) 初始化 ================");
        try {
            File keyDir = new File(KEY_DIR);
            if (!keyDir.exists()) keyDir.mkdirs();

            File privateKeyFile = new File(PRIVATE_KEY_PATH);
            File publicKeyFile = new File(PUBLIC_KEY_PATH);

            if (!privateKeyFile.exists() || !publicKeyFile.exists()) {
                logger.info("系統尚未建立金鑰檔案，準備自動生成新的 RSA 金鑰對...");
                generateAndSaveKeyPair();
            }

            this.privateKey = readPrivateKey(PRIVATE_KEY_PATH);
            this.publicKey = readPublicKey(PUBLIC_KEY_PATH);
            
            logger.info("✅ RSA 公私鑰讀取成功，JWT 準備就緒！");
        } catch (Exception e) {
            logger.error("❌ JWT 金鑰初始化失敗：{}", e.getMessage());
        }
        logger.info("==============================================================");
    }

    private void generateAndSaveKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        String privateKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        Files.write(Paths.get(PRIVATE_KEY_PATH), privateKeyBase64.getBytes());

        String publicKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        Files.write(Paths.get(PUBLIC_KEY_PATH), publicKeyBase64.getBytes());
    }

    private PrivateKey readPrivateKey(String filePath) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(filePath));
        String privateKeyBase64 = new String(keyBytes);
        byte[] decodedKey = Base64.getDecoder().decode(privateKeyBase64);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    private PublicKey readPublicKey(String filePath) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(filePath));
        String publicKeyBase64 = new String(keyBytes);
        byte[] decodedKey = Base64.getDecoder().decode(publicKeyBase64);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    // ================= 雙 Token 生成邏輯 =================

    /**
     * 簽發 Access Token (短 Token)
     */
    public String generateAccessToken(UserCert userCert) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userCert.getUserId());
        claims.put("role", userCert.getRole());
        claims.put("emailcheck", userCert.isEmailcheck());
        claims.put("token_type", "access"); // 標記為 Access Token
        
        return Jwts.builder()
                .claims(claims)
                .subject(userCert.getUserName())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(this.privateKey, Jwts.SIG.RS256)
                .compact();
    }

    /**
     * 簽發 Refresh Token (長 Token)
     */
    public String generateRefreshToken(UserCert userCert) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("token_type", "refresh"); // 標記為 Refresh Token
        
        return Jwts.builder()
                .claims(claims)
                .subject(userCert.getUserName())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(this.privateKey, Jwts.SIG.RS256)
                .compact();
    }

    // ================= JWT 解析與驗證邏輯 =================

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(this.publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get("token_type", String.class));
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * 原本的 validateToken (為了相容 JwtRequestFilter，這裡固定驗證 access token)
     */
    public Boolean validateToken(String token, String username) {
        return validateAccessToken(token, username);
    }

    /**
     * 驗證 Access Token 是否合法
     */
    public Boolean validateAccessToken(String token, String username) {
        try {
            if (this.publicKey == null) return false;
            
            final String extractedUsername = extractUsername(token);
            final String tokenType = extractTokenType(token);
            
            boolean isUsernameMatch = username.equals(extractedUsername);
            boolean isTokenNotExpired = !isTokenExpired(token);
            boolean isAccessToken = "access".equals(tokenType); // 阻擋 refresh token 存取一般 API
            
            return isUsernameMatch && isTokenNotExpired && isAccessToken;
        } catch (Exception e) {
            logger.error("Access Token validation failed for user [{}]. Root cause: {}", username, e.getMessage());
            return false;
        }
    }

    /**
     * 驗證 Refresh Token 是否合法
     */
    public Boolean validateRefreshToken(String token) {
        try {
            if (this.publicKey == null) return false;
            
            final String tokenType = extractTokenType(token);
            boolean isTokenNotExpired = !isTokenExpired(token);
            boolean isRefreshToken = "refresh".equals(tokenType);
            
            return isTokenNotExpired && isRefreshToken;
        } catch (Exception e) {
            logger.error("Refresh Token validation failed. Root cause: {}", e.getMessage());
            return false;
        }
    }
}

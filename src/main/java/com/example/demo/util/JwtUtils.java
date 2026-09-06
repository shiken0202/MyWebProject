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

    // 從 application.properties 讀取過期時間
    @Value("${jwt.expiration}")
    private Long expiration;

    // 定義公私鑰的儲存路徑 (存放在專案根目錄下的 keys 資料夾)
    private static final String KEY_DIR = "keys";
    private static final String PRIVATE_KEY_PATH = KEY_DIR + "/private_key.pem";
    private static final String PUBLIC_KEY_PATH = KEY_DIR + "/public_key.pem";

    // 宣告私鑰與公鑰物件，用來在記憶體中快取，避免每次驗證都要重新讀取檔案
    private PrivateKey privateKey;
    private PublicKey publicKey;

    /**
     * @PostConstruct 代表當 Spring Boot 啟動並把 JwtUtils 建立成 Bean 後，
     * 就會自動執行這個 init() 方法。我們在這裡進行金鑰的讀取或初始化。
     */
    @PostConstruct
    public void init() {
        logger.info("================ JWT 非對稱式加密 (RSA) 初始化 ================");
        try {
            File keyDir = new File(KEY_DIR);
            // 檢查 keys 資料夾是否存在，不存在的話幫忙建一個
            if (!keyDir.exists()) {
                keyDir.mkdirs();
            }

            File privateKeyFile = new File(PRIVATE_KEY_PATH);
            File publicKeyFile = new File(PUBLIC_KEY_PATH);

            // 如果沒有發現金鑰檔案，代表可能是第一次啟動，我們自動生成一對新的 RSA 公私鑰並存檔
            if (!privateKeyFile.exists() || !publicKeyFile.exists()) {
                logger.info("系統尚未建立金鑰檔案，準備自動生成新的 RSA 金鑰對...");
                generateAndSaveKeyPair();
            }

            // 無論是剛剛建立的，還是以前留下來的，我們都在此把實體檔案讀取到記憶體中
            this.privateKey = readPrivateKey(PRIVATE_KEY_PATH);
            this.publicKey = readPublicKey(PUBLIC_KEY_PATH);
            
            logger.info("✅ RSA 公私鑰讀取成功，JWT 準備就緒！");
        } catch (Exception e) {
            logger.error("❌ JWT 金鑰初始化失敗：{}", e.getMessage());
        }
        logger.info("==============================================================");
    }

    /**
     * 生成 RSA 金鑰對並將其以 Base64 格式儲存到實體檔案中。
     * (這個方法取代了 PDF 教材裡手動寫 main 方法執行的步驟，讓系統全自動處理)
     */
    private void generateAndSaveKeyPair() throws Exception {
        // 1. 取得產生 RSA 金鑰對的工具 (KeyPairGenerator)
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        // 2. 初始化金鑰長度為 2048 位元 (越長越安全，但運算較慢，2048是目前業界標準)
        keyPairGenerator.initialize(2048);
        // 3. 產生金鑰對 (包含一組公鑰與私鑰)
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // 4. 將私鑰轉成位元組 (byte[])，然後進行 Base64 編碼，最後存入檔案
        String privateKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        Files.write(Paths.get(PRIVATE_KEY_PATH), privateKeyBase64.getBytes());
        logger.info("🔒 私鑰已自動儲存至: {}", PRIVATE_KEY_PATH);

        // 5. 將公鑰也做一樣的處理
        String publicKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        Files.write(Paths.get(PUBLIC_KEY_PATH), publicKeyBase64.getBytes());
        logger.info("🔑 公鑰已自動儲存至: {}", PUBLIC_KEY_PATH);
    }

    /**
     * 從指定路徑讀取「私鑰」檔案，並轉回 PrivateKey 物件
     */
    private PrivateKey readPrivateKey(String filePath) throws Exception {
        // 1. 讀取指定路徑的檔案內容 (會讀到一串 Base64 字串)
        byte[] keyBytes = Files.readAllBytes(Paths.get(filePath));
        String privateKeyBase64 = new String(keyBytes);

        // 2. 使用 Base64 將字串解碼，還原回原本的二進位資料 (byte[])
        byte[] decodedKey = Base64.getDecoder().decode(privateKeyBase64);

        // 3. 私鑰的標準編碼格式是 PKCS8，所以我們使用 PKCS8EncodedKeySpec 來包裝解碼後的資料
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        
        // 4. 透過 KeyFactory (RSA工廠) 把規格書 (keySpec) 真正轉換成 Java 的 PrivateKey 物件
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 從指定路徑讀取「公鑰」檔案，並轉回 PublicKey 物件
     */
    private PublicKey readPublicKey(String filePath) throws Exception {
        // 1. 讀取指定路徑的檔案內容
        byte[] keyBytes = Files.readAllBytes(Paths.get(filePath));
        String publicKeyBase64 = new String(keyBytes);

        // 2. Base64 解碼
        byte[] decodedKey = Base64.getDecoder().decode(publicKeyBase64);

        // 3. 注意！公鑰的標準編碼格式是 X.509，跟私鑰不同，必須使用 X509EncodedKeySpec 來包裝
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        
        // 4. 透過 KeyFactory 轉換成 Java 的 PublicKey 物件
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    // ================= 以下為 JWT 核心操作邏輯 =================

    /**
     * 生成 JWT Token 給使用者。
     * 這裡最關鍵的是：我們使用「私鑰 (privateKey)」來進行簽章 (Sign)。
     */
    public String generationToken(UserCert userCert) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userCert.getUserId());
        claims.put("role", userCert.getRole());
        claims.put("emailcheck", userCert.isEmailcheck());
        
        return Jwts.builder()
                .claims(claims) // 放入 Payload 負載資料
                .subject(userCert.getUserName()) // 設定主旨通常為 username
                .issuedAt(new Date(System.currentTimeMillis())) // 簽發時間
                .expiration(new Date(System.currentTimeMillis() + expiration)) // 過期時間
                // 【核心改變】採用非對稱式演算法 Jwts.SIG.RS256，並傳入「私鑰」
                .signWith(this.privateKey, Jwts.SIG.RS256)
                .compact();
    }

    /**
     * 解析 JWT，取得所有宣告資訊 (Claims)。
     * 這裡最關鍵的是：我們使用「公鑰 (publicKey)」來驗證 Token 是否合法、有沒有被篡改。
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                // 【核心改變】用「公鑰」去解鎖並驗證這張 Token
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

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, String username) {
        try {
            if (this.publicKey == null) {
                logger.error("FATAL: publicKey is null. Cannot validate token.");
                return false;
            }
            final String extractedUsername = extractUsername(token);
            boolean isUsernameMatch = username.equals(extractedUsername);
            boolean isTokenNotExpired = !isTokenExpired(token);
            return isUsernameMatch && isTokenNotExpired;
        } catch (Exception e) {
            logger.error("Token validation failed for user [{}]. Root cause: {}", username, e.toString());
            return false;
        }
    }
}

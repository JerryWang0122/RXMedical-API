package com.rxmedical.api.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;

public class KeyUtil {

    /**
     * 可將字節陣列轉換為十六進制格式的字符串。
     * 這通常用於方便地顯示二進制數據，如數字簽名、摘要或加密的數據。
     *
     * @param bytes 要轉換的字節陣列
     * @return 表示給定字節的十六進制格式的字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * 要從十六進制格式的雜湊字串轉回 byte[]
     *
     * @return 返回 byte[]。
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    // JWT
    /**
     * 生成指定長度的隨機密鑰，並將其 Base64 編碼。
     *
     * @param byteLength 欲生成的密鑰的字節長度。例如，若要生成 256 位的密鑰，字節長度應為 32。
     * @return 返回 Base64 編碼的密鑰。
     *
     * 使用方法:
     * - SecureRandom 用於生成加密安全的隨機數。
     * - 這個方法首先使用 SecureRandom 生成指定長度的隨機密鑰。
     * - 然後將生成的密鑰進行 Base64 編碼，這樣可以將它安全地存儲或轉輸，同時避免任何不打印或特殊的字符。
     */
    public static String generateSecret(int byteLength) {
        SecureRandom secureRandom = new SecureRandom();   // 創建一個加密安全的隨機數生成器
        byte[] key = new byte[byteLength];                // 分配用於保存密鑰的空間
        secureRandom.nextBytes(key);                      // 生成隨機密鑰
        return Base64.getEncoder().encodeToString(key);   // 將密鑰 Base64 編碼並返回
    }

    /**
     * 簽名 JWT。
     *
     * @param claimsSet   JWT 聲明集。
     * @param secret      簽名密鑰。
     * @return 簽名的 JWT 字串。
     * @throws JOSEException 如果簽名過程中發生錯誤。
     */
    public static String signJWT(JWTClaimsSet claimsSet, String secret) throws JOSEException {
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        JWSSigner signer = new MACSigner(secret);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    /**
     * 驗證 JWT 的簽名。
     *
     * @param token  JWT 字串。
     * @param secret 驗證密鑰。
     * @return 若簽名有效返回 true，否則返回 false。
     * @throws ParseException 如果解析 JWT 字串時出錯。
     * @throws JOSEException  如果驗證過程中發生錯誤。
     */
    public static boolean verifyJWTSignature(String token, String secret) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            // 檢查簽名
            JWSVerifier verifier = new MACVerifier(secret);
            if (!signedJWT.verify(verifier)) {
                return false;
            }

            // 檢查過期時間
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expirationTime != null && new Date().after(expirationTime)) {
                return false; // Token 已經過期
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 從 JWT 字串中獲取聲明集。
     *
     * @param token JWT 字串。
     * @return JWTClaimsSet 物件。
     * @throws ParseException 如果解析 JWT 字串時出錯。
     */
    public static JWTClaimsSet getClaimsFromToken(String token) throws ParseException {
        return SignedJWT.parse(token).getJWTClaimsSet();
    }

}

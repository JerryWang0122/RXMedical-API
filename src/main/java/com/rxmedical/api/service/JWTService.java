package com.rxmedical.api.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.rxmedical.api.model.po.User;
import com.rxmedical.api.util.KeyUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class JWTService {

    private final static String SIGNING_SECURE_PATH = "./signingSecure.txt";
    private final static String SIGNING_SECURE = getJWTSigningSecure();

    /**
     * 使用者在登入成功後發的JWT
     * @param user 使用者資訊
     * @return 依照傳入參數產生的JWT，包含id, authLevel
     * @throws JOSEException
     */
    public String getUserUsageJWT(User user) throws JOSEException {
        if ("register".equals(user.getAuthLevel()) || "off".equals(user.getAuthLevel())) {
            return null;
        }

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("User usage")
                .issuer("RXMedical")
                .claim("id", user.getId())
                .claim("authLevel", user.getAuthLevel())
                .build();
        return KeyUtil.signJWT(claimsSet, SIGNING_SECURE);
    }

    public Map<String, Object> verifyUserUsageJWT(HttpServletRequest request) throws ParseException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ") ) {
            return null;
        }

        // CSRF 驗證
        String jwt = authorizationHeader.substring(7);
        if (!KeyUtil.verifyJWTSignature(jwt, SIGNING_SECURE)) {
            return null;
        }
        JWTClaimsSet claims = KeyUtil.getClaimsFromToken(jwt);
        return Map.of(
                "userId", claims.getIntegerClaim("id"),
                "authLevel", claims.getClaim("authLevel")
        );
    }


    /**
     * 取得CSRF Token 及其 JWT
     * @return [CSRF_Token, JWT(CSRF_Token)]
     * @throws JOSEException
     */
    public List<String> getCSRFTokenJWT() throws JOSEException {
        String CSRFToken = getUUID();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("CSRFToken")
                .issuer("RXMedical")
                .claim("CSRFToken", CSRFToken)
                .build();
        return List.of(CSRFToken, KeyUtil.signJWT(claimsSet, SIGNING_SECURE));
    }

    /**
     * 檢驗request header 的 CSRF Token 與傳入的token是否吻合
     * @param token form body 傳入的 CSRF Token
     * @param request header's authorization should be set and bearer token
     * @return CSRF Token是否符合
     * @throws ParseException
     */
    public boolean checkCSRFTokenJWT(String token, HttpServletRequest request) throws ParseException {
        if (token == null || request == null) {
            return false;
        }

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ") ) {
            return false;
        }

        // CSRF 驗證
        String jwt = authorizationHeader.substring(7);
        if (!KeyUtil.verifyJWTSignature(jwt, SIGNING_SECURE)) {
            return false;
        }
        JWTClaimsSet claims = KeyUtil.getClaimsFromToken(jwt);

        return token.equals(claims.getClaim("CSRFToken"));
    }

    /**
     * [工具] 輔助JWT產生
     * @return 取得JWT簽名專用密鑰
     */
    private static String getJWTSigningSecure() {
        // 如果檔案存在，則直接取出內容，否則建立並寫入檔案
        try {
            if (new File(SIGNING_SECURE_PATH).exists()) {
                return Files.readString(Path.of(SIGNING_SECURE_PATH));
            } else {
                String signingSecret = KeyUtil.generateSecret(32);
                Files.writeString(Path.of(SIGNING_SECURE_PATH), signingSecret);
                return signingSecret;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 取得 UUID Token
     * @return UUID Token
     */
    private String getUUID() {
        return UUID.randomUUID().toString();
    }
}

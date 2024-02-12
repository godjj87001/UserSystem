package com.userSystem;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.util.Base64;

@Service
@Slf4j
public class UtilService {


    /**
     * PBKDF2 hash
     *
     * @param password   密碼
     * @param salt       加鹽
     * @param iterations 次數
     * @param keyLength  密鑰長度
     * @return hashedString
     */
    public String hashWithPBKDF2(String password, String salt, int iterations, int keyLength) {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), iterations, keyLength * 8);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            log.error("Error hashing with PBKDF2", e);
        }
        return "";
    }
}

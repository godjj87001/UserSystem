package com.userSystem.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Locale;

@Service
@Slf4j
public class UtilService {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final RestTemplate restTemplate = new RestTemplate();

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

    /**
     * 獲取當前時間
     *
     * @return string time
     */
    public String getLocalTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return currentDateTime.format(formatter);
    }


    public  <T> T sendHttpRequest(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType) {
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, method, requestEntity, responseType);
        return responseEntity.getBody();
    }

    public  <T> T sendHttpGetRequest(String url, Class<T> responseType) {
        return sendHttpRequest(url, HttpMethod.GET, null, responseType);
    }

    public  <T> T sendHttpPostRequest(String url, HttpEntity<?> requestEntity, Class<T> responseType) {
        System.out.println("send post");
        System.out.println(url);
        return sendHttpRequest(url, HttpMethod.POST, requestEntity, responseType);
    }


}

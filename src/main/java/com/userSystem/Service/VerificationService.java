package com.userSystem.Service;

import com.userSystem.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VerificationService {


    @Autowired
    private  EmailService emailService;


    public void sendVerificationCode(String email) {
        String verificationCode = generateCode();

        // 保存驗證碼到緩存中
//        userRo.saveVerificationCode(email, verificationCode);
        //寄送信見到客戶mail
        emailService.sendSimpleMessage(email, "Your Verify code from UserSystem", verificationCode);
    }

    public boolean verifyCode(String email, String userEnteredCode) {
//        String storedCode = userRo.getVerificationCodeByEmail(email);
        String storedCode ="";
        return userEnteredCode.equals(storedCode);
    }
    public static String generateCode() {
        // 生成6位數字
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}


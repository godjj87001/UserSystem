package com.userSystem.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;
    private ResponseVo successResponse = ResponseVo.builder().httpCode(200).message("success").build();
    private ResponseVo errorResponse = ResponseVo.builder().httpCode(500).message("MailException").build();

    /**
     * send text mail
     *
     * @param receiver 收件者
     * @param subject  主旨
     * @param text     文件
     */
    public ResponseVo sendSimpleMessage(String receiver, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        try {

            message.setTo(receiver);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
        } catch (NullPointerException e) {
            log.error("" + e);
            return ResponseVo.builder().httpCode(400).message("Bad Request: "+e).build();

        } catch (Exception e) {
            log.error("" + e);
            return errorResponse;
        }
        return successResponse;
    }

    /**
     * send text mail post
     */
    public ResponseVo sendSimpleMessage(EmailRO emailRO) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailRO.getReceiver());
            message.setSubject(emailRO.getSubject());
            message.setText(emailRO.getContent());
            emailSender.send(message);
        } catch (Exception e) {
            return errorResponse;
        }
        return successResponse;
    }

}

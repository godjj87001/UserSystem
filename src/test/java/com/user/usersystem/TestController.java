package com.user.usersystem;

import com.userSystem.mail.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class TestController {
    @Autowired
    EmailService emailService;
    @GetMapping("/test")
    public String test() {
        return "hello ";
    }
    @GetMapping("/send")
    public String sendEmail() {
        emailService.sendSimpleMessage("txy52673@gmail.com", "Test Subject", "Hello, this is a test email.");
        return "Email sent successfully!";
    }
}

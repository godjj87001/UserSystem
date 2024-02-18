package com.user.usersystem;

import com.userSystem.Service.EmailService;
import com.userSystem.Service.JwtService;
import com.userSystem.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@ComponentScan
public class TestController {
    @Autowired
    EmailService emailService;
    @Autowired
    JwtService jwtService;
    @GetMapping("")
    public String test() {
        return "hello ";
    }
    @GetMapping("/send")
    public String sendEmail() {
        emailService.sendSimpleMessage("txy52673@gmail.com", "Test Subject", "Hello, this is a test email.");
        return "Email sent successfully!";
    }
    @GetMapping("/jwt")
    public String getJwt() {
       String jwt= jwtService.generateJwtToken("email","godjj87001@gmail.com");
        System.out.println(jwt);
        return jwt;
    }
}

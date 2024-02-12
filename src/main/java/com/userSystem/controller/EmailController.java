package com.userSystem.controller;

import com.userSystem.mail.EmailRO;
import com.userSystem.mail.EmailService;
import com.userSystem.mail.ResponseVo;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/email")
@ResponseBody

public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("")
    public ResponseVo sendMail(@RequestBody EmailRO emailRO) {
       return emailService.sendSimpleMessage(emailRO);
    }
}


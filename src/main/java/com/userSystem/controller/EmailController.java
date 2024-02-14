package com.userSystem.controller;

import com.userSystem.model.EmailRO;
import com.userSystem.Service.EmailService;
import com.userSystem.model.ResponseVo;
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


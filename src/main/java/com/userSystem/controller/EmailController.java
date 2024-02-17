package com.userSystem.controller;

import com.userSystem.model.EmailRO;
import com.userSystem.Service.EmailService;
import com.userSystem.model.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/email")
@ResponseBody

public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("")
    public ResponseEntity<?> sendMail(@RequestBody EmailRO emailRO) {
        return emailService.sendSimpleMessage(emailRO);
    }
}


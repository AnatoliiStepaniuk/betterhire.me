package com.sdehunt.controller;

import com.sdehunt.dto.SendEmailRequestDTO;
import com.sdehunt.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @RequestMapping(method = RequestMethod.POST, path = "/send")
    public void getUploadUrl(@RequestBody SendEmailRequestDTO request) {
        emailService.send(request.getTemplateId(), request.getSql());
    }

}

package com.sdehunt.controller;

import com.sdehunt.dto.SendEmailRequestDTO;
import com.sdehunt.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @RequestMapping(method = RequestMethod.POST, path = "/send")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getUploadUrl(@RequestBody SendEmailRequestDTO request) {
        emailService.sendUsersBySql(request.getTemplateId(), request.getSql());
    }

}

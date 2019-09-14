package com.sdehunt.controller;

import com.sdehunt.dto.TaskApplicationDTO;
import com.sdehunt.service.TaskApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task/application")
public class TaskApplicationController {

    @Autowired
    private TaskApplicationService service;

    @RequestMapping(method = RequestMethod.GET, path = "/company/{company}")
    public TaskApplicationDTO getUrls(@PathVariable("company") String company) {
        return service.getUrls(company);
    }

}

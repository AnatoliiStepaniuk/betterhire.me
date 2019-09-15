package com.sdehunt.controller;

import com.sdehunt.dto.TaskApplicationDTO;
import com.sdehunt.dto.TaskApplicationUrlsDTO;
import com.sdehunt.service.TaskApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task/application")
public class TaskApplicationController {

    @Autowired
    private TaskApplicationService service;

    @RequestMapping(method = RequestMethod.GET, path = "/company/{company}")
    public TaskApplicationUrlsDTO getUrls(@PathVariable("company") String company) {
        return service.getUrls(company);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/company/{company}")
    public void getUrls(@PathVariable("company") String company, @RequestBody TaskApplicationDTO req) {
        service.saveAndNotify(company, req);
    }

}

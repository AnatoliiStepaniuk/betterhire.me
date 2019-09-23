package com.sdehunt.controller;

import com.sdehunt.dto.EmptyDTO;
import com.sdehunt.dto.TaskApplicationDTO;
import com.sdehunt.dto.TaskApplicationUrlsDTO;
import com.sdehunt.service.TaskApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "/company/{company}"
    )
    public EmptyDTO postUrls(@PathVariable("company") String company, @RequestBody TaskApplicationDTO req) {
        service.saveAndNotify(company, req);
        return new EmptyDTO();
    }

}

package com.sdehunt.controller;

import com.sdehunt.commons.model.ExtendedUser;
import com.sdehunt.service.ExtendedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/users/extended")
public class ExtendedUserController {

    @Autowired
    private ExtendedUserService extendedUserService;

    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<ExtendedUser> getAll() {
        return extendedUserService.getAll();
    }

    @RequestMapping(path = "/task/{taskId}", method = RequestMethod.GET)
    public List<ExtendedUser> getForTask(@PathVariable String taskId) {
        return extendedUserService.getForTask(taskId.toLowerCase());
    }

    @RequestMapping(path = "/company/{company}", method = RequestMethod.GET)
    public List<ExtendedUser> getForCompany(@PathVariable("company") String company) {
        return extendedUserService.getForCompany(company);
    }

}

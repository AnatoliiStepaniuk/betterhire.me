package com.sdehunt.controller;

import com.sdehunt.Util;
import com.sdehunt.model.Task;
import com.sdehunt.repository.JdbiTaskRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @RequestMapping(method = RequestMethod.GET, path = "/test")
    public List<Task> test(){
        JdbiTaskRepository repo = new JdbiTaskRepository(Util.getDBConnection());
        return repo.getAll();
    }

}

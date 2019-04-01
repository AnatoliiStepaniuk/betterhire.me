package com.sdehunt.controller;

import com.sdehunt.model.Task;
import com.sdehunt.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class TaskController {

    @Autowired
    private TaskRepository tasks;

    @RequestMapping(method=GET, path="/tasks")
    public List<Task> getAllTasks(){
        return tasks.getAll();
    }

    @RequestMapping(method=GET, path="/tasks/{id}")
    public Task getTask(@PathVariable("id") String id){
        return tasks.get(id).orElse(null); //TODO test null
    }

}

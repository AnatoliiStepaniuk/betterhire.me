package com.sdehunt.controller;

import com.sdehunt.commons.model.Task;
import com.sdehunt.commons.model.impl.TaskImpl;
import com.sdehunt.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path = "/tasks")
public class TaskController {

    @Autowired
    private TaskRepository tasks;

    @RequestMapping(method = GET, path = "", produces = APPLICATION_JSON_VALUE)
    public List<Task> getAll() {
        return tasks.getAll();
    }

    @RequestMapping(method = GET, path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Task get(@PathVariable("id") String id) {
        return tasks.get(id).orElse(null);
    }

    @RequestMapping(method = POST, path = "")
    public void create(@RequestBody TaskImpl task) {
        tasks.create(task);
    }

    @RequestMapping(method = DELETE, path = "/{id}")
    public void delete(@PathVariable("id") String id) {
        tasks.delete(id);
    }
}

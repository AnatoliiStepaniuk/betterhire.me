package com.sdehunt.controller;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.Task;
import com.sdehunt.commons.model.impl.TaskImpl;
import com.sdehunt.dto.UpdateTaskDTO;
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

    @RequestMapping(method = GET, path = "", produces = APPLICATION_JSON_VALUE) // TODO use field `enabled`
    public List<Task> getAll() {
        return tasks.getAll();
    }

    @RequestMapping(method = GET, path = "/{taskId}", produces = APPLICATION_JSON_VALUE)
    public Task get(@PathVariable("taskId") String taskId) {
        return tasks.get(taskId).orElse(null);
    }

    @RequestMapping(method = PUT, path = "/{taskId}")
    public void update(@PathVariable("taskId") String taskId, @RequestBody UpdateTaskDTO updateTaskRequest) {
        tasks.update(
                new TaskImpl()
                        .setId(TaskID.of(taskId))
                        .setDescription(updateTaskRequest.getDescription())
        );
    }

    @RequestMapping(method = DELETE, path = "/{taskId}") // TODO add field enabled
    public void delete(@PathVariable("taskId") String taskId) {
        tasks.delete(taskId);
    }
}

package com.sdehunt.controller;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.ShortTask;
import com.sdehunt.commons.model.Task;
import com.sdehunt.dto.UpdateTaskDTO;
import com.sdehunt.exception.TaskNotFoundException;
import com.sdehunt.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path = "/tasks")
public class TaskController {

    @Autowired
    private TaskRepository tasks;

    @RequestMapping(method = GET, path = "", produces = APPLICATION_JSON_VALUE) // TODO use field `enabled`
    public List<Task> getAll(@RequestParam(required = false) boolean test) {
        return tasks.getAll(test);
    }

    @RequestMapping(method = GET, path = "/short", produces = APPLICATION_JSON_VALUE) // TODO use field `enabled`
    public List<ShortTask> getAllShort(@RequestParam(required = false) boolean test) { // TODO test
        return tasks.getAllShort(test);
    }

    @RequestMapping(method = GET, path = "/{taskId}", produces = APPLICATION_JSON_VALUE)
    public Task get(@PathVariable("taskId") String taskId) {
        return tasks.get(taskId).orElseThrow(TaskNotFoundException::new);
    }

    @RequestMapping(method = GET, path = "/{taskId}/short", produces = APPLICATION_JSON_VALUE)
    public ShortTask getShortTask(@PathVariable("taskId") String taskId) { // TODO test
        return tasks.getShort(taskId).orElseThrow(TaskNotFoundException::new);
    }

    @RequestMapping(method = PUT, path = "/{taskId}", produces = APPLICATION_JSON_VALUE)
    public void update(@PathVariable("taskId") String taskId, @RequestBody UpdateTaskDTO updateTaskRequest) {
        Task taskToUpdate = new Task();
        taskToUpdate.setId(TaskID.of(taskId));
        taskToUpdate.setDescription(updateTaskRequest.getDescription());
        taskToUpdate.setName(updateTaskRequest.getName());
        taskToUpdate.setImageUrl(updateTaskRequest.getImageUrl());
        taskToUpdate.setEnabled(updateTaskRequest.isEnabled());
        taskToUpdate.setSubmittable(updateTaskRequest.isSubmittable());
        taskToUpdate.setShortDescription(updateTaskRequest.getShortDescription());

        tasks.update(taskToUpdate);
    }

    @RequestMapping(method = DELETE, path = "/{taskId}")
    public void delete(@PathVariable("taskId") String taskId) {
        tasks.delete(taskId);
    }
}

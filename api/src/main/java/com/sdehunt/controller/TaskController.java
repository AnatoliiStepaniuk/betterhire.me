package com.sdehunt.controller;

import com.sdehunt.commons.model.ShortTask;
import com.sdehunt.commons.model.Task;
import com.sdehunt.dto.UpdateTaskDTO;
import com.sdehunt.dto.UrlDTO;
import com.sdehunt.exception.TaskNotFoundException;
import com.sdehunt.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path = "/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @RequestMapping(method = GET, path = "", produces = APPLICATION_JSON_VALUE) // TODO use field `enabled`
    public List<Task> getAll(@RequestParam(required = false) boolean test) {
        return taskService.getAll(test);
    }

    @RequestMapping(method = GET, path = "/company/{company}", produces = APPLICATION_JSON_VALUE)
    public List<Task> getForCompany(@PathVariable String company) {
        return taskService.getForCompany(company);
    }

    @RequestMapping(method = GET, path = "/short", produces = APPLICATION_JSON_VALUE) // TODO use field `enabled`
    public List<ShortTask> getAllShort(@RequestParam(required = false) boolean test) { // TODO test
        return taskService.getAllShort(test);
    }

    @RequestMapping(method = GET, path = "/{taskId}", produces = APPLICATION_JSON_VALUE)
    public Task get(@PathVariable("taskId") String taskId) {
        return taskService.get(taskId.toLowerCase()).orElseThrow(TaskNotFoundException::new);
    }

    @RequestMapping(method = GET, path = "/{taskId}/short", produces = APPLICATION_JSON_VALUE)
    public ShortTask getShortTask(@PathVariable("taskId") String taskId) {
        return taskService.getShort(taskId).orElseThrow(TaskNotFoundException::new);
    }

    @RequestMapping(method = GET, path = "/{taskId}/history", produces = APPLICATION_JSON_VALUE)
    public List<Task> getTaskHistory(@PathVariable("taskId") String taskId) {
        return taskService.getHistory(taskId.toLowerCase());
    }

    @RequestMapping(method = PUT, path = "/{taskId}", produces = APPLICATION_JSON_VALUE)
    public Task update(@PathVariable("taskId") String taskId, @RequestBody UpdateTaskDTO request) {
        Task taskToUpdate = new Task();
        taskToUpdate.setId(taskId.toLowerCase());
        taskToUpdate.setDescription(request.getDescription());
        taskToUpdate.setDescriptionUrl(request.getDescriptionUrl());
        taskToUpdate.setName(request.getName());
        taskToUpdate.setImageUrl(request.getImageUrl());
        taskToUpdate.setEnabled(request.isEnabled());
        taskToUpdate.setSubmittable(request.isSubmittable());
        taskToUpdate.setShortDescription(request.getShortDescription());
        taskToUpdate.setRequirements(request.getRequirements());
        taskToUpdate.setInputFilesUrl(request.getInputFilesUrl());
        taskToUpdate.setTags(request.getTags());
        taskToUpdate.setLanguages(request.getLanguages());
        taskToUpdate.setCompany(request.getCompany());
        taskToUpdate.setCity(request.getCity());
        taskToUpdate.setJob(request.getJob());
        taskToUpdate.setJobUrl(request.getJobUrl());
        taskToUpdate.setType(request.getType());
        taskToUpdate.setEmails(request.getEmails());

        return taskService.update(taskToUpdate);
    }

    @RequestMapping(method = POST, path = "", produces = APPLICATION_JSON_VALUE)
    public Task create(@RequestBody Task task) {
        return taskService.create(task);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(method = DELETE, path = "/{taskId}")
    public void delete(@PathVariable("taskId") String taskId) {
        taskService.delete(taskId);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/{taskId}/file/{file}/upload")
    public UrlDTO getUploadUrl(@PathVariable("taskId") String taskId, @PathVariable("file") String file) {
        return new UrlDTO().setUrl(taskService.uploadUrl(taskId, file));
    }
}

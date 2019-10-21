package com.sdehunt.service;

import com.sdehunt.commons.model.ShortTask;
import com.sdehunt.commons.model.Task;
import com.sdehunt.commons.s3.S3Client;
import com.sdehunt.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

public class TaskService {

    private final String BUCKET = "betterhire-tasks";
    private TaskRepository tasks;
    private S3Client s3Client;
    private ReadmeService readmeService;

    public TaskService(TaskRepository tasks, S3Client s3Client, ReadmeService readmeService) {
        this.tasks = tasks;
        this.s3Client = s3Client;
        this.readmeService = readmeService;
    }

    public String uploadUrl(String taskId, String fileName) {
        return s3Client.uploadUrl(BUCKET, taskId + "/" + fileName);
    }

    public Task update(Task task) {
        Task updated = tasks.update(task);
        if (task.getDescription() != null) {
            readmeService.update(task.getId(), task.getDescription());
        }
        return updated;
    }

    public Task create(Task task) {
        Task created = tasks.create(task);
        readmeService.create(task.getId(), task.getDescription());
        return created;
    }

    public void delete(String taskId) {
        tasks.delete(taskId);
    }

    public List<Task> getHistory(String taskId) {
        return tasks.getHistory(taskId);
    }

    public Optional<ShortTask> getShort(String taskId) {
        return tasks.getShort(taskId);
    }

    public Optional<Task> get(String taskId) {
        return tasks.get(taskId);
    }

    public List<ShortTask> getAllShort(boolean test) {
        return tasks.getAllShort(test);
    }

    public List<Task> getForCompany(String company) {
        return tasks.getForCompany(company);
    }

    public List<Task> getAll(boolean test) {
        return tasks.getAll(test);
    }
}

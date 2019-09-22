package com.sdehunt.service;

import com.sdehunt.commons.s3.S3Client;
import com.sdehunt.dto.TaskApplicationDTO;
import com.sdehunt.dto.TaskApplicationUrlsDTO;
import com.sdehunt.repository.TaskApplicationRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TaskApplicationService {

    private final String BUCKET = "betterhire-task-application";
    private final String JOB = "job";
    private final String TASK = "task";
    private final String TEMPLATE = "TaskApplicationInternal";
    private final long TTL_MILLIS = 60 * 60 * 1000;

    private S3Client s3Client;
    private TaskApplicationRepository taskApplicationRepository;
    private EmailService emailService;

    public TaskApplicationService(
            S3Client s3Client,
            TaskApplicationRepository taskApplicationRepository,
            EmailService emailService
    ) {
        this.s3Client = s3Client;
        this.taskApplicationRepository = taskApplicationRepository;
        this.emailService = emailService;
    }

    public TaskApplicationUrlsDTO getUrls(String company) {
        String taskId = UUID.randomUUID().toString();
        String jobUrl = s3Client.uploadUrl(BUCKET, company + "/" + taskId + "/" + JOB, TTL_MILLIS);
        String taskUrl = s3Client.uploadUrl(BUCKET, company + "/" + taskId + "/" + TASK, TTL_MILLIS);
        return new TaskApplicationUrlsDTO()
                .setJobUrl(jobUrl)
                .setTaskUrl(taskUrl);
    }

    public void saveAndNotify(String company, TaskApplicationDTO req) {
        String taskId = extractTaskId(req.getJobUrl(), company);
        String jobUrl = trim(req.getJobUrl());
        String taskUrl = trim(req.getTaskUrl());
        String contact = req.getContact();
        taskApplicationRepository.save(company, contact, taskId, jobUrl, taskUrl);
        notify(company, contact, jobUrl, taskUrl);
    }

    private String extractTaskId(String url, String company) {
        return url.split(company)[1].split("/")[1];
    }

    private void notify(String company, String contact, String jobUrl, String taskUrl) {
        Map<String, Object> params = new HashMap<>();
        params.put("company", company);
        params.put("contact", contact);
        params.put("jobUrl", jobUrl);
        params.put("taskUrl", taskUrl);
        emailService.sendUsersBySql(TEMPLATE, "email = 'hey@betterhire.me'", params);
    }

    /**
     * Returns url without query parameters
     */
    private String trim(String url) {
        return url.indexOf('?') < 0 ? url : url.substring(0, url.indexOf('?'));
    }
}

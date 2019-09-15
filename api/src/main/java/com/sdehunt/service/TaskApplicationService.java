package com.sdehunt.service;

import com.sdehunt.commons.s3.S3Client;
import com.sdehunt.dto.TaskApplicationDTO;
import com.sdehunt.repository.TaskApplicationRepository;

import java.util.UUID;

public class TaskApplicationService {

    private final String BUCKET = "betterhire-task-application";
    private final String JOB = "job";
    private final String TASK = "task";
    private final long TTL_MILLIS = 60 * 60 * 1000;

    private S3Client s3Client;
    private TaskApplicationRepository taskApplicationRepository;

    public TaskApplicationService(S3Client s3Client, TaskApplicationRepository taskApplicationRepository) {
        this.s3Client = s3Client;
        this.taskApplicationRepository = taskApplicationRepository;
    }

    public TaskApplicationDTO getUrls(String company) {
        String taskId = UUID.randomUUID().toString();
        String jobUrl = s3Client.uploadUrl(BUCKET, company + "/" + taskId + "/" + JOB, TTL_MILLIS);
        String taskUrl = s3Client.uploadUrl(BUCKET, company + "/" + taskId + "/" + TASK, TTL_MILLIS);
        taskApplicationRepository.save(company, taskId, trim(jobUrl), trim(taskUrl));
        return new TaskApplicationDTO()
                .setJobUrl(jobUrl)
                .setTaskUrl(taskUrl);
    }

    /**
     * Returns url without query parameters
     */
    private String trim(String url) {
        return url.indexOf('?') < 0 ? url : url.substring(0, url.indexOf('?'));
    }
}

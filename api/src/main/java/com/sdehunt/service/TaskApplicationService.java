package com.sdehunt.service;

import com.sdehunt.commons.s3.S3Client;
import com.sdehunt.dto.TaskApplicationDTO;

import java.util.UUID;

public class TaskApplicationService {

    private final String BUCKET = "betterhire-task-application";
    private final String JOB = "job";
    private final String TASK = "task";
    private S3Client s3Client;

    public TaskApplicationService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public TaskApplicationDTO getUrls(String company) {
        String taskId = UUID.randomUUID().toString(); // TODO save it to DB
        String jobUrl = s3Client.uploadUrl(BUCKET, company + "/" + taskId + "/" + JOB);
        String taskUrl = s3Client.uploadUrl(BUCKET, company + "/" + taskId + "/" + TASK);
        return new TaskApplicationDTO()
                .setJobUrl(jobUrl)
                .setTaskUrl(taskUrl);
    }
}

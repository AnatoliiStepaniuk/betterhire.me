package com.sdehunt.service;

import com.sdehunt.commons.s3.S3Client;

public class TaskService {

    private final String BUCKET = "betterhire-tasks";
    private S3Client s3Client;

    public TaskService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadUrl(String taskId, String fileName) {
        return s3Client.uploadUrl(BUCKET, taskId + "/" + fileName);
    }

}

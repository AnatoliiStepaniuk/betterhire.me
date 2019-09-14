package com.sdehunt.service;

import com.sdehunt.commons.s3.S3Client;

public class CvService {

    private final String bucketName = "betterhire-cv";
    private S3Client s3Client;

    public CvService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadUrl(String userId, String fileName) {
        return s3Client.uploadUrl(bucketName, userId + "/" + fileName);
    }

}

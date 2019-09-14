package com.sdehunt.commons.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import java.time.Instant;
import java.util.Date;

public class S3Client {
    private final long ttlMillis = 60 * 1000;
    private AmazonS3 s3Client;

    public S3Client() {
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new ProfileCredentialsProvider(".aws/credentials", null))
                .withRegion("eu-central-1")
                .build();
    }

    public String uploadUrl(String bucketName, String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(Date.from(Instant.now().plusMillis(ttlMillis)));
        return s3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

}

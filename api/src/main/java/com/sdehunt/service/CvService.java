package com.sdehunt.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import java.time.Instant;
import java.util.Date;

public class CvService {

    private final String bucketName = "betterhire-cv";
    private final long ttlMillis = 60 * 1000; // TODO unhardcode
    private Regions clientRegion = Regions.EU_CENTRAL_1;
    private AmazonS3 s3Client;

    public CvService() {
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new ProfileCredentialsProvider())
                .withRegion(clientRegion)
                .build();
    }

    public String uploadUrl(String userId, String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, userId + "/" + fileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(Date.from(Instant.now().plusMillis(ttlMillis)));
        return s3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    public String downloadUrl(String userId) {
        // TODO first get cvUrl from user.
        return null; // TODO
    }

}

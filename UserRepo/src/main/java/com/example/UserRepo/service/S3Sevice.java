package com.example.UserRepo.service;

import com.example.UserRepo.exceptions.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.Properties;

@Service
public class S3Sevice {
    private final S3Client s3Client;
    @Autowired
    private Properties properties;
    private static Logger logger = LogManager.getLogger(S3Sevice.class);

    public S3Sevice(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void putObject(String bucketName, String key, byte[] file) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.putObject(objectRequest, RequestBody.fromBytes(file));
    }

    //Regrex pattern = {"$profileImage/"}
    //
    public byte[] getObject(String bucketName, String key) {
        try {
            if (key == null || !key.contains(properties.getProperty("avatar"))) {
                return null;
            }
            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            ResponseInputStream<GetObjectResponse> object = s3Client.getObject(objectRequest);
            return object.readAllBytes();
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    public void deleteObject(String bucketName, String key) {
        if (!key.contains("profileImage/")) {
            return;
        }
        DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.deleteObject(objectRequest);
    }
}

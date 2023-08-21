package com.LifeTales.global.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class RequestIMGService {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    public InputStream getImageInputStream(String objectKey) throws IOException {
        S3Object s3Object = amazonS3Client.getObject(bucket, objectKey);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        return inputStream;
    }
}

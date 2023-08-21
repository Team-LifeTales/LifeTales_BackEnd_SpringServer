package com.LifeTales.domain.test.controller;

import com.LifeTales.domain.test.service.S3TestService;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/test/s3")
@RequiredArgsConstructor
public class s3TestController {
    private final AmazonS3Client amazonS3Client;
    private final S3TestService s3TestService;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @PostMapping("/upload/")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            String fileName=file.getOriginalFilename();
            String fileUrl= "https://" + bucket + "/test" +fileName;
            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket,fileName,file.getInputStream(),metadata);
            return ResponseEntity.ok(fileUrl);
        } catch (SdkClientException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{objectKey}/")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String objectKey) throws IOException {
        InputStream imageInputStream = s3TestService.getImageInputStream(objectKey);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // 이미지 타입에 따라 변경
                .body(new InputStreamResource(imageInputStream));
      }



}

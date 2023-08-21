package com.LifeTales.domain.feed.service;


import com.LifeTales.domain.feed.domain.Feed;
import com.LifeTales.domain.feed.domain.FeedImageList;
import com.LifeTales.domain.feed.repository.DTO.FeedUploadDTO;
import com.LifeTales.domain.feed.repository.FeedImageListRepository;
import com.LifeTales.domain.feed.repository.FeedRepository;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.random.*;
import java.io.ByteArrayInputStream;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class FeedService {
    @Autowired
    private final FeedRepository feedRepository;
    private final FeedImageListRepository feedImageListRepository;
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String Feed_upload_service(FeedUploadDTO uploadData){
        try {
            String fileName = "";
            log.info("Feed_upload_service Start >> id {}",uploadData.getContent() );

            Feed newFeed = Feed.builder()
                    .familySeq(uploadData.getFamilySeq())
                    .userSeq(uploadData.getUserSeq())
                    .content(uploadData.getContent())
                    .build();

            Feed savedFeed = feedRepository.save(newFeed);
            Long savedFeedId = savedFeed.getSeq();

            if(uploadData.getUploadImages() != null){
                int countImage = 1;
                try{
                    for (byte[] uploadImage : uploadData.getUploadImages() ){
                        Random random = new Random();
                        int coin = random.nextInt(100000);
                        fileName = "Feed-"+uploadData.getUserSeq()+"$"+savedFeedId.toString()+"$"+countImage+"$"+coin;
                        countImage = countImage+1;
                        //String fileUrl= "https://" + bucket + "/lifeTales" +fileName;
                        byte[] imageData = uploadImage;
                        String mimeType = "image/jpeg";


                        ObjectMetadata metadata = new ObjectMetadata();
                        metadata.setContentLength(imageData.length);
                        metadata.setContentType(mimeType);

                        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);

                        amazonS3Client.putObject(bucket, fileName, inputStream, metadata);

                        feedImageListRepository.save(
                                FeedImageList.builder()
                                        .feedSeq(savedFeedId)
                                        .feedImageURL(fileName)
                                        .build()
                        );
                    }


                }catch (SdkClientException e) {
                    log.info("fail - s3 upload fail" + e);
                    return "fail - s3 upload fail";

                }
            }else{
                fileName = "feedImage"; // 없는 경우에는 그냥 fileName을 null이라고 해놓음 일단 -> 기본 이미지로 변경하면 될듯
            }

            return "Success";
        } catch (DataAccessException ex) {
            // 데이터베이스 예외 처리
            log.error("데이터베이스 예외 발생", ex);
            // 다른 처리 로직 추가

            return "DataAccessException";
        } catch (RuntimeException ex) {
            // 런타임 예외 처리
            log.error("런타임 예외 발생", ex);
            // 다른 처리 로직 추가

            return "RuntimeException";
        }
    }



}

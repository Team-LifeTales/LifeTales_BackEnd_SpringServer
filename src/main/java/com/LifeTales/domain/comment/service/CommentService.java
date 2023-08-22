package com.LifeTales.domain.comment.service;

import com.LifeTales.common.User.UserIdChecker;
import com.LifeTales.domain.comment.domain.Comment;
import com.LifeTales.domain.comment.repository.CommentRepository;
import com.LifeTales.domain.comment.repository.DTO.CommentUploadDTO;
import com.LifeTales.domain.user.domain.User;
import com.LifeTales.domain.user.repository.DTO.UserDataDTO;
import com.LifeTales.domain.user.repository.DTO.UserSignUpDTO;
import com.LifeTales.domain.user.repository.DTO.UserSignUpStep2DTO;
import com.LifeTales.domain.user.repository.UserRepository;
import com.LifeTales.global.s3.RequestIMGService;
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
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CommentService {

    //user Service
    @Autowired
    private final CommentRepository commentRepository;
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final RequestIMGService imgService;
    @PersistenceContext
    private EntityManager entityManager;

    public String comment_upload_service(@RequestBody CommentUploadDTO commentUploadDTO){
        try {
            commentRepository.save(
                    Comment.builder()
                            .userSeq(commentUploadDTO.getUserSeq())
                            .feedSeq(commentUploadDTO.getFeedSeq())
                            .content(commentUploadDTO.getContent())
                            .build()
            );
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

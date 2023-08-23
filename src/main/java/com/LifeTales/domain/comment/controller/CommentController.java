package com.LifeTales.domain.comment.controller;

import com.LifeTales.domain.comment.repository.DTO.CommentUploadDTO;
import com.LifeTales.domain.comment.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {
    private final ObjectMapper objectMapper;
    private final CommentService commentService;
    public CommentController(ObjectMapper objectMapper, CommentService commentService) {
        this.objectMapper = objectMapper;
        this.commentService = commentService;

    }

    @PostMapping("/upload/comment/")
    public ResponseEntity CommentUpload(@RequestBody CommentUploadDTO commentUploadDTO) {
        log.info("CommentUpload Start - need Data \nuser : {} , feed : {}, content : {} , " ,commentUploadDTO.getUserId(),commentUploadDTO.getFeedSeq(), commentUploadDTO.getContent());

        log.info("CommentUpload service logic Start");
        String return_text = commentService.comment_upload_service(commentUploadDTO);
        log.info("CommentUpload service logic end");

        ResponseEntity<String> responseEntity;
        if ("Success".equals(return_text)) {
            log.info("CommentUplaod service Success , {}", commentUploadDTO.getUserId());
            // 댓글 업로드 성공시 처리
            responseEntity = ResponseEntity.ok("Comment Upload Success");
        } else if ("DataAccessException".equals(return_text)) {
            log.info("CommentUplaod service DataAccessException , {}", commentUploadDTO.getUserId());
            // 데이터베이스 예외 발생한 경우 처리
            responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DataAccessException");
        } else if ("RuntimeException".equals(return_text)) {
            log.info("CommentUplaod service RuntimeException , {}", commentUploadDTO.getUserId());
            // 런타임 예외 발생한 경우 처리
            responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("RuntimeException");
        } else {
            log.info("CommentUplaod service don't Know Error , Please contact about Developer, {}", commentUploadDTO.getUserId());
            // 기타 예외나 다른 경우 처리
            responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("don't Know Error");
        }

        // Service (Create Logic End)
        return responseEntity;




    }
    @GetMapping("/read/MasterComment/{feedSeq}/")
    public ResponseEntity MasterCommentListRead(Long feedSeq){
        log.info("MasterCommentList Read Controller Start >>  {}" , feedSeq);

        return null;
    }


}

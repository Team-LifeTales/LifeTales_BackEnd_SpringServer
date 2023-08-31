package com.LifeTales.domain.comment.controller;

import com.LifeTales.domain.comment.repository.DTO.CommentUploadDTO;
import com.LifeTales.domain.comment.repository.DTO.MasterCommentReadDTO;
import com.LifeTales.domain.comment.repository.DTO.SlaveCommentReadDTO;
import com.LifeTales.domain.comment.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/comment")
@CrossOrigin(origins = {"http://172.20.144.1:3000", "http://3.39.251.34:3000"})
public class CommentController {
    private final ObjectMapper objectMapper;
    private final CommentService commentService;
    public CommentController(ObjectMapper objectMapper, CommentService commentService) {
        this.objectMapper = objectMapper;
        this.commentService = commentService;

    }

    @PostMapping("/upload/comment")
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
    // ex URL http://localhost:8080/api/v1/comment/read/MasterComment/1?page=0
    @GetMapping("/read/MasterComment/{feedSeq}")
    public ResponseEntity<Page<MasterCommentReadDTO>> masterCommentListRead(
            @PathVariable(required = true) Long feedSeq,
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNum,
            Pageable pageable
            )
     {
        log.info("MasterCommentList Read Controller Start >> {}", feedSeq);
        //pageNumber  = (pageNumber == 0)? 0: (pageNumber -1);

        Page<MasterCommentReadDTO> commentPage = commentService.master_comment_read_service(feedSeq , pageNum , pageable );

        return ResponseEntity.ok(commentPage);
    }

    @GetMapping("/read/MasterComment/{feedSeq}/{masterCommentSeq}")
    public ResponseEntity<Page<SlaveCommentReadDTO>> slaveCommentListRead(
            @PathVariable(required = true) Long feedSeq,
            @PathVariable(required = true) Long masterCommentSeq,
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNum,
            Pageable pageable
    )
    {
        log.info("slaveCommentListRead Read Controller Start >> {}", feedSeq);
        //pageNumber  = (pageNumber == 0)? 0: (pageNumber -1);

        Page<SlaveCommentReadDTO> commentPage = commentService.slave_comment_read_service(feedSeq , pageNum , pageable , masterCommentSeq);
        if(commentPage == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }else{
            return ResponseEntity.ok(commentPage);
        }

    }
}

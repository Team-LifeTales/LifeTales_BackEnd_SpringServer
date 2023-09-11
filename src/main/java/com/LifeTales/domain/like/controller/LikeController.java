package com.LifeTales.domain.like.controller;

import com.LifeTales.common.User.LikeChecker;
import com.LifeTales.domain.comment.service.CommentService;
import com.LifeTales.domain.feed.domain.Feed;
import com.LifeTales.domain.feed.repository.FeedRepository;
import com.LifeTales.domain.like.repository.DTO.CheckLikeDTO;
import com.LifeTales.domain.like.repository.LikeRepository;
import com.LifeTales.domain.like.service.LikeService;
import com.LifeTales.domain.user.domain.User;
import com.LifeTales.domain.user.repository.UserRepository;
import com.LifeTales.global.util.UseTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/like")
public class LikeController {
    private final ObjectMapper objectMapper;

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    private final LikeService likeService;

    private final FeedRepository feedRepository;

    private final UseTokenUtil tokenUtil;

    public LikeController(ObjectMapper objectMapper, CommentService commentService, LikeRepository likeRepository, UserRepository userRepository, LikeService likeService, LikeChecker likeChecker, FeedRepository feedRepository, UseTokenUtil tokenUtil) {
        this.objectMapper = objectMapper;

        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.likeService = likeService;
        this.feedRepository = feedRepository;
        this.tokenUtil = tokenUtil;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping("/check/like")
    public ResponseEntity checkLikeForFeed( HttpServletRequest request,
                                                @RequestParam("feedSeq") Long feedSeq,
                                                @RequestParam("checkType") boolean checkType) throws IOException {
        /**
         * do
         * 하트를 눌렀을 때 동작하는 코드
         * 만약 빈 하트를 눌렀으면 하트가 채워지고, 채워진 하트를 눌렀으면 비워져야함
         * 처음 누른게 아니면 기존 db에서 변경, 처음 누른거라면 db에 생성해야함
         * 피드있는지 체크, user있는지 체크, feed와 user가 동시에 있는게 db에 있는지 체크
         * 있으면 boolean type 변경, 없으면 boolean type 생성해서 db에 저장
         */

        boolean checkFeedSeqExists = feedRepository.existsBySeq(feedSeq);
        String id = tokenUtil.findUserIdForJWT(request);
        boolean checkUserIdExists = userRepository.existsById(id);
        log.info(" checkLikeForFeed feed, user check Start");

        if(checkFeedSeqExists && checkUserIdExists){
            log.info(" checkLikeForFeed Start >> feed exists, user exists");
            User user = userRepository.findById(id);
            Feed feed = entityManager.find(Feed.class, feedSeq);
            CheckLikeDTO checkLikeDTO = new CheckLikeDTO();
            checkLikeDTO.setUserId(id);
            checkLikeDTO.setFeedSeq(feedSeq);
            if (checkType){
                checkLikeDTO.setFeedSeq(feedSeq);
            }else{
                checkLikeDTO.setFeedSeq(feedSeq);
            }
            log.info(" check likeDB already exists for user and feed"); //이미 DB에 있는지 확인
            boolean likecheck = likeRepository.existsByUserIdAndFeedSeq(user,feed);
            if(likecheck){
                log.info(" likeDB already existed"); //이미 존재 변경만 하면됨
                if (checkType){
                    checkLikeDTO.setChecked(true);
                }else{
                    checkLikeDTO.setChecked(false);
                }
                likeService.change_like_service(checkLikeDTO);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("기존에 체크된 좋아요 변경");
                //존재하니까 바꾸기만

            }else{
                //여기선 DB에 생성
                log.info(" likeDB not exists"); //새로 생성
                checkLikeDTO.setChecked(true);
                likeService.create_like_service(checkLikeDTO);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("좋아요 DB에 생성 및 체크");
            }
        }else{
            log.info("checkLikeForFeed feed, user check >> fail -> 존재하지 않는 user거나 feed임 >> userId : {} , feedSeq : {}" , id, feedSeq);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자가 존재하지 않거나 존재하지 않는 피드입니다.");
        }
    }
}

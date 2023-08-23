package com.LifeTales.domain.comment.service;
import com.LifeTales.domain.comment.domain.Comment;
import com.LifeTales.domain.comment.domain.CommentRole;
import com.LifeTales.domain.comment.repository.DTO.CommentUploadDTO;
import com.LifeTales.domain.comment.repository.MasterCommentRepository;
import com.LifeTales.domain.comment.repository.SlaveCommentRepository;
import com.LifeTales.domain.feed.domain.Feed;
import com.LifeTales.domain.feed.repository.FeedRepository;
import com.LifeTales.domain.user.domain.User;
import com.LifeTales.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CommentService {
    @Autowired
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    //comment Repository
    private final MasterCommentRepository masterCommentRepository;
    private final SlaveCommentRepository slaveCommentRepository;


    public String comment_upload_service(@RequestBody CommentUploadDTO commentUploadDTO){
        /**
         * do
         * feed 존재여부..
         * id 존재여부 ..
         * comment role check
         * comment role slave -> master Comment exist
         * upload...
         */
        String userId = commentUploadDTO.getUserId();
        Long feedSeq = commentUploadDTO.getFeedSeq();
        CommentRole role = commentUploadDTO.getRole();
        //log.info("{} {} {}", userId , feedSeq , role);
        String return_text = check_comment_data(userId,feedSeq,role);
        if (return_text.equals("check Sum")){
            //upload comment Data Start

            /**
             * 댓글을 올리기위해서 User seq , Feed Seq set
             */
            User user = userRepository.findById(userId);
            Feed feed = feedRepository.findBySeq(feedSeq);

            if(role.equals(CommentRole.MASTER_COMMENT)){
                //master Comment
                String text = upload_master_comment_db_service(commentUploadDTO , user , feed);
                if(text.equals("Success")){
                    return "Success";
                }else{
                    return text;
                }

            }else{
                //slave Comment
                if(masterCommentRepository.existsById(commentUploadDTO.getMasterCommentSeq())){
                    //feed 존재
                    Comment masterComment = masterCommentRepository.findBySeq(commentUploadDTO.getMasterCommentSeq());
                    String text =  upload_slave_comment_db_service(commentUploadDTO , user , feed , masterComment);

                    return "Success";
                }else{
                    //feed 없음
                    return "feed is not exist";
                }

            }
        }else{
            //refuse comment
            return return_text;
        }
        
    }


    private String check_comment_data(String userId , Long feedSeq , CommentRole role){
        if (userRepository.existsById(userId) && feedRepository.existsBySeq(feedSeq)) {
            log.info("feed & id exist >> ");
            if(role.equals(CommentRole.MASTER_COMMENT) || role.equals(CommentRole.SLAVE_COMMENT)){
                // 정상.. 확인
                return "check Sum";
            }else{
                return "role Name wrong";
            }
        }else{
            log.info("feed or id not exist >> ");
            return "feed or id is not exist";
        }

    }

    private String upload_master_comment_db_service(CommentUploadDTO commentUploadDTO , User user , Feed feed){
        try {
                masterCommentRepository.save(
                        Comment.builder()
                                .content(commentUploadDTO.getContent())
                                .role(commentUploadDTO.getRole())
                                .userId(user)
                                .feedSeq(feed)
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
    private String upload_slave_comment_db_service(CommentUploadDTO commentUploadDTO , User user , Feed feed , Comment masterComment){
        try{
            slaveCommentRepository.save(
                    Comment.builder()
                            .content(commentUploadDTO.getContent())
                            .role(commentUploadDTO.getRole())
                            .userId(user)
                            .feedSeq(feed)
                            .masterComment(masterComment)
                            .build()
            );
            return "Success";
        }catch (DataAccessException ex) {
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

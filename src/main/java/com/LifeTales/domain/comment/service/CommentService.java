package com.LifeTales.domain.comment.service;
import com.LifeTales.domain.comment.domain.Comment;
import com.LifeTales.domain.comment.domain.CommentRole;
import com.LifeTales.domain.comment.repository.DTO.CommentUploadDTO;
import com.LifeTales.domain.comment.repository.DTO.MasterCommentReadDTO;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    public List<String> master_comment_read_service(Long feedSeq){
        /**
         * to do
         * feed 존재여부..
         * comment 조회 - 해당 피드에 있는 master comment들에 대한 리스트
         * 주어야할 정보 .. 댓글을 단
         * 유저 NickName , profile
         * comment 에 대한 내용 , 최종일 , slave 댓글이 있는지에 대한 여부..
         */
        log.info("feedFinder Start");
        if(feedRepository.existsBySeq(feedSeq)){
            //feed exist

            List<MasterCommentReadDTO> returnList = new ArrayList<>();


            String userProfile;
            String userNickName;
            String commentContent;
            Long existSalve;
            LocalDateTime updateTime;

            Feed feedData = feedRepository.findBySeq(feedSeq);
            List<Comment> commentList = masterCommentRepository.findByFeedSeqAndIsDELETED(feedData , false);
            for(Comment commentData : commentList){
                userNickName = commentData.getUserId().getNickName();
                userProfile = commentData.getUserId().getProfileIMG();
                commentContent = commentData.getContent();
                existSalve = commentData.getExistSalve();
                updateTime = commentData.getIsUpdated();

                MasterCommentReadDTO masterCommentReadDTO = new MasterCommentReadDTO();
                masterCommentReadDTO.setUserProfile(userProfile);
                masterCommentReadDTO.setUserNickName(userNickName);
                masterCommentReadDTO.setCommentContent(commentContent);
                masterCommentReadDTO.setExistSalve(existSalve);
                masterCommentReadDTO.setIsUpdated(updateTime);

                returnList.add(masterCommentReadDTO);
            }
            //printCommentList(commentList); //확인용임
            printReturnList(returnList); //확인용임
        }else{
            //feed not exist
        }
            return  null;
    }




    public String comment_upload_service(CommentUploadDTO commentUploadDTO){
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

                    if(text.equals("Success")){
                        return "Success";
                    }else{
                        return text;
                    }

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
                                .existSalve(0L)
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
            //master 댓글 ++
            // 여기서 masterComment의 existSalve 값을 업데이트
            masterComment.setExistSalve(masterComment.getExistSalve() + 1);
            masterCommentRepository.save(masterComment);

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
    public void printCommentList(List<Comment> commentList) {
        for (Comment comment : commentList) {
            System.out.println("Comment Seq: " + comment.getSeq());
            System.out.println("Content: " + comment.getContent());
            System.out.println("Role: " + comment.getRole());
            System.out.println("Exist Slave: " + comment.getExistSalve());
            System.out.println("User ID: " + comment.getUserId().getId());
            System.out.println("Is Created: " + comment.getIsCreated());
            System.out.println("Is Updated: " + comment.getIsUpdated());
            System.out.println("Is Deleted: " + comment.isDELETED());
            System.out.println("---------------------------");
        }
    }
    public void printReturnList(List<MasterCommentReadDTO> returnList) {
        for (MasterCommentReadDTO dto : returnList) {
            System.out.println("User Profile: " + dto.getUserProfile());
            System.out.println("User NickName: " + dto.getUserNickName());
            System.out.println("Comment Content: " + dto.getCommentContent());
            System.out.println("Exist Slave: " + dto.getExistSalve());
            System.out.println("Update Time: " + dto.getIsUpdated());
            System.out.println("---------------------------");
        }
    }


}

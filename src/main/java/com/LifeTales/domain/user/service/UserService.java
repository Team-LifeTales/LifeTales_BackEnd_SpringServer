package com.LifeTales.domain.user.service;

import com.LifeTales.common.User.UserIdChecker;
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
public class UserService {

    //user Service
    @Autowired
    private final UserRepository userRepository;
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final UserIdChecker userIdChecker;

    private final RequestIMGService imgService;
    @PersistenceContext
    private EntityManager entityManager;

    public String user_signUp_service(@RequestBody UserSignUpDTO userSignUpdata){
        try {
            userRepository.save(
                    User.builder()
                            .id(userSignUpdata.getId())
                            .pwd(userSignUpdata.getPwd())
                            .name(userSignUpdata.getName())
                            .nickName(userSignUpdata.getNickName())
                            .birthDay(userSignUpdata.getBirthDay().atStartOfDay())
                            .phoneNumber(userSignUpdata.getPhoneNumber())
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

    public String user_signUp_step2_service(@RequestBody UserSignUpStep2DTO data){
        //프로파일 이미지 & 소개글
        log.info("user_signUp_step2_service Start >> id {}",data.getId() );
        if(data.getProfileIMG() != null){
            //프로파일 이미지가 있는경우
            try{
                String fileName = "UserProfile-"+data.getId();
                String fileUrl= "https://" + bucket + "/lifeTales" +fileName;
                byte[] imageData = data.getProfileIMG();
                String mimeType = "image/jpeg";

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(imageData.length);
                metadata.setContentType(mimeType);

                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);

                amazonS3Client.putObject(bucket, fileName, inputStream, metadata);
                User user  = userRepository.findSeqById(data.getId());
                Long userSeq = user.getSeq();
                Optional<String> fileUrlOptional = Optional.of(fileUrl);
                boolean checkMergeUserProfile = user_signUp_step2_db_service(data.getIntro() , fileUrlOptional ,userSeq);
                if(checkMergeUserProfile){
                    return "Success";
                }else{
                    return "Error - merge Error";
                }

            }catch (SdkClientException e) {
                log.info("fail - s3 upload fail" + e);
                return "fail - s3 upload fail";

            }catch (Exception e) {
                // 기타 예외 처리
                e.printStackTrace();
                return "Error";
            }
        }else{
            //없는 경우
            return "fail - s3 upload fail";
        }

    }

    private boolean user_signUp_step2_db_service(String intro , Optional<String> fileUrl , Long userSeq){
        /**
         * db에 Merge 하기위한 메서드 입니다.
         */
        try {
            User user = entityManager.find(User.class , userSeq);
            if (user != null){
                user.setIntroduce(intro);

                if (fileUrl.isPresent()) {
                    String fileURLMerge = fileUrl.get();
                    user.setProfileIMG(fileURLMerge);
                }
                try {
                    entityManager.merge(user);
                    return true;
                }catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }

            }else{
                return false;
            }
        }
       catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            return false;
        }
    }

    public UserDataDTO getDataForUser(String userId) throws IOException {
        /**
         * 해야할것
         * 아이디체크 (존재여부)
         * 존재하는 경우 - 데이터 저장 .. 유저 프로필이미지 , 유저 닉네임 , 유저 아이디 , 유저 코드
         * 유저 프로필이미지 - 존재하는지 - 있으면 s3에서 받아와야함
         * 나머지 만들어서 주기
         */

        log.info("getDataForUser Start >> {}",userId);
        boolean idCheck = userIdChecker.doesIdExist(userId);
        log.info("getDataForUser checkId result >> {}" , idCheck);
        if(idCheck){
            log.info("getDataForUser checkId Success >> {}" , userId);
            UserDataDTO userDataDTO = new UserDataDTO();
            User userData = userRepository.findById(userId);
            //데이터 셋업
            userDataDTO.setId(userData.getId());
            userDataDTO.setNickName(userData.getNickName());
            userDataDTO.setSeq(userData.getSeq());
            //s3에 요청하기 전 확인하기
            if(userData.getProfileIMG() != null){
                log.info("getDataForUser S3 connect Start");
                String objectKey = "UserProfile-"+userId;
                try {
                    InputStream profileIMG = imgService.getImageInputStream(objectKey);
                    userDataDTO.setProfileIMG(profileIMG);

                }catch (SdkClientException e){
                    //aws sdk Error
                    log.error("getDataForUser : " + e);
                    return null;
                }catch (Exception e){
                    log.error("getDataForUser : " + e);
                    return null;
                }

            }
            return userDataDTO;



        }else{

        }

        return null;
    }


}

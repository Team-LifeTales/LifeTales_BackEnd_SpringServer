package com.LifeTales.domain.family.service;

import com.LifeTales.common.User.FamilyNicknameChecker;
import com.LifeTales.common.User.UserIdChecker;
import com.LifeTales.domain.family.repository.DTO.FamilyDataDTO;
import com.LifeTales.domain.family.repository.DTO.FamilySignUpDTO;
import com.LifeTales.domain.family.repository.FamilyRepository;
import com.LifeTales.domain.family.repository.FamilyRepository;
import com.LifeTales.domain.family.domain.Family;
import com.LifeTales.domain.user.domain.User;
import com.LifeTales.domain.user.domain.UserRole;
import com.LifeTales.domain.user.repository.DTO.UserDataDTO;
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

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class FamilyService {
    @Autowired
    private final FamilyRepository familyRepository;
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final FamilyNicknameChecker familySeqChecker;
    private final RequestIMGService imgService;
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public String family_signUp_service(FamilySignUpDTO familySignUpdata){
        try {
            String fileName = "";
            log.info("family_signUp_service Start >> id {}",familySignUpdata.getIntroduce() );
            if(familySignUpdata.getProfileIMG() != null){
                //프로파일 이미지가 있는경우
                try{
                    fileName = "FamilyProfile-"+familySignUpdata.getNickName();
                    byte[] imageData = familySignUpdata.getProfileIMG();
                    String mimeType = "image/jpeg";

                    ObjectMetadata metadata = new ObjectMetadata();
                    metadata.setContentLength(imageData.length);
                    metadata.setContentType(mimeType);

                    ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);

                    amazonS3Client.putObject(bucket, fileName, inputStream, metadata);

                }catch (SdkClientException e) {
                    log.info("fail - s3 upload fail" + e);
                    return "fail - s3 upload fail";

                }
            }else{
                fileName = "profileImage"; // 없는 경우에는 그냥 fileName을 null이라고 해놓음 일단 -> 기본 이미지로 변경하면 될듯
            }

            User user = userRepository.findById(familySignUpdata.getUserId());
            Family family = Family.builder().nickName(familySignUpdata.getNickName())
                    .profileIMG(fileName)
                    .introduce(familySignUpdata.getIntroduce())
                    .userSeq(user)
                    .build();
            familyRepository.save(family);
            user.setFamilySeq(family);
            user.setRole(UserRole.FAMILY_LEADER);
            entityManager.merge(user);
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

    public FamilyDataDTO getDataForFamily(String nickname) throws IOException {
        /**
         * to do list
         * 아이디체크 (존재여부)
         * 존재하는 경우 - 데이터 저장 .. 가족 프로필 이미지 , 가족 닉네임 , 가족 소개 , 대표 유저 코드
         * 가족 프로필이미지 - 존재하는지 - 있으면 s3에서 받아와야함
         */

        log.info("getDataForFamily Start >> {}",nickname);
        boolean nickNameCheck = familySeqChecker.doesNickNameExist(nickname);
        log.info("getDataForFamily checkNickname result >> {}" , nickNameCheck);
        if(nickNameCheck){
            log.info("getDataForFamily checkNickName Success >> {}" , nickname);
            FamilyDataDTO familyDataDTO = new FamilyDataDTO();
            Family familyData = familyRepository.findByNickName(nickname);
            //데이터 셋업
            familyDataDTO.setUserId(familyData.getUserSeq().getId());
            familyDataDTO.setIntroduce(familyData.getIntroduce());
            familyDataDTO.setNickName(familyData.getNickName());
            //s3에 요청하기 전 확인하기
            if(familyData.getProfileIMG() != null){
                log.info("getDataForUser S3 connect Start");
                String objectKey = "FamilyProfile-"+nickname;
                try {
                    InputStream profileIMG = imgService.getImageInputStream(objectKey);
                    familyDataDTO.setProfileIMG(profileIMG);

                }catch (SdkClientException e){
                    //aws sdk Error
                    log.error("getDataForUser : " + e);
                    return null;
                }catch (Exception e){
                    log.error("getDataForUser : " + e);
                    return null;
                }

            }
            return familyDataDTO;



        }else{

        }

        return null;
    }

}

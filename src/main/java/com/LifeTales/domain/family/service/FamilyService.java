package com.LifeTales.domain.family.service;

import com.LifeTales.common.User.FamilyNicknameChecker;
import com.LifeTales.domain.family.repository.DAO.FamilyDataDAO;
import com.LifeTales.domain.family.repository.DAO.FamilySignInDataDAO;
import com.LifeTales.domain.family.repository.DTO.FamilySignUpDTO;
import com.LifeTales.domain.family.repository.FamilyRepository;
import com.LifeTales.domain.family.domain.Family;
import com.LifeTales.domain.family.repository.FamilyRepository2;
import com.LifeTales.domain.user.domain.User;
import com.LifeTales.domain.user.domain.UserRole;
import com.LifeTales.domain.user.repository.UserRepository;
import com.LifeTales.global.s3.RequestIMGService;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class FamilyService {
    private static final int PAGE_POST_COUNT = 9;
    private static final String orderCriteria = "isCreated";
    @Autowired
    private final FamilyRepository familyRepository;
    private final FamilyRepository2 familyRepository2;
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
                fileName = family_signUp_service_S3_connect(familySignUpdata);
            }else{
                fileName = "profileImage"; // 없는 경우에는 그냥 fileName을 profileImage라고 해놓음 일단 -> 기본 이미지로 변경하면 될듯
            }

            User user = userRepository.findById(familySignUpdata.getUserId());
            Family family = Family.builder().nickname(familySignUpdata.getNickName())
                    .profileIMG(fileName)
                    .introduce(familySignUpdata.getIntroduce())
                    .userSeq(user)
                    .familySignInQuestion(familySignUpdata.getFamilySignInQuestion())
                    .familySignInAnswer(familySignUpdata.getFamilySignInAnswer())
                    .build();
            log.info("family join success in db");
            familyRepository.save(family);
            user.setFamilySeq(family);
            user.setRole(UserRole.FAMILY_LEADER);
            entityManager.merge(user);
            log.info("user database update");
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public FamilyDataDAO getDataForFamily(String nickname) throws IOException {
        /**
         * to do list
         * 아이디체크 (존재여부)
         * 존재하는 경우 - 데이터 저장 .. 가족 프로필 이미지 , 가족 닉네임 , 가족 소개 , 대표 유저 코드
         * 가족 프로필이미지 - 존재하는지 - 있으면 s3에서 받아와야함
         */

        log.info("getDataForFamily Start >> {}",nickname);
        boolean nickNameCheck = familySeqChecker.doesNicknameExist(nickname);
        log.info("getDataForFamily checkNickname result >> {}" , nickNameCheck);
        if(nickNameCheck){
            log.info("getDataForFamily checkNickName Success >> {}" , nickname);
            FamilyDataDAO familyDataDAO = new FamilyDataDAO();
            Family familyData = familyRepository.findByNickname(nickname);
            //데이터 셋업
            familyDataDAO.setUserId(familyData.getUserSeq().getId());
            familyDataDAO.setIntroduce(familyData.getIntroduce());
            familyDataDAO.setNickname(familyData.getNickname());
            familyDataDAO.setProfileIMGURL(familyData.getProfileIMG());
            log.info("getDataForFamily checkNickName Success >> {}" , familyDataDAO);
            return familyDataDAO;


        }else{
            log.error("getDataForFamily :  not exists Family Data" );
        }

        return null;
    }

    public Page<FamilySignInDataDAO> getFamilyDataForSignIn(String searchNickName, int pageNum , Pageable pageable) throws IOException {
        /**
         * to do list
         * 가족 닉네임을 파라미터로 받음
         * 닉네임을 이용해서 FamilySeq를 받아옴
         * familySeq에 해당하는 feed를 모두 가져옴
         * feed에 해당하는 feedImage를 대표로 맨 앞 하나만 가져옴
         * 가져온 feed들을 화면에 띄움
         * */

        log.info("getFeedDataForFamily Start >> {}",searchNickName);
        Sort sort = Sort.by(
                Sort.Order.desc(orderCriteria)
        );
        pageable = PageRequest.of(pageNum, PAGE_POST_COUNT, sort);
        log.info("snn >> {}",searchNickName);
        boolean f = familyRepository.existsByNickname(searchNickName);
        log.info("family >> {}",f);
        //Family family = familyRepository.findByNickname(searchNickName);
        //log.info("family >> {}",family);
        Page<Family> families = familyRepository2.findByNicknameContaining(searchNickName , pageable);
        log.info("getPage >> {}",families);
        Page<FamilySignInDataDAO> returnPage = getPageFamilyData(families);
        if (returnPage == null){
            log.info("null_returnPage");
            return null;
        }
        return returnPage;


    }

    public String family_signUp_service_S3_connect(FamilySignUpDTO familySignUpdata) throws IOException{
        try{
            String fileName = "FamilyProfile-"+familySignUpdata.getNickName();
            byte[] imageData = familySignUpdata.getProfileIMG();
            String mimeType = "image/jpeg";

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(imageData.length);
            metadata.setContentType(mimeType);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);

            amazonS3Client.putObject(bucket, fileName, inputStream, metadata);

            return fileName;
        }catch (SdkClientException e) {
            log.info("fail - s3 upload fail" + e);
            return "fail - s3 upload fail";

        }

    }

    public Page<FamilySignInDataDAO> getPageFamilyData(Page<Family> families)throws  IOException{
        Page<FamilySignInDataDAO> returnPage = families.map(family -> {
            boolean idCheck = userRepository.existsById(family.getUserSeq().getSeq());
            if (idCheck){
                FamilySignInDataDAO familySignInDataDAO = new FamilySignInDataDAO();
                familySignInDataDAO.setUserId(family.getUserSeq().getId());
                familySignInDataDAO.setNickname(family.getNickname());
                familySignInDataDAO.setIntroduce(family.getIntroduce());
                familySignInDataDAO.setProfileIMGURL(family.getProfileIMG());
                familySignInDataDAO.setQuestionForSignIn(family.getFamilySignInQuestion());
                log.info("getfamilyDataDAO");
                return familySignInDataDAO;
            }
            else{
                return null;
            }
        });
        return returnPage;


    }

    public boolean family_answer_check(String nickname, String answer){
        log.info("family_answer_check service start");
        Family family = familyRepository.findByNickname(nickname);
        if(family.getFamilySignInAnswer().equals(answer)){
            log.info("correct answer ");
            return true;
        }else{
            log.info("Incorrect answer");
            return false;
        }


    }
}

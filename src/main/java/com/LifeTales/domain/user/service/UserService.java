package com.LifeTales.domain.user.service;

import com.LifeTales.common.User.UserIdChecker;
import com.LifeTales.domain.family.domain.Family;
import com.LifeTales.domain.family.repository.FamilyRepository;
import com.LifeTales.domain.user.domain.User;
import com.LifeTales.domain.user.domain.UserRole;
import com.LifeTales.domain.user.repository.DTO.UserDataDTO;
import com.LifeTales.domain.user.repository.DTO.UserSignUpDTO;
import com.LifeTales.domain.user.repository.DTO.UserSignUpStep2DTO;
import com.LifeTales.domain.user.repository.DTO.UserSignUpStep3DTO;
import com.LifeTales.domain.user.repository.UserRepository;
import com.LifeTales.global.s3.RequestIMGService;
import com.LifeTales.global.util.JwtUtil;
import com.LifeTales.util.UserUtil;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    //user Service
    @Autowired
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final UserUtil userUtil;
    private  PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final AmazonS3Client amazonS3Client;
    private final UserIdChecker userIdChecker;

    private final RequestIMGService imgService;
    @PersistenceContext
    private EntityManager entityManager;

    //value

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${jwt.Life-tales-secretKey}")
    private String secretKey;

    private Long expiredMs = 1000*60*60l; //1h


    public String login(String id , String password){
        //인증..
        /**
         * do
         * id 존재여부...
         * id -> password 검증..
         */
        if(userRepository.existsById(id)){
            //존재하는경우...
            User user = userRepository.findById(id);
//            log.info("들어온값 id {} , password {}" , id , password);
//            log.info("찾은값 id {} , password {}" , id );
            //검증...
            if(passwordEncoder.matches(password, user.getPwd())){
                log.info("login Service >> {} .. success" ,  id);
                return JwtUtil.createJwtToken(id , secretKey,expiredMs);
            }else{
                return "password is not exist";
            }
        }else{
            //없는경우...
            return "id is not exist";
        }
    }



    public String user_signUp_service(@RequestBody UserSignUpDTO userSignUpdata){
        String encodedPassword = passwordEncoder.encode(userSignUpdata.getPwd());
        if(userRepository.existsByEmail(userSignUpdata.getEmail())){
            return "already_email";
        }else{
            try {
                userRepository.save(
                        User.builder()
                                .id(userSignUpdata.getId())
                                .pwd(encodedPassword)
                                .name(userSignUpdata.getName())
                                .nickName(userSignUpdata.getNickName())
                                .birthDay(userSignUpdata.getBirthDay().atStartOfDay())
                                .phoneNumber(userSignUpdata.getPhoneNumber())
                                .email(userSignUpdata.getEmail())
                                .role(UserRole.TEMP)
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
    public String user_signUp_step2_service(@RequestBody UserSignUpStep2DTO data){
        //프로파일 이미지 & 소개글
        log.info("user_signUp_step2_service Start >> id {}",data.getId() );
        if(data.getProfileIMG() != null){
            //프로파일 이미지가 있는경우
            String return_text = user_img_upload_s3(data.getId() , data.getProfileIMG());
            if(return_text.equals("fail - s3 upload fail") || return_text.equals("Error") ){
                return return_text;
            }else{
                Long userSeq = userUtil.findUserSeqForId(data.getId());
                boolean checkMergeUserProfile = user_signUp_step2_db_service(data.getIntro() , return_text.describeConstable(),userSeq);
                if(checkMergeUserProfile){
                    return "Success";
                }else{
                    return "Error - merge Error";
                }
            }


        }else{
            //없는 경우
            return "fail - s3 upload fail";
        }

    }

    public String user_signUp_step3_service(UserSignUpStep3DTO userData){
        log.info("user_signUp_step3_service >> userId {}" , userData.getId());
        /**
         * to do
         * userId check (exist)
         * family Seq check (exist)
         * userRole Check (current)
         * merge
         * return
         */

        if(userRepository.existsById(userData.getId())){
            // id 존재하는경우
            log.info("user_signUp_step3_service >> idCheck __ success");
            if(familyRepository.existsBySeq(userData.getFamilySeq())){
                //familySeq 가 존재하는경우
                log.info("user_signUp_step3_service >> familySeqCheck __ success");
                Family family = familyRepository.findBySeq(userData.getFamilySeq());
                if (family.getFamilySignInAnswer().equals(userData.getAnswerForSignIn())){
                    log.info("user_signUp_step3_service >> familyAnswerTrue __ success");
                    if(userData.getUserRole().equals("leader") || userData.getUserRole().equals("member")){
                        //userRole Check (current)
                        log.info("user_signUp_step3_service >> roleCheck __ success");
                        //set Seq Number
                        User user  = userRepository.findSeqById(userData.getId());
                        Long userSeq = user.getSeq();

                        //Start db merge
                        user_signup_step3_db_service(userData.getFamilySeq() , userData.getUserRole() ,userSeq);
                        return "Success";
                        //end db merge

                    }else{
                        log.info("user_signUp_step3_service >> roleCheck __ fail");
                        return "roleCheck Fail";
                    }
                }else{
                    log.info("user_signUp_step3_service >> familyAnswerFalse __ fail");
                    return "FamilyAnswer False";
                }

            }else{
                //없음
                log.info("user_signUp_step3_service >> familySeqCheck __ fail");
                return "FamilyCheck Fail";
            }


        }else{
            //존재하지 않음
            log.info("user_signUp_step3_service >> idCheck __ fail");
            return "IdCheck Fail";
        }

    }
    private boolean user_signup_step3_db_service(long familySeq , String userRole , Long userSeq){
        log.info("user_signup_step3_db_service Start >> ");
        try {
            User user = entityManager.find(User.class , userSeq);
            Family family = entityManager.find(Family.class , familySeq);

            if(user !=null){
                //user mergeSetting Start
                user.setFamilySeq(family);
                if(userRole.equals("leader")){
                    user.setRole(UserRole.FAMILY_LEADER);
                }else{
                    user.setRole(UserRole.FAMILY_MEMBER);
                }
                //user mergeSetting End
                //user merge Start
                try {
                    entityManager.merge(user);
                    return true;
                }catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                //user merge End
            }
            else{
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
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

    public String update_user_service(String id , String selectService , String newValue){
        log.info("update_user_service {}", id);
        try {
            Long userSeq = userUtil.findUserSeqForId(id);
            User user = entityManager.find(User.class,userSeq);

            if (user == null) {
                throw new IllegalArgumentException("User not found with ID: " + user.getId());
            }else{
                switch (selectService){
                    case "password":
                        //password Update
                        log.info("update_password_service Start : id {}",id);
                        if(update_password_service(user , newValue)){
                            return "success";
                        }else{
                            return "fail Update Password";
                        }

                    case "nickName":
                        log.info("update_nickName_service {}", id);
                        if(update_nickName_service(user , newValue)){
                            return "success";
                        }else{
                            return "fail Update nickName";
                        }
                    case "intro":
                        log.info("update_intro_service {}", id);
                        if(update_intro_service(user , newValue)){
                            return "success";
                        }else{
                            return "fail intro intro";
                        }
                    case "profile":
                        log.info("update_profile_service {}", id);
                        if(update_profile_service(user , newValue)){
                            return "success";
                        }else{
                            return "fail intro intro";
                        }
                    default:
                        return "Invalid service selection";
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            return "fail find User";
        }


    }

    private boolean update_password_service(User user , String newPassword){
        try{
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPwd(encodedPassword);
            entityManager.merge(user);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean update_nickName_service(User user , String newNickName){
        try{
            user.setNickName(newNickName);
            entityManager.merge(user);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean update_intro_service(User user , String newIntro){
        try{
            user.setIntroduce(newIntro);
            entityManager.merge(user);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean update_profile_service(User user , String newProfile){
        try{
            user.setProfileIMG(newProfile);
            entityManager.merge(user);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String update_profile_S3_service(String id ,byte[] imageData ){
       String imgURL =  userRepository.findById(id).getProfileIMG();
       if(imgURL.isEmpty()){
           String return_text = user_img_upload_s3(id , imageData);
           //이미지 등록이 없는 경우.. 바로 등록..
           if(return_text.equals("fail - s3 upload fail") || return_text.equals("Error") ){
               return return_text;
           }else{
              return update_user_service(id , "profile" , return_text);
           }

       }else{
           //이미지가 이미 있는 경우 삭제후 .. 등록
           log.info("img url check {}" , imgURL); //체크용 삭제할것
           InputStream imageStream = new ByteArrayInputStream(imageData);
           if(user_img_update_s3(imgURL , imageStream)){
                return "success";
           }else{
               return "fail";
           }

       }
    }


    private String user_img_upload_s3(String id, byte[] imageData){
        try{
            String fileName = "UserProfile-"+id;
            String fileUrl= "https://" + bucket + "/" +fileName;
            String mimeType = "image/jpeg";

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(imageData.length);
            metadata.setContentType(mimeType);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);

            amazonS3Client.putObject(bucket, fileName, inputStream, metadata);
            Optional<String> fileUrlOptional = Optional.of(fileUrl);
            String fileURL = String.valueOf(fileUrlOptional);
            return fileURL;

        }catch (SdkClientException e) {
            log.info("fail - s3 upload fail" + e);
            return "fail - s3 upload fail";

        }catch (Exception e) {
            // 기타 예외 처리
            e.printStackTrace();
            return "Error";
        }
    }

    public boolean user_img_update_s3(String imageUrl, InputStream imageStream) {
        try {
            String originalValue = imageUrl;
            String newValue = originalValue.replace("Optional[", "").replace("]", "");
            log.info(newValue); // 확인용, 필요한 경우 주석 처리 가능

            String[] parts = newValue.split("/");

            String bucketName = parts[2];
            String fileName = parts[3];
            String contentType = "image/jpeg"; // JPEG 형식인 경우

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            // S3 버킷에 객체 업로드
            PutObjectRequest request = new PutObjectRequest(bucketName, fileName, imageStream,metadata);
            amazonS3Client.putObject(request);

            return true;
        } catch (Exception e) {
            // 이미지 업로드 중 오류 발생
            e.printStackTrace();
            return false;
        }
    }




}

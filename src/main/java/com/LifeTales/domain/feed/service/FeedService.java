package com.LifeTales.domain.feed.service;


import com.LifeTales.common.User.FamilyNicknameChecker;
import com.LifeTales.common.User.FeedChecker;
import com.LifeTales.common.User.UserIdChecker;
import com.LifeTales.domain.family.domain.Family;
import com.LifeTales.domain.family.repository.FamilyRepository;
import com.LifeTales.domain.feed.domain.Feed;
import com.LifeTales.domain.feed.domain.FeedImageList;
import com.LifeTales.domain.feed.repository.DTO.FeedDataDTO;
import com.LifeTales.domain.feed.repository.DTO.FeedDetailDTO;
import com.LifeTales.domain.feed.repository.DTO.FeedUploadDTO;
import com.LifeTales.domain.feed.repository.FeedImageListRepository;
import com.LifeTales.domain.feed.repository.FeedRepository;
import com.LifeTales.domain.user.domain.User;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.random.*;
import java.io.ByteArrayInputStream;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class FeedService {
    @Autowired
    private final FeedRepository feedRepository;
    private final FeedImageListRepository feedImageListRepository;
    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final FamilyNicknameChecker familySeqChecker;
    private final UserIdChecker userIdChecker;
    private final FeedChecker feedSeqChecker;
    private final RequestIMGService imgService;

    @PersistenceContext
    private EntityManager entityManager;
    public String Feed_upload_service(FeedUploadDTO uploadData){
        try {
            log.info("Feed_upload_service Start >> id {}",uploadData.getContent() );
            User user = entityManager.find(User.class , uploadData.getUserSeq());
            Family family = entityManager.find(Family.class , uploadData.getFamilySeq());
            Feed newFeed = Feed.builder()
                    .familySeq(family)
                    .userSeq(user)
                    .content(uploadData.getContent())
                    .build();

            String fileName = "";
            Feed savedFeed = feedRepository.save(newFeed);
            Long savedFeedId = savedFeed.getSeq(); //저장한 feed의 키 값을 기록
            int countImage = 1;
            try{
                for (byte[] uploadImage : uploadData.getUploadImages() ){
                    Random random = new Random();
                    int coin = random.nextInt(100000);
                    fileName = "Feed-"+uploadData.getUserSeq()+"$"+savedFeedId.toString()+"$"+countImage+"$"+coin;
                    countImage = countImage+1;
                    //String fileUrl= "https://" + bucket + "/lifeTales" +fileName;
                    byte[] imageData = uploadImage;
                    String mimeType = "image/jpeg";


                    ObjectMetadata metadata = new ObjectMetadata();
                    metadata.setContentLength(imageData.length);
                    metadata.setContentType(mimeType);

                    ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);

                    amazonS3Client.putObject(bucket, fileName, inputStream, metadata);

                    feedImageListRepository.save(
                            FeedImageList.builder()
                                    .feedSeq(savedFeed)
                                    .feedImageURL(fileName)
                                    .build()
                    );
                }


            }catch (SdkClientException e) {
                log.info("fail - s3 upload fail" + e);
                return "fail - s3 upload fail";

            }
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

    public List<FeedDataDTO> getFeedDataForFamily(String nickname) throws IOException {
        /**
         * to do list
         * 가족 닉네임을 파라미터로 받음
         * 닉네임을 이용해서 FamilySeq를 받아옴
         * familySeq에 해당하는 feed를 모두 가져옴
         * feed에 해당하는 feedImage를 대표로 맨 앞 하나만 가져옴
         * 가져온 feed들을 화면에 띄움
         * */

        log.info("getFeedDataForFamily Start >> {}",nickname);
        boolean nickNameCheck = familySeqChecker.doesNickNameExist(nickname); // family 존재하는지 확인
        log.info("getFeedDataForFamily checkId result >> {}" , nickNameCheck);
        if(nickNameCheck){
            log.info("getFeedDataForFamily checkId Success >> {}" , nickname);
            Family family = familyRepository.findByNickName(nickname);
            List<Feed> feedDatas = feedRepository.findByFamilySeq(family); //familySeq로 해당하는 feed 전부 찾아서 리스트로 가져옴
            //데이터 셋업
            List<FeedDataDTO> feedDataDTOList = new ArrayList<>(); // feed에 대한 정보들도 리스트로 만듬, 들어가는 정보 (메인사진, userSeq, familySeq, )
            for(Feed feedData: feedDatas){
                boolean idCheck = userRepository.existsById(feedData.getUserSeq().getSeq());
                if (idCheck){
                    FeedDataDTO feedDataDTO = new FeedDataDTO();
                    feedDataDTO.setUserSeq(feedData.getUserSeq().getSeq());
                    feedDataDTO.setFeedSeq(feedData.getSeq());
                    List<FeedImageList> feedImageList = feedImageListRepository.findByFeedSeq(feedData);
                    FeedImageList firstFeedImage = feedImageList.get(0); //메인 사진 하나만 가져옴
                    String firstFeedImageURL = firstFeedImage.getFeedImageURL();
                    if(firstFeedImageURL != null){
                        log.info("getFeedDataForFamily S3 connect Start");
                        String objectKey = firstFeedImageURL;
                        try {
                            InputStream feedIMG = imgService.getImageInputStream(objectKey);
                            feedDataDTO.setFeedIMG(feedIMG);
                            feedDataDTOList.add(feedDataDTO); //이미지까지 입력했을 시에 DTO리스트에 추가

                        }catch (SdkClientException e){
                            //aws sdk Error
                            log.error("getFeedDataForFamily : " + e);
                            return null;
                        }catch (Exception e){
                            log.error("getFeedDataForFamily : " + e);
                            return null;
                        }

                    }
                }
            }
            return feedDataDTOList;



        }else{
            log.info("not exists  family");
            return null;
        }

    }

    public List<FeedDataDTO> getFeedDataForUser(String id) throws IOException {
        /**
         * to do list
         * 가족 닉네임을 파라미터로 받음
         * 닉네임을 이용해서 FamilySeq를 받아옴
         * familySeq에 해당하는 feed를 모두 가져옴
         * feed에 해당하는 feedImage를 대표로 맨 앞 하나만 가져옴
         * 가져온 feed들을 화면에 띄움
         * */

        log.info("getFeedDataForUser Start >> {}",id);
        boolean idCheck = userIdChecker.doesIdExist(id);
        log.info("getFeedDataForUser checkId result >> {}" , idCheck);
        if(idCheck){
            log.info("getFeedDataForUser checkId Success >> {}" , idCheck);
            User user = userRepository.findById(id);
            List<Feed> feedDatas = feedRepository.findByUserSeq(user);
            //데이터 셋업
            List<FeedDataDTO> feedDataDTOList = new ArrayList<>();
            for(Feed feedData: feedDatas){
                FeedDataDTO feedDataDTO = new FeedDataDTO();
                feedDataDTO.setUserSeq(feedData.getUserSeq().getSeq());
                feedDataDTO.setFeedSeq(feedData.getSeq());
                List<FeedImageList> feedImageList = feedImageListRepository.findByFeedSeq(feedData);
                FeedImageList firstFeedImage = feedImageList.get(0);
                String firstFeedImageURL = firstFeedImage.getFeedImageURL();
                if(firstFeedImageURL != null) {
                    log.info("getFeedDataForUser S3 connect Start");
                    String objectKey = firstFeedImageURL;
                    try {
                        InputStream feedIMG = imgService.getImageInputStream(objectKey);
                        feedDataDTO.setFeedIMG(feedIMG);
                        feedDataDTOList.add(feedDataDTO);

                    } catch (SdkClientException e) {
                        //aws sdk Error
                        log.error("getFeedDataForUser : " + e);
                        return null;
                    } catch (Exception e) {
                        log.error("getFeedDataForUser : " + e);
                        return null;
                    }

                }
            }
            return feedDataDTOList;



        }else{
            log.info("not exists user or faily");
            return null;
        }

    }

    public FeedDetailDTO getFeedDetail(Long feedSeq) throws IOException {
        /**
         * to do list
         * feedSeq를 파라미터로 받음
         * feedSeq를 이용해서 content, 작성일자, 작성자를 가져오고 이미지도 list로 가져옴
         * 가져온 feedDetail을 띄움
         * */

        log.info("getFeedDeatil Start >> {}",feedSeq);
        boolean feedSeqCheck = feedSeqChecker.doesSeqExist(feedSeq);
        log.info("getFeedDetail checkSeq result >> {}" , feedSeqCheck);
        if(feedSeqCheck){
            log.info("getFeedDetail checkSeq Success >> {}" , feedSeqCheck);

            Feed feed = feedRepository.findBySeq(feedSeq);
            FeedDetailDTO feedDetailDTO = new FeedDetailDTO();
            Long userSeq = feed.getUserSeq().getSeq();
            Long familySeq = feed.getFamilySeq().getSeq();
            LocalDateTime isCreated = feed.getIsCreated();
            String content = feed.getContent();
            boolean userCheck = userRepository.existsById(userSeq);
            boolean nickNameCheck = familyRepository.existsById(familySeq);
            if (userCheck && nickNameCheck){
                List<FeedImageList> feedImageList = feedImageListRepository.findByFeedSeq(feed);
                List<InputStream> feedImages = new ArrayList<>();
                //데이터 셋업
                for(FeedImageList feedImage : feedImageList){
                    String feedImageURL = feedImage.getFeedImageURL();
                    if(feedImageURL != null){
                        log.info("getFeedDetail S3 connect Start");
                        String objectKey = feedImageURL;
                        try {
                            InputStream feedIMG = imgService.getImageInputStream(objectKey);
                            feedImages.add(feedIMG);

                        }catch (SdkClientException e){
                            //aws sdk Error
                            log.error("getFeedDetail : " + e);
                            return null;
                        }catch (Exception e){
                            log.error("getFeedDetail : " + e);
                            return null;
                        }

                    }
                }
                feedDetailDTO.setUserSeq(userSeq);
                feedDetailDTO.setIsCreated(isCreated);
                feedDetailDTO.setContent(content);
                feedDetailDTO.setFeedIMGs(feedImages);
                feedDetailDTO.setFeedSeq(feedSeq);

                return feedDetailDTO;
            }

            }else{
                log.info("not exists user or family");
                return null;
            }

        return null;
    }

}

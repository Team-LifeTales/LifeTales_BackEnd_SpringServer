package com.LifeTales.domain.like.service;

import com.LifeTales.domain.feed.domain.Feed;
import com.LifeTales.domain.like.domain.LikeEntity;
import com.LifeTales.domain.like.repository.DTO.CheckLikeDTO;
import com.LifeTales.domain.like.repository.LikeRepository;
import com.LifeTales.domain.user.domain.User;
import com.LifeTales.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class LikeService {

    //user Service
    @Autowired
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public String create_like_service(@RequestBody CheckLikeDTO checkLikeDTO){
        try {
            User user = userRepository.findById(checkLikeDTO.getUserId());
            Feed feed = entityManager.find(Feed.class, checkLikeDTO.getFeedSeq());
            log.info(" likeDB create start");
            likeRepository.save(
                    LikeEntity.builder()
                            .userId(user)
                            .feedSeq(feed)
                            .checkLike(checkLikeDTO.isChecked())
                            .build()
            );
            log.info(" likeDB create clear");
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

    public String change_like_service(@RequestBody CheckLikeDTO checkLikeDTO){
        try {
            User user = userRepository.findById(checkLikeDTO.getUserId());
            Feed feed = entityManager.find(Feed.class, checkLikeDTO.getFeedSeq());
            LikeEntity likeEntity = likeRepository.findByUserIdAndFeedSeq(user, feed);
            likeEntity.setCheckLike(!likeEntity.isCheckLike());
            entityManager.merge(likeEntity);
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

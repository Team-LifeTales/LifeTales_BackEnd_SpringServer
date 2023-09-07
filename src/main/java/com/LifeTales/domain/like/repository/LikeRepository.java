package com.LifeTales.domain.like.repository;

import com.LifeTales.domain.feed.domain.Feed;
import com.LifeTales.domain.like.domain.LikeEntity;
import com.LifeTales.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    boolean existsBySeq(Long seq);

    boolean existsByUserIdAndFeedSeq(User user, Feed feed);

    LikeEntity findByUserIdAndFeedSeq(User user, Feed feed);


}

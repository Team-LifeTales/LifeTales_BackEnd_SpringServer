package com.LifeTales.domain.feed.repository;

import com.LifeTales.domain.feed.domain.Feed;
import com.LifeTales.domain.feed.domain.FeedImageList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedImageListRepository extends JpaRepository<FeedImageList, Long> {
    boolean existsBySeq(Long seq);

    List<FeedImageList> findByFeedSeq(Feed feedSeq);

    FeedImageList findFirstByFeedSeq(Feed feedSeq);

}

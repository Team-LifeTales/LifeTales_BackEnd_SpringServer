package com.LifeTales.domain.feed.repository;

import com.LifeTales.domain.feed.domain.FeedImageList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedImageListRepository extends JpaRepository<FeedImageList, Long> {
    boolean existsBySeq(Long seq);

    List<FeedImageList> findByFeedSeq(Long feedSeq);
}

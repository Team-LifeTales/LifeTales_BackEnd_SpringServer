package com.LifeTales.domain.feed.repository;

import com.LifeTales.domain.feed.domain.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    boolean existsBySeq(Long seq);

    List<Feed> findBySeqContaining(Long seq);

    List<Feed> findByFamilySeq( Long familySeq);

    List<Feed> findByUserSeq( Long userSeq);

    Feed findBySeq(Long seq);
}

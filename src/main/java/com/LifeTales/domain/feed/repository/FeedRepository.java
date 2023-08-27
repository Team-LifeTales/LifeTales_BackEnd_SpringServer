package com.LifeTales.domain.feed.repository;

import com.LifeTales.domain.family.domain.Family;
import com.LifeTales.domain.feed.domain.Feed;
import com.LifeTales.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    boolean existsBySeq(Long seq);

    List<Feed> findBySeqContaining(Long seq);

    List<Feed> findByFamilySeq( Family familySeq);

    List<Feed> findByUserSeq( User userSeq);

    Feed findBySeq(Long seq);


}

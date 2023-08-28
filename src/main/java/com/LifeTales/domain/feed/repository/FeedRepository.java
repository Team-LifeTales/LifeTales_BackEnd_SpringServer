package com.LifeTales.domain.feed.repository;

import com.LifeTales.domain.comment.domain.Comment;
import com.LifeTales.domain.comment.domain.CommentRole;
import com.LifeTales.domain.family.domain.Family;
import com.LifeTales.domain.feed.domain.Feed;
import com.LifeTales.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    boolean existsBySeq(Long seq);

    List<Feed> findBySeqContaining(Long seq);

    List<Feed> findByFamilySeq( Family familySeq);

    List<Feed> findByUserSeq( User userSeq);

    Page<Feed> findByFamilySeqAndIsDELETED(Family familySeq , boolean isDeleted, Pageable pageable);

    Page<Feed> findByUserSeqAndIsDELETED(User userSeq , boolean isDeleted, Pageable pageable);
    Feed findBySeq(Long seq);


}

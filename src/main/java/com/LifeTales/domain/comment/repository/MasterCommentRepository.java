package com.LifeTales.domain.comment.repository;

import com.LifeTales.domain.comment.domain.Comment;
import com.LifeTales.domain.feed.domain.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MasterCommentRepository extends JpaRepository<Comment, Long> {
    Comment findBySeq(Long seq);

    List<Comment> findByFeedSeqAndIsDELETED(Feed feed, boolean isDeleted);
}

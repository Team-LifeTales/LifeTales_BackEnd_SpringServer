package com.LifeTales.domain.comment.repository;

import com.LifeTales.domain.comment.domain.Comment;
import com.LifeTales.domain.comment.domain.CommentRole;
import com.LifeTales.domain.feed.domain.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MasterCommentRepository extends JpaRepository<Comment, Long> {
    Comment findBySeq(Long seq);

    Page<Comment> findByFeedSeqAndIsDELETEDAndRole(Feed feed, boolean isDeleted, Pageable pageable , CommentRole role);
}

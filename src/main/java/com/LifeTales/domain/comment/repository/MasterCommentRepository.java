package com.LifeTales.domain.comment.repository;

import com.LifeTales.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MasterCommentRepository extends JpaRepository<Comment, Long> {
    Comment findBySeq(Long seq);
}

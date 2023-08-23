package com.LifeTales.domain.comment.repository;

import com.LifeTales.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlaveCommentRepository extends JpaRepository<Comment, Long> {
}

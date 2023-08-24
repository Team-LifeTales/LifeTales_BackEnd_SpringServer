package com.LifeTales.domain.comment.repository;

import com.LifeTales.domain.comment.domain.Comment;
import com.LifeTales.domain.comment.domain.CommentRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlaveCommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByMasterCommentAndIsDELETEDAndRole(Comment masterCommentSeq , boolean isDeleted, Pageable pageable , CommentRole commentRole);
}

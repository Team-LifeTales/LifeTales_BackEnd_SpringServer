package com.LifeTales.domain.comment.repository;

import com.LifeTales.domain.comment.domain.Comment;
import com.LifeTales.domain.family.domain.Family;
import com.LifeTales.domain.family.repository.DTO.FamilySearchDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}

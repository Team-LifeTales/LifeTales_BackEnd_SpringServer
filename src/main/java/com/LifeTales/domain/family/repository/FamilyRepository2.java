package com.LifeTales.domain.family.repository;

import com.LifeTales.domain.family.domain.Family;
import com.LifeTales.domain.family.repository.DTO.FamilySearchDTO;
import com.LifeTales.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FamilyRepository2 extends JpaRepository<Family, Long> {
    Page<Family> findByNicknameContaining(String nickName, Pageable pageable);



}

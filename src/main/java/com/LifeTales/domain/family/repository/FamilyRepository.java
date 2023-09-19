package com.LifeTales.domain.family.repository;

import com.LifeTales.domain.family.domain.Family;

import com.LifeTales.domain.family.repository.DTO.FamilySearchDTO;
import com.LifeTales.domain.feed.domain.Feed;
import com.LifeTales.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FamilyRepository extends JpaRepository<Family, Long> {
    boolean existsByNickname(String nickname);
    boolean existsByUserSeq(Long userSeq);

    boolean existsBySeq(Long Seq);
    @Query(nativeQuery = true, name = "Family.findFamilyByNickNameContaining")
    List<FamilySearchDTO> findFamilyByNicknameContaining(@Param("nickName") String nickName);
    Family findBySeq(Long familySeq);

    Family findByNickname(String nickName);

    Family findByUserSeq(User user);
    List<Family> findByNicknameContaining(String nickName);



}

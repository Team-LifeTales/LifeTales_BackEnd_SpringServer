package com.LifeTales.domain.family.repository;

import com.LifeTales.domain.family.domain.Family;

import com.LifeTales.domain.family.repository.DTO.FamilyDataDTO;
import com.LifeTales.domain.family.repository.DTO.FamilySearchDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FamilyRepository extends JpaRepository<Family, Long> {
    boolean existsByNickName(String nickname);
    boolean existsByUserSeq(Long userSeq);

    boolean existsBySeq(Long Seq);
    @Query(nativeQuery = true, name = "Family.findFamilyByNickNameContaining")
    List<FamilySearchDTO> findFamilyByNickNameContaining(@Param("nickName") String nickName);
    Family findBySeq(Long familySeq);

    Family findByNickName(String nickName);
    List<Family> findByNickNameContaining(String nickName);
}

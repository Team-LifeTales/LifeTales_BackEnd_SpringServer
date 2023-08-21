package com.LifeTales.common.User;

import com.LifeTales.domain.family.repository.FamilyRepository;
import com.LifeTales.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class FamilyNicknameChecker {
    private final FamilyRepository familyRepository;

    public FamilyNicknameChecker(FamilyRepository familyRepository) {
        this.familyRepository = familyRepository;
    }

    public boolean doesNickNameExist(String nickname) {
        return familyRepository.existsByNickName(nickname);
    }

    public boolean doesSeqExist(Long seq) {
        return familyRepository.existsBySeq(seq);
    }


    public boolean doesUserSeqExist(Long userSeq) {
        return familyRepository.existsByUserSeq(userSeq);
    }
}

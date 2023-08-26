package com.LifeTales.common.User;

import com.LifeTales.domain.family.repository.FamilyRepository;
import com.LifeTales.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class FamilyNicknameChecker {
    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;
    public FamilyNicknameChecker(FamilyRepository familyRepository, UserRepository userRepository) {

        this.familyRepository = familyRepository;
        this.userRepository = userRepository;
    }

    public boolean doesNickNameExist(String nickname) {
        return familyRepository.existsByNickName(nickname);
    }

    public boolean doesSeqExist(Long seq) {
        return familyRepository.existsBySeq(seq);
    }


    public boolean doesUserSeqExist(Long userSeq) {
        return userRepository.existsById(userSeq);
    }
}

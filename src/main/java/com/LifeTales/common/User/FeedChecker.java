package com.LifeTales.common.User;

import com.LifeTales.domain.family.repository.FamilyRepository;
import com.LifeTales.domain.feed.repository.FeedRepository;
import com.LifeTales.domain.user.domain.User;
import com.LifeTales.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class FeedChecker {
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    private final FamilyRepository familyRepository;
    public FeedChecker(FeedRepository feedRepository, UserRepository userRepository, FamilyRepository familyRepository) {

        this.feedRepository = feedRepository;
        this.userRepository = userRepository;
        this.familyRepository = familyRepository;
    }


    public boolean doesSeqExist(Long seq) {
        return feedRepository.existsBySeq(seq);
    }

    public boolean doesUserSeqExist(Long seq) {
        return userRepository.existsById(seq);
    }

    public boolean doesUserIdExist(String id) {
        return userRepository.existsById(id);
    }

    public boolean doesFamilySeqExist(Long seq) {
        return familyRepository.existsById(seq);
    }

    public boolean doesFamilyNicknameExist(String nickname) {
        return familyRepository.existsByNickName(nickname);
    }


}

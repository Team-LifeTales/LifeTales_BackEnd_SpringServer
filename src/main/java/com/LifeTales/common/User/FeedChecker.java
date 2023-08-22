package com.LifeTales.common.User;

import com.LifeTales.domain.family.repository.FamilyRepository;
import com.LifeTales.domain.feed.repository.FeedRepository;
import org.springframework.stereotype.Component;

@Component
public class FeedChecker {
    private final FeedRepository feedRepository;

    public FeedChecker(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }


    public boolean doesSeqExist(Long seq) {
        return feedRepository.existsBySeq(seq);
    }

}

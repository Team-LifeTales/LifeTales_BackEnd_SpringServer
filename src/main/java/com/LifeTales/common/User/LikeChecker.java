package com.LifeTales.common.User;

import com.LifeTales.domain.family.repository.FamilyRepository;
import com.LifeTales.domain.feed.domain.Feed;
import com.LifeTales.domain.feed.repository.FeedRepository;
import com.LifeTales.domain.like.repository.LikeRepository;
import com.LifeTales.domain.user.domain.User;
import com.LifeTales.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class LikeChecker {
    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;

    private final FeedRepository feedRepository;

    private final LikeRepository likeRepository;

    public LikeChecker(FamilyRepository familyRepository, UserRepository userRepository, FeedRepository feedRepository, LikeRepository likeRepository) {
        this.familyRepository = familyRepository;
        this.userRepository = userRepository;
        this.feedRepository = feedRepository;
        this.likeRepository = likeRepository;
    }

    public boolean doesUserIdExist(String id) {return userRepository.existsById(id);}
    public boolean doesFeedSeqExist(Long feedSeq) {return feedRepository.existsBySeq(feedSeq);}

    public boolean doesLikeSeqExist(User user, Feed feed){return likeRepository.existsByUserIdAndFeedSeq(user, feed);}



}

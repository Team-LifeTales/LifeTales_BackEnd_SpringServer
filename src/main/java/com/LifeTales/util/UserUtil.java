package com.LifeTales.util;

import com.LifeTales.common.User.UserIdChecker;
import com.LifeTales.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {
    // 자주 사용할법한 기능들을 따로 정리해서 바로 쓸 수 있게 만들어둔 클래스..
    /**
     * userId -> userSeq Finder
     */
    private final UserRepository userRepository;
    private final UserIdChecker userIdChecker;
    public UserUtil(UserRepository userRepository, UserIdChecker userIdChecker) {
        this.userRepository = userRepository;
        this.userIdChecker = userIdChecker;
    }

    public Long findUserSeqForId(String userId) {
        if (userIdChecker.doesIdExist(userId)) {
            return userRepository.findById(userId).getSeq();
        } else {
            return null;
        }
    }

}

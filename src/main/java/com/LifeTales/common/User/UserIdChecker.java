package com.LifeTales.common.User;

import com.LifeTales.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserIdChecker {
    private final UserRepository userRepository;

    public UserIdChecker(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean doesIdExist(String id) {return userRepository.existsById(id);
    }
}

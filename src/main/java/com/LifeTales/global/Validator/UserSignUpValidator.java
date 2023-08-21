package com.LifeTales.global.Validator;

import com.LifeTales.domain.user.repository.DTO.UserSignUpDTO;

public interface UserSignUpValidator {
    String userSignUpValidate(UserSignUpDTO userSignUpDTO);
}

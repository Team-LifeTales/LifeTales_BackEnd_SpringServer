package com.LifeTales.global.Validator;

import com.LifeTales.domain.family.repository.DTO.FamilySignUpDTO;
import com.LifeTales.domain.user.repository.DTO.UserSignUpDTO;

public interface FamilySignUpValidator {
    String familySignUpValidate(FamilySignUpDTO familySignUpDTO);
}

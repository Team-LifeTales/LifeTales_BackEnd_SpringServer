package com.LifeTales.domain.user.repository.DTO;

import com.LifeTales.domain.family.domain.Family;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignUpStep3DTO {
    private String id;
    private String userRole;
    private Long familySeq;
    private String questionForSignIn;
    private String answerForSignIn;
}

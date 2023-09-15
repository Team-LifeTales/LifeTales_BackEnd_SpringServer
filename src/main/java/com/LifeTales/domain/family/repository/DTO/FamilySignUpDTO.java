package com.LifeTales.domain.family.repository.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FamilySignUpDTO {

    private String nickName;
    private String introduce;
    private byte [] profileIMG;
    private String userId;
    private String familySignInQuestion;
    private String familySignInAnswer;

}

package com.LifeTales.domain.user.repository.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserSignUpDTO {

    private String id;
    private String nickName;
    private String pwd;
    private String name ; //Real Name
    private LocalDate birthDay;

    private String phoneNumber;

    private String email;

}

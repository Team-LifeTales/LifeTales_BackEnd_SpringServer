package com.LifeTales.domain.user.repository.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserSignUpStep2DTO {
    //step 2 .. profile & introduce
    private String id;
    private String intro;

    private byte [] profileIMG;

}

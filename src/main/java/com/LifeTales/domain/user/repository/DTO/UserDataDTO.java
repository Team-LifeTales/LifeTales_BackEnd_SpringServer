package com.LifeTales.domain.user.repository.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

@Getter
@Setter
public class UserDataDTO {
    private Long seq;
    private String id;
    private String nickName;

    private InputStream profileIMG;
}

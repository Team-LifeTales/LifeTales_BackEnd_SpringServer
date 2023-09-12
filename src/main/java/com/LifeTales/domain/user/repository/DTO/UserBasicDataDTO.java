package com.LifeTales.domain.user.repository.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserBasicDataDTO {
    private Long seq;
    private Long familySeq;
    private String id;
    private String NickName;
    private String profile;
}

package com.LifeTales.domain.user.repository.DAO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserBasicDataDAO {
    private Long seq;
    private Long familySeq;
    private String id;
    private String nickName;
    private String profile;
}

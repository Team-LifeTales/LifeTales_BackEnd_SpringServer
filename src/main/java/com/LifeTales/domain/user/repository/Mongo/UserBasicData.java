package com.LifeTales.domain.user.repository.Mongo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "basic_user")
public class UserBasicData {
    private Long seq;
    private Long familySeq;
    private String id;
    private String NickName;
    private String profile;
}

package com.LifeTales.domain.user.repository.DAO.admin;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DeletedUserDAO {
    private Long seq;
    private String id;
    private String name;
    private String nickName;
    private String email;
    private String phoneNumber;
}

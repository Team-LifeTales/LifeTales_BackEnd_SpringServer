package com.LifeTales.domain.family.repository.DAO;

import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

@Getter @Setter
public class FamilyDataDAO {

    private String nickName;
    private String introduce;
    private InputStream profileIMG;
    private String userId;
}

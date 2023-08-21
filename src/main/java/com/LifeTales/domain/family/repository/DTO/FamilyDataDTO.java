package com.LifeTales.domain.family.repository.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.util.InputMismatchException;

@Getter @Setter
public class FamilyDataDTO {

    private String nickName;
    private String introduce;
    private InputStream profileIMG;
    private Long userSeq;
}

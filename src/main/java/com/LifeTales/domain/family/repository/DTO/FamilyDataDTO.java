package com.LifeTales.domain.family.repository.DTO;

import com.LifeTales.domain.feed.repository.DTO.FeedDataDTO;
import com.LifeTales.domain.feed.repository.DTO.FeedDetailDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.List;

@Getter @Setter
public class FamilyDataDTO {

    private String nickName;
    private String introduce;
    private InputStream profileIMG;
    private String userId;
}

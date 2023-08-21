package com.LifeTales.domain.feed.repository.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FeedUploadDTO {

    private Long familySeq;
    private Long userSeq;
    private List<byte[]> uploadImages;
    private String content;

}

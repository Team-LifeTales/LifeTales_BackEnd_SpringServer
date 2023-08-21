package com.LifeTales.domain.feed.repository.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

@Getter
@Setter
public class FeedDataDTO {

    private Long feedSeq;
    private Long userSeq;
    private InputStream feedIMG;

}

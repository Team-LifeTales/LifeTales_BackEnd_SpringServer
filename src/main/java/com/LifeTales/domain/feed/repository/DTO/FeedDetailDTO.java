package com.LifeTales.domain.feed.repository.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class FeedDetailDTO {

    private Long feedSeq;
    private Long userSeq;
    private List<InputStream> feedIMGs;
    private String content;
    private LocalDateTime isCreated;
    private List<FeedCommentDTO> feedComments;

}

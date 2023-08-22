package com.LifeTales.domain.feed.repository.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FeedCommentDTO {

    private Long userSeq;
    private String commentContent;
    private LocalDateTime isCreated;

}

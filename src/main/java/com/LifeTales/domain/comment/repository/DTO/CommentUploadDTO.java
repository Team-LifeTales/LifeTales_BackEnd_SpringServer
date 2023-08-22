package com.LifeTales.domain.comment.repository.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CommentUploadDTO {

    private String content;
    private Long feedSeq;
    private Long userSeq;

}

package com.LifeTales.domain.comment.repository.DTO;

import com.LifeTales.domain.comment.domain.CommentRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CommentUploadDTO {

    private String content;
    private Long feedSeq;
    private String userId;
    private CommentRole role;
    private Long masterCommentSeq;  // Only for slave comments

}

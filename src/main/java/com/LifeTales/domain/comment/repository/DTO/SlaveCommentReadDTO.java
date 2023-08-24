package com.LifeTales.domain.comment.repository.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class SlaveCommentReadDTO {

    private String userNickName;
    private String userProfile;
    private String CommentContent;
    private LocalDateTime isUpdated;
}

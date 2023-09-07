package com.LifeTales.domain.like.repository.DTO;

import com.LifeTales.domain.feed.domain.Feed;
import com.LifeTales.domain.user.domain.User;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CheckLikeDTO {
    private String userId;
    private Long feedSeq;
    private boolean isChecked;
}

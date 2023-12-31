package com.LifeTales.domain.feed.repository.DAO;

import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class FeedDetailDAO {

    private Long feedSeq;
    private Long userSeq;
    private List<String> feedIMGs;
    private String content;
    private LocalDateTime isCreated;

}

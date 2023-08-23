package com.LifeTales.domain.comment.domain;

import com.LifeTales.domain.feed.domain.Feed;
import com.LifeTales.domain.user.domain.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "Comment",
        schema = "LifeTales_Spring_Server"
)
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SEQ")
    private Long seq;

    @Column(name = "CONTENT" , nullable = false , length = 100)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name="ROLE" , nullable = false)
    private CommentRole role;
    
    //slave Comment 만 사용
    @ManyToOne
    @JoinColumn(name = "MASTER_COMMENT_SEQ")
    private Comment masterComment; 
    //master Comment 만 사용
    @Column(name = "EXIST_SLAVE" , nullable = true)
    private Long existSalve;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "FEED_SEQ")
    private Feed feedSeq;

    @Column(name = "IS_CREATED" , nullable = false)
    @CreationTimestamp
    private LocalDateTime isCreated;
    @Column(name = "IS_UPDATED" , nullable = false)
    @UpdateTimestamp
    private LocalDateTime isUpdated;
    @Column(name = "IS_DELETED" , nullable = false)
    private boolean isDELETED;

}

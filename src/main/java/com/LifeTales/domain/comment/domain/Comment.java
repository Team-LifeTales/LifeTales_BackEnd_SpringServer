package com.LifeTales.domain.comment.domain;

import com.LifeTales.domain.family.repository.DTO.FamilySearchDTO;
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

    @ManyToOne
    @JoinColumn(name = "master_comment_seq")
    private Comment masterComment;

    @Enumerated(EnumType.STRING)
    @Column(name="ROLE" , nullable = false)
    private CommentRole role;

    @Column(name = "USERSEQ" , nullable = false)
    private Long userSeq;

    @Column(name = "FeedSEQ" , nullable = false)
    private Long feedSeq;

    @Column(name = "IS_CREATED" , nullable = false)
    @CreationTimestamp
    private LocalDateTime isCreated;
    @Column(name = "IS_UPDATED" , nullable = false)
    @UpdateTimestamp
    private LocalDateTime isUpdated;
    @Column(name = "IS_DELETED" , nullable = false)
    private boolean isDELETED;

}

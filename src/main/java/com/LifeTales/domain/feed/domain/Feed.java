package com.LifeTales.domain.feed.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "Feed",
        schema = "LifeTales_Spring_Server"
)
@Entity
@Data
public class Feed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SEQ")
    private Long seq;
    @Column(name="FAMILYSEQ")
    private Long familySeq;
    @Column(name="USERSEQ")
    private Long userSeq;
    @Column(name="FEEDIMAGELISTSEQ")
    private Long feedImageListSeq;

    @Column(name = "CONTENT" , nullable = true , length = 200)
    private String content;

    @Column(name = "IS_CREATED" , nullable = false)
    @CreationTimestamp
    private LocalDateTime isCreated;
    @Column(name = "IS_UPDATED" , nullable = false)
    @UpdateTimestamp
    private LocalDateTime isUpdated;
    @Column(name = "IS_DELETED" , nullable = false)
    private boolean isDELETED;


}

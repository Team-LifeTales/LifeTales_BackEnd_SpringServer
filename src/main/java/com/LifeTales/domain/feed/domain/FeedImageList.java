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
        name = "FeedImageList",
        schema = "LifeTales_Spring_Server"
)
@Entity
@Data
public class FeedImageList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SEQ", nullable = false)
    private Long seq;
    @Column(name="FEEDSEQ", nullable = false)
    private Long feedSeq;

    @Column(name = "FEEDIMAGEURL" , nullable = false , length = 100)
    private String feedImageURL;

    @Column(name = "IS_CREATED" , nullable = false)
    @CreationTimestamp
    private LocalDateTime isCreated;
    @Column(name = "IS_UPDATED" , nullable = false)
    @UpdateTimestamp
    private LocalDateTime isUpdated;
    @Column(name = "IS_DELETED" , nullable = false)
    private boolean isDELETED;

}

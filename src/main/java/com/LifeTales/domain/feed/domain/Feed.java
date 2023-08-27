package com.LifeTales.domain.feed.domain;

import com.LifeTales.domain.family.domain.Family;
import com.LifeTales.domain.user.domain.User;
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

    @ManyToOne
    @JoinColumn(name = "FAMILY_SEQ")
    private Family familySeq;

    @ManyToOne
    @JoinColumn(name = "USER_SEQ")
    private User userSeq;


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

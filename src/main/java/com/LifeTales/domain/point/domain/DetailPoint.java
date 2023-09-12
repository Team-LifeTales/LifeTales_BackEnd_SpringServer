package com.LifeTales.domain.point.domain;

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
        name = "DetailPoint",
        schema = "LifeTales_Spring_Server"
)
@Data
public class DetailPoint {
    /**
     * 사용자 포인트 정보를 저장하고 관리하는 클래스 - 엔티티
     * detailPoint - 값..
     * log - 이유
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SEQ")
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "POINT")
    private Point point;

    @Column(name="DETAIL_POINT")
    private int detailPoint; //detail Point score

    @Column(name="POINT_LOG")
    private String pointLog;

    @Column(name = "IS_CREATED" , nullable = false)
    @CreationTimestamp
    private LocalDateTime isCreated;
    @Column(name = "IS_UPDATED" , nullable = false)
    @UpdateTimestamp
    private LocalDateTime isUpdated;
    @Column(name = "IS_DELETED" , nullable = false)
    private boolean isDELETED;
}

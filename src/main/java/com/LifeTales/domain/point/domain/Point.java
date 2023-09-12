package com.LifeTales.domain.point.domain;

import com.LifeTales.domain.user.domain.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "Point",
        schema = "LifeTales_Spring_Server"
)
@Data
public class Point {
    /**
     * detail point 의 총합을 저장하는 클래스 - 엔티티
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SEQ")
    private Long seq;
    @OneToOne
    @JoinColumn(name = "USER")
    private User user;
    @Column(name="POINT")
    private int point;


}

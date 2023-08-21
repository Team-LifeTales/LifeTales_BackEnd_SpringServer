package com.LifeTales.domain.test.domain;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "LifeTalestest",
        schema = "LifeTales_Spring_Server"
)
@Data
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SEQ")
    private Long seq;
    @Column(name = "NAME" , nullable = false , length = 30)
    private String name;
    @Column(name = "age" , nullable = false)
    private int age;
    @Column(name = "NICKNAME" , nullable = false , length = 30)
    private String NickName;
    @UpdateTimestamp
    private LocalDateTime isUpdated;
}

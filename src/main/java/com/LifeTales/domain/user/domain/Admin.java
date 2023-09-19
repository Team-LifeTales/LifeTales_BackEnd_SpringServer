package com.LifeTales.domain.user.domain;

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
        name = "Admin",
        schema = "LifeTales_Spring_Server"
)
@Data
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SEQ")
    private Long seq;
    @Column(name = "ID" , nullable = false , length = 30 , unique = true)
    private String id;
    @Column(name = "EMAIL" , nullable = false , length = 30 , unique = true)
    private String email;
    @Column(name = "PWD" , nullable = false , length = 200)
    private String pwd;
    @Enumerated(EnumType.STRING)
    @Column(name="ROLE" , nullable = false)
    private AdminRole role;
    @Column(name = "IS_CREATED" , nullable = false)
    @CreationTimestamp
    private LocalDateTime isCreated;
    @Column(name = "IS_UPDATED" , nullable = false)
    @UpdateTimestamp
    private LocalDateTime isUpdated;
    @Column(name = "IS_DELETED" , nullable = false)
    private boolean isDELETED;
}

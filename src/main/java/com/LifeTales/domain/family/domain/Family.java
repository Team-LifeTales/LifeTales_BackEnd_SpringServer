package com.LifeTales.domain.family.domain;

import com.LifeTales.domain.family.repository.DTO.FamilySearchDTO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NamedNativeQuery(
        name = "Family.findFamilyByNickNameContaining",
        query = "SELECT f.SEQ, f.NICKNAME FROM Family f WHERE f.NICKNAME LIKE :nickName",
        resultSetMapping = "FamilySearchDTOMapping"
)
@SqlResultSetMapping(
        name = "FamilySearchDTOMapping",
        classes = @ConstructorResult(
                targetClass = FamilySearchDTO.class,
                columns = {
                        @ColumnResult(name = "SEQ", type = Long.class),
                        @ColumnResult(name = "NICKNAME", type = String.class)
                }
        )
)

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "Family",
        schema = "LifeTales_Spring_Server",
        uniqueConstraints = @UniqueConstraint(columnNames = "NICKNAME")
)
@Data
public class Family {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SEQ")
    private Long seq;
    @Column(name = "NICKNAME" , nullable = false , length = 10 , unique = true)
    private String nickName;

    @Column(name = "PROFILEIMG" , nullable = true , length = 100)
    private String profileIMG;

    @Column(name = "INTRODUCE" , nullable = true , length = 100)
    private String introduce;

    @Column(name = "USERSEQ" , nullable = false)
    private Long userSeq;

    @Column(name = "IS_CREATED" , nullable = false)
    @CreationTimestamp
    private LocalDateTime isCreated;
    @Column(name = "IS_UPDATED" , nullable = false)
    @UpdateTimestamp
    private LocalDateTime isUpdated;
    @Column(name = "IS_DELETED" , nullable = false)
    private boolean isDELETED;

}

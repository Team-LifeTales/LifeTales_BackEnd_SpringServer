package com.LifeTales.domain.user.domain;

import com.LifeTales.domain.family.domain.Family;
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
        name = "User",
        schema = "LifeTales_Spring_Server",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_id_email", columnNames = {"ID", "email"})
        }
)
@Data
public class User {
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
    @Column(name = "NAME" , nullable = false , length = 10)
    private String name;
    @Column(name = "NICKNAME" , nullable = false , length = 10)
    private String nickName;
    @Column(name = "BIRTHDAY" , nullable = false )
    private LocalDateTime birthDay;

    @Enumerated(EnumType.STRING)
    @Column(name="ROLE" , nullable = false)
    private UserRole role;

    @ManyToOne
    @JoinColumn(name = "FAMILY_SEQ")
    private Family familySeq;

    @Column(name = "PHONENUMBER" , nullable = false , length = 11)
    private String phoneNumber;

    @Column(name = "PROFILEIMG" , nullable = true , length = 100)
    private String profileIMG;

    @Column(name = "INTRODUCE" , nullable = true , length = 100)
    private String introduce;

    @Column(name = "IS_CREATED" , nullable = false)
    @CreationTimestamp
    private LocalDateTime isCreated;
    @Column(name = "IS_UPDATED" , nullable = false)
    @UpdateTimestamp
    private LocalDateTime isUpdated;
    @Column(name = "IS_DELETED" , nullable = false)
    private boolean isDELETED;

    @Getter
    @Setter
    public static class UserUpdate__Password {
        private String pwd;
        private String newPwd;
        public  UserUpdate__Password() {

        }
        public  UserUpdate__Password(String pwd , String newPwd) {
            this.pwd = pwd;
            this.newPwd = newPwd;
        }
    }

    @Getter
    @Setter
    public static class UserUpdate__NickName {
        private String nickName;
        public  UserUpdate__NickName() {

        }
        public  UserUpdate__NickName(String nickName) {
            this.nickName = nickName;
        }
    }

    @Getter
    @Setter
    public static class UserUpdate__Profile {
        private String pwd;
        private String newPwd;
        public  UserUpdate__Profile() {

        }
        public  UserUpdate__Profile(String pwd , String newPwd) {
            this.pwd = pwd;
            this.newPwd = newPwd;
        }
    }

    @Getter
    @Setter
    public static class UserUpdate__Intro {
        private String introduce;
        public  UserUpdate__Intro() {

        }
        public  UserUpdate__Intro(String introduce) {
            this.introduce= introduce;
        }
    }
}

package com.example.puppytalk.User;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Where(clause = "is_deleted = false")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(nullable = true, unique = true, length = 100)
    private String email;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private String profileImageUrl;

    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void setPassword(String newPassword) {
        this.password = password;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateProfileImage(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    @Column(nullable = false)
    private boolean isDeleted;

    public void withdraw() {
        this.isDeleted = true;
    }
}

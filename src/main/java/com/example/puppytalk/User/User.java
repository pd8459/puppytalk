package com.example.puppytalk.User;

import com.example.puppytalk.Shop.BaseTimeEntity;
import com.example.puppytalk.pet.Pet;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@Table(name = "users")
//@Where(clause = "is_deleted = false")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long kakaoId;
    private String googleId;
    private String naverId;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(unique = true, length = 100)
    private String email;

    private String profileImageUrl;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets = new ArrayList<>();

    @Builder
    public User(String username, String password, String nickname, String email, String profileImageUrl, UserRole role, UserStatus status, Long kakaoId, String googleId, String naverId) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        if (role != null) this.role = role;
        if (status != null) this.status = status;
        this.kakaoId = kakaoId;
        this.googleId = googleId;
        this.naverId = naverId;
    }

    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateProfileImage(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void withdraw() {
        this.isDeleted = true;
    }

    public void promoteToAdmin() {
        this.role = UserRole.ADMIN;
    }

    public void updateStatus(UserStatus status) {
        this.status = status;
    }

    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }

    public User googleIdUpdate(String googleId) {
        this.googleId = googleId;
        return this;
    }

    public User naverIdUpdate(String naverId) {
        this.naverId = naverId;
        return this;
    }
}
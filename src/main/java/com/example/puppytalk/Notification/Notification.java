package com.example.puppytalk.Notification;

import com.example.puppytalk.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private boolean isRead = false;

    @CreatedDate
    private LocalDateTime createdAt;

    public Notification(User user, NotificationType notificationType, String message, String url) {
        this.user = user;
        this.notificationType = notificationType;
        this.message = message;
        this.url = url;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
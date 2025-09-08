package com.example.puppytalk.Notification;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotificationResponseDto {
    private Long id;
    private String message;
    private String url;
    private boolean isRead;
    private LocalDateTime createdAt;

    public NotificationResponseDto(Notification notification) {
        this.id = notification.getId();
        this.message = notification.getMessage();
        this.url = notification.getUrl();
        this.isRead = notification.isRead();
        this.createdAt = notification.getCreatedAt();
    }

    public boolean getIsRead() {
        return this.isRead;
    }
}

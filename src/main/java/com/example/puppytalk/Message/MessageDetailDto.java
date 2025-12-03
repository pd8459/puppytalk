package com.example.puppytalk.Message;

import com.example.puppytalk.User.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MessageDetailDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private boolean isRead;

    private String senderUsername;
    private String senderNickname;
    private String senderProfileImageUrl;

    private String receiverUsername;
    private String receiverNickname;
    private String receiverProfileImageUrl;

    public MessageDetailDto(Message message, User currentUser) {
        this.id = message.getId();
        this.content = message.getContent();
        this.createdAt = message.getCreatedAt();
        this.isRead = message.isRead();

        User sender = message.getSender();
        User receiver = message.getReceiver();

        this.senderUsername = sender.getUsername();
        this.senderNickname = (sender.getNickname() != null) ? sender.getNickname() : sender.getUsername();
        this.senderProfileImageUrl = sender.getProfileImageUrl();

        this.receiverUsername = receiver.getUsername();
        this.receiverNickname = (receiver.getNickname() != null) ? receiver.getNickname() : receiver.getUsername();
        this.receiverProfileImageUrl = receiver.getProfileImageUrl();
    }
}
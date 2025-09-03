package com.example.puppytalk.Message;

import com.example.puppytalk.User.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageDetailDto {
    private String senderNickname;
    private String senderUsername;
    private String senderProfileImageUrl;
    private String receiverNickname;
    private String receiverUsername;
    private String content;
    private LocalDateTime createdAt;
    private boolean sentByMe;

    public MessageDetailDto(Message message, User currentUser) {
        User sender = message.getSender();
        User receiver = message.getReceiver();

        this.senderNickname = sender.getNickname();
        this.senderUsername = sender.getUsername();
        this.senderProfileImageUrl = sender.getProfileImageUrl();

        this.receiverNickname = receiver.getNickname();
        this.receiverUsername = receiver.getUsername();

        this.content = message.getContent();
        this.createdAt = message.getCreatedAt();
        this.sentByMe = sender.getId().equals(currentUser.getId());
    }
}
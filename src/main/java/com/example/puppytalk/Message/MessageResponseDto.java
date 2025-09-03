package com.example.puppytalk.Message;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageResponseDto {
    private Long messageId;
    private String content;
    private String senderNickname;
    private String receiverNickname;
    private LocalDateTime createdAt;

    public MessageResponseDto(Message message) {
        this.messageId = message.getId();
        this.content = message.getContent();
        this.senderNickname = message.getSender().getNickname();
        this.receiverNickname = message.getReceiver().getNickname();
        this.createdAt = message.getCreatedAt();
    }
}

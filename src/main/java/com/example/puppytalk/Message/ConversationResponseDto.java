package com.example.puppytalk.Message;

import com.example.puppytalk.User.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ConversationResponseDto {
    private Long conversationId;
    private String otherParticipantNickname;
    private String otherParticipantProfileImageUrl;
    private String lastMessage;
    private LocalDateTime lastMessageCreatedAt;

    public ConversationResponseDto(Conversation conversation, User currentUser) {
        this.conversationId = conversation.getId();

        User otherParticipant = conversation.getParticipants().stream()
                .filter(participant -> !participant.getId().equals(currentUser.getId()))
                .findFirst()
                .orElse(null);

        if (otherParticipant != null) {
            this.otherParticipantNickname = otherParticipant.getNickname();
            this.otherParticipantProfileImageUrl = otherParticipant.getProfileImageUrl();
        }

        if (conversation.getMessages() != null && !conversation.getMessages().isEmpty()) {
            Message lastMsg = conversation.getMessages().get(conversation.getMessages().size() - 1);
            this.lastMessage = lastMsg.getContent();
            this.lastMessageCreatedAt = lastMsg.getCreatedAt();
        }
    }
}
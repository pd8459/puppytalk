package com.example.puppytalk.Message;

import com.example.puppytalk.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column(nullable = false, length = 1000)
    private String content;

    private boolean isRead = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Message(Conversation conversation, User sender, User receiver, String content) {
        this.conversation = conversation;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}

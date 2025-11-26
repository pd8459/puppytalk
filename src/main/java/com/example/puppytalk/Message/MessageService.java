package com.example.puppytalk.Message;

import com.example.puppytalk.Notification.NotificationService;
import com.example.puppytalk.User.User;
import com.example.puppytalk.User.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Transactional
    public MessageResponseDto sendMessage(User sender, String receiverUsername, MessageSendRequestDto requestDto) {
        User receiver = userRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new IllegalArgumentException("받는 사람을 찾을 수 없습니다."));

        Conversation conversation = conversationRepository.findByParticipants(sender, receiver)
                .or(() -> conversationRepository.findByParticipants(receiver, sender))
                .orElseGet(() -> {
                    Conversation newConversation = new Conversation(Set.of(sender, receiver));
                    return conversationRepository.save(newConversation);
                });

        Message message = new Message(conversation, sender, receiver, requestDto.getContent());
        Message savedMessage = messageRepository.save(message);

        notificationService.sendNewMessageNotification(savedMessage);

        return new MessageResponseDto(savedMessage);
    }

    @Transactional(readOnly = true)
    public List<ConversationResponseDto> getConversations(User user) {
        List<Conversation> conversations = conversationRepository.findByParticipantsContains(user);

        return conversations.stream()
                .map(conversation -> new ConversationResponseDto(conversation, user))
                .sorted((c1, c2) -> {
                    if (c1.getLastMessageCreatedAt() == null) return 1;
                    if (c2.getLastMessageCreatedAt() == null) return -1;
                    return c2.getLastMessageCreatedAt().compareTo(c1.getLastMessageCreatedAt());
                })
                .toList();
    }

    @Transactional
    public List<MessageDetailDto> getMessagesInConversation(Long conversationId, User user) {
        if (user == null) {
            throw new IllegalArgumentException("사용자 정보가 없습니다.");
        }

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("대화방을 찾을 수 없습니다."));

        Set<User> participants = conversation.getParticipants();
        if (participants.isEmpty()) {
        } else {
            participants.forEach(p -> {
            });
        }

        boolean isParticipant = participants.stream()
                .anyMatch(p -> p.getId().equals(user.getId()));

        if (!isParticipant) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        List<Message> messages = messageRepository.findAllByConversationOrderByCreatedAtAsc(conversation);
        messages.forEach(message -> {
            if (!message.getSender().getId().equals(user.getId()) && !message.isRead()) {
                message.markAsRead();
            }
        });

        return messages.stream()
                .map(message -> new MessageDetailDto(message, user))
                .toList();
    }

    @Transactional
    public void deleteConversation(Long conversationId, User user) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("대화방을 찾을 수 없습니다."));

        if (!conversation.getParticipants().contains(user)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        conversationRepository.delete(conversation);
    }
}
package com.example.puppytalk.Message;

import com.example.puppytalk.User.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/{receiverUsername}")
    public ResponseEntity<MessageResponseDto> sendMessage(
            @PathVariable String receiverUsername,
            @RequestBody MessageSendRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        MessageResponseDto responseDto = messageService.sendMessage(
                userDetails.getUser(), receiverUsername, requestDto
        );
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<ConversationResponseDto>> getConversations(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<ConversationResponseDto> conversations = messageService.getConversations(userDetails.getUser());
        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<List<MessageDetailDto>> getMessagesInConversation(
            @PathVariable Long conversationId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<MessageDetailDto> messages = messageService.getMessagesInConversation(conversationId, userDetails.getUser());
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("/{conversationId}")
    public ResponseEntity<String> deleteConversation(
            @PathVariable Long conversationId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            messageService.deleteConversation(conversationId, userDetails.getUser());
            return ResponseEntity.ok("대화가 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
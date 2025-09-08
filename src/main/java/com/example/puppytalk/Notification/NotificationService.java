package com.example.puppytalk.Notification;

import com.example.puppytalk.Comment.Comment;
import com.example.puppytalk.Message.Message;
import com.example.puppytalk.Post.Post;
import com.example.puppytalk.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void sendNewCommentOnPostNotification(Comment comment) {
        Post post = comment.getPost();
        User postAuthor = post.getUser();
        User commenter = comment.getUser();

        if (!postAuthor.getId().equals(commenter.getId())) {
            String message = commenter.getNickname() + "님이 회원님의 게시글에 댓글을 남겼습니다.";
            String url = "/post-detail?id=" + post.getId();

            Notification notification = new Notification(
                    postAuthor, // 알림을 받는 사람
                    NotificationType.NEW_COMMENT_ON_POST,
                    message,
                    url
            );
            notificationRepository.save(notification);
        }
    }

    public void sendNewReplyOnCommentNotification(Comment reply) {
        Comment parentComment = reply.getParent();
        if (parentComment == null) {
            return;
        }

        User parentCommentAuthor = parentComment.getUser();
        User replyAuthor = reply.getUser();

        if (!parentCommentAuthor.getId().equals(replyAuthor.getId())) {
            String message = replyAuthor.getNickname() + "님이 회원님의 댓글에 답글을 남겼습니다.";
            String url = "/post-detail?id=" + reply.getPost().getId();

            Notification notification = new Notification(
                    parentCommentAuthor,
                    NotificationType.NEW_REPLY_ON_COMMENT,
                    message,
                    url
            );
            notificationRepository.save(notification);
        }
    }

    public void sendNewMessageNotification(Message message) {
        User sender = message.getSender();
        User receiver = message.getReceiver();

        if (!sender.getId().equals(receiver.getId())) {
            String notificationMessage = sender.getNickname() + "님으로부터 새로운 메시지가 도착했습니다.";
            String url = "/conversation?id=" + message.getConversation().getId();

            Notification notification = new Notification(
                    receiver,
                    NotificationType.NEW_MESSAGE,
                    notificationMessage,
                    url
            );
            notificationRepository.save(notification);
        }
    }

    @Transactional(readOnly = true)
    public List<NotificationResponseDto> getNotifications(User user) {
        List<Notification> notifications = notificationRepository.findAllByUserOrderByCreatedAtDesc(user);
        return notifications.stream()
                .map(NotificationResponseDto::new)
                .toList();
    }

    public void markAllAsRead(User user) {
        List<Notification> notifications = notificationRepository.findAllByUser(user);
        notifications.forEach(notification -> {
            if (!notification.isRead()) {
                notification.markAsRead();
            }
        });
    }

    @Transactional(readOnly = true)
    public long getUnreadNotificationCount(User user) {
        List<Notification> notifications = notificationRepository.findAllByUser(user);
        return notifications.stream().filter(n -> !n.isRead()).count();
    }

    public void markAsRead(Long notificationId, User user) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("알림을 찾을 수 없습니다."));
        if (!notification.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
        notification.markAsRead();
    }
}

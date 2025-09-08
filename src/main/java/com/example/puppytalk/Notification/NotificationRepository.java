package com.example.puppytalk.Notification;

import com.example.puppytalk.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUserOrderByCreatedAtDesc(User user);

    List<Notification> findAllByUser(User user);
}

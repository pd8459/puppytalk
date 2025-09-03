package com.example.puppytalk.Message;

import com.example.puppytalk.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("SELECT c FROM Conversation c JOIN c.participants p1 JOIN c.participants p2 " +
            "WHERE p1 = :user1 AND p2 = :user2 AND SIZE(c.participants) = 2")
    Optional<Conversation> findByParticipants(@Param("user1") User user1, @Param("user2") User user2);

    List<Conversation> findByParticipantsContains(User user);
}

package com.example.puppytalk.Comment;

import com.example.puppytalk.Post.Post;
import com.example.puppytalk.Post.PostRepository;
import com.example.puppytalk.User.User;
import com.example.puppytalk.User.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CommentDataPrepareTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    @Rollback(false)
    void prepareLargeComments() {
        User user = userRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("유저가 없습니다."));
        Post post = postRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("게시글이 없습니다."));

        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            comments.add(new Comment("API 성능 측정용 실제 데이터 " + i, user, post, null));
        }

        commentRepository.saveAll(comments);
        System.out.println("======= DB에 10,000개 댓글 저장 완료! =======");
    }
}
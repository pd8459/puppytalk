package com.example.puppytalk.Comment;

import com.example.puppytalk.Post.Post;
import com.example.puppytalk.Post.PostCategory;
import com.example.puppytalk.Post.PostRepository;
import com.example.puppytalk.User.User;
import com.example.puppytalk.User.UserRole;
import com.example.puppytalk.User.UserStatus;
import com.example.puppytalk.User.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class CommentPerformanceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager em;

    private Long testPostId;
    private Long testUserId;

    @BeforeEach
    void setUp() {
        String suffix = String.valueOf(System.currentTimeMillis());

        User user = User.builder()
                .username("user_" + suffix)
                .password("password")
                .nickname("nick_" + suffix)
                .email("test_" + suffix + "@test.com")
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .build();

        user = userRepository.save(user);
        testUserId = user.getId();

        Post post = Post.builder()
                .title("테스트 게시글 " + suffix)
                .content("내용")
                .category(PostCategory.FREE_BOARD)
                .user(user)
                .build();

        post = postRepository.save(post);
        testPostId = post.getId();

        for (int i = 0; i < 10000; i++) {
            Comment comment = new Comment("테스트 댓글 " + i, user, post, null);
            em.persist(comment);

            if (i % 1000 == 0) {
                em.flush();
                em.clear();
            }
        }
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("댓글 10,000건 조회 성능 측정")
    void measureCommentPerformance() {
        em.flush();
        em.clear();

        User currentUser = userRepository.findById(testUserId)
                .orElseThrow(() -> new NoSuchElementException("테스트 유저를 찾을 수 없습니다."));

        Pageable pageable = PageRequest.of(0, 10000);

        System.out.println("======= [성능 측정 시작] =======");
        long startTime = System.currentTimeMillis();

        Page<CommentResponseDto> result = commentService.getCommentsByPost(testPostId, currentUser, pageable);

        long endTime = System.currentTimeMillis();
        System.out.println("======= [성능 측정 종료] =======");

        System.out.println("댓글 수: " + result.getContent().size());
        System.out.println("총 소요 시간: " + (endTime - startTime) + "ms");

        assertThat(result.getContent()).hasSize(10000);
    }
}
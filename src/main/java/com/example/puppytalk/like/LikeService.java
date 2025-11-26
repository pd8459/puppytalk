package com.example.puppytalk.like;

import com.example.puppytalk.Comment.Comment;
import com.example.puppytalk.Comment.CommentRepository;
import com.example.puppytalk.Post.Post;
import com.example.puppytalk.Post.PostRepository;
import com.example.puppytalk.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public boolean toggleLike(Long postId, User user){
        Post post = postRepository.findById(postId).orElseThrow(()->
                new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );
        if(postLikeRepository.findByUserAndPost(user, post).isPresent()){
            postLikeRepository.deleteByUserAndPost(user,post);
            return false;
        } else {
            postLikeRepository.save(new PostLike(user, post));
            return true;
        }
    }

    @Transactional
    public boolean toggleCommentLike(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 댓글입니다.")
        );
        if (commentLikeRepository.findByUserAndComment(user, comment).isPresent()) {
            commentLikeRepository.deleteByUserAndComment(user, comment);
            return false;
        } else {
            commentLikeRepository.save(new CommentLike(user, comment));
            return true;
        }
    }
}

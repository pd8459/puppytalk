package com.example.puppytalk.like;

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
    private final PostRepository postRepository;

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
}

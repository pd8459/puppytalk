package com.example.puppytalk.Mypage;

import com.example.puppytalk.Comment.CommentResponseDto;
import com.example.puppytalk.Post.PostResponseDto;
import com.example.puppytalk.User.User;
import com.example.puppytalk.User.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/posts")
    public Page<PostResponseDto> getMyPosts(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        User user = userDetails.getUser();
        return myPageService.getMyPosts(user, pageable);
    }

    @GetMapping("/comments")
    public Page<CommentResponseDto> getMyComments(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        User user = userDetails.getUser();
        return myPageService.getMyComments(user, pageable);
    }
}
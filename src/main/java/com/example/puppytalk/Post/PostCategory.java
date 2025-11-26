package com.example.puppytalk.Post;

public enum PostCategory {
    FREE_BOARD("자유게시판"),
    TRAINING_TIPS("훈련 꿀팁"),
    HEALTH_INFO("건강 정보");

    private final String description;

    PostCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

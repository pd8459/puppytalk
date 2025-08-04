package com.example.puppytalk.Post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Transactional
    void deleteByImageUrl(String imageUrl);
}

package com.example.puppytalk.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Image i WHERE i.imageUrl IN :imageUrls")
    void deleteAllByImageUrlIn(@Param("imageUrls") List<String> imageUrls);

    @Transactional
    void deleteByImageUrl(String imageUrl);
}

package com.example.puppytalk.image;

import lombok.Getter;

@Getter
public class ImageUploadResponseDto {
    private String url;

    public ImageUploadResponseDto(String url) {
        this.url = url;
    }
}

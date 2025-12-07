package com.example.puppytalk.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoogleUserInfoDto {
    private String id;
    private String email;
    private String name;
    private String picture;

    public GoogleUserInfoDto(String id, String email, String name, String picture) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.picture = picture;
    }
}
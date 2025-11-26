package com.example.puppytalk.ai;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatRequestDto {
    private String question;

    public ChatRequestDto(String question) {
        this.question = question;
    }
}

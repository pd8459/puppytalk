package com.example.puppytalk.pet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetRequestDto {
    private String name;
    private String breed;
    private int age;
    private String gender;
    private String introduction;
    private String profileImageUrl;
    private Double weight;
}
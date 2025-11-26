package com.example.puppytalk.pet;

import lombok.Getter;

@Getter
public class PetRequestDto {
    private String name;
    private String breed;
    private int age;
    private String gender;
    private String introduction;
}
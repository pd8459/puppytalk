package com.example.puppytalk.pet;

import lombok.Getter;

@Getter
public class PetResponseDto {
    private Long id;
    private String name;
    private String breed;
    private int age;
    private String gender;
    private String introduction;
    private String profileImageUrl;

    public PetResponseDto(Pet pet) {
        this.id = pet.getId();
        this.name = pet.getName();
        this.breed = pet.getBreed();
        this.age = pet.getAge();
        this.gender = pet.getGender();
        this.introduction = pet.getIntroduction();
        this.profileImageUrl = pet.getProfileImageUrl();
    }
}
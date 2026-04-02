package com.example.puppytalk.pet;

import com.example.puppytalk.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    @Transactional
    public PetResponseDto createPet(PetRequestDto requestDto, User user) {
        Pet pet = new Pet();
        pet.setUser(user);
        pet.setName(requestDto.getName());
        pet.setBreed(requestDto.getBreed());
        pet.setAge(requestDto.getAge());
        pet.setGender(requestDto.getGender());
        pet.setIntroduction(requestDto.getIntroduction());
        pet.setWeight(requestDto.getWeight());

        if (requestDto.getProfileImageUrl() != null) {
            pet.setProfileImageUrl(requestDto.getProfileImageUrl());
        }

        Pet savedPet = petRepository.save(pet);
        return new PetResponseDto(savedPet);
    }

    @Transactional(readOnly = true)
    public List<PetResponseDto> getPets(User user) {
        return petRepository.findAllByUser(user).stream()
                .map(PetResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PetResponseDto updatePet(Long petId, PetRequestDto requestDto, User user) {
        Pet pet = findPetByIdAndUser(petId, user);

        pet.setName(requestDto.getName());
        pet.setBreed(requestDto.getBreed());
        pet.setAge(requestDto.getAge());
        pet.setGender(requestDto.getGender());
        pet.setIntroduction(requestDto.getIntroduction());
        pet.setWeight(requestDto.getWeight());

        if (requestDto.getProfileImageUrl() != null) {
            pet.setProfileImageUrl(requestDto.getProfileImageUrl());
        }

        return new PetResponseDto(pet);
    }

    @Transactional
    public void deletePet(Long petId, User user) {
        Pet pet = findPetByIdAndUser(petId, user);
        petRepository.delete(pet);
    }

    private Pet findPetByIdAndUser(Long petId, User user) {
        Pet pet = petRepository.findByIdWithUser(petId)
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 반려견 정보가 없습니다."));

        if (!pet.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("해당 반려견에 대한 권한이 없습니다.");
        }
        return pet;
    }
}
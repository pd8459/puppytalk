package com.example.puppytalk.pet;

import com.example.puppytalk.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findAllByUser(User user);
}
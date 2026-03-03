package com.example.puppytalk.pet;

import com.example.puppytalk.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    @Query("SELECT p FROM Pet p JOIN FETCH p.user WHERE p.user = :user")
    List<Pet> findAllByUser(@Param("user") User user);

    @Query("SELECT p FROM Pet p JOIN FETCH p.user WHERE p.id = :id")
    Optional<Pet> findByIdWithUser(@Param("id") Long id);
}
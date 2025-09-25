package com.example.puppytalk.playground;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaygroundRepository extends JpaRepository<Playground, Long> {
    List<Playground> findByNameContainingOrAddressContaining(String nameKeyword, String addressKeyword);
}

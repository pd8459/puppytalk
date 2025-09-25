package com.example.puppytalk.hospital;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimalHospitalRepository extends JpaRepository<AnimalHospital, Long> {
    List<AnimalHospital> findByNameContainingOrAddressContaining(String nameKeyword, String addresKeyword);
}

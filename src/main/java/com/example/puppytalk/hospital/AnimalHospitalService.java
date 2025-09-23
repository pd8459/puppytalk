package com.example.puppytalk.hospital;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnimalHospitalService {

    private final AnimalHospitalRepository hospitalRepository;

    @Transactional(readOnly = true)
    public List<AnimalHospitalResponseDto> getAllHospitals() {
        return hospitalRepository.findAll().stream()
                .map(AnimalHospitalResponseDto::new)
                .collect(Collectors.toList());
    }
}
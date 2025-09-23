package com.example.puppytalk.hospital;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AnimalHospitalController {

    private final AnimalHospitalService hospitalService;

    @GetMapping("/api/hospitals")
    public ResponseEntity<List<AnimalHospitalResponseDto>> getAllHospitals() {
        List<AnimalHospitalResponseDto> hospitals = hospitalService.getAllHospitals();
        return ResponseEntity.ok(hospitals);
    }
}
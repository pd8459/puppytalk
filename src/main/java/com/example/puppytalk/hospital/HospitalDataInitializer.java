package com.example.puppytalk.hospital;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class HospitalDataInitializer {

    private final AnimalHospitalRepository hospitalRepository;

    @PostConstruct
    public void loadData() throws CsvValidationException, IOException {
        if (hospitalRepository.count() > 0) {
            return;
        }

        try (CSVReader reader = new CSVReader(new InputStreamReader(
                new ClassPathResource("동물병원_전국.csv").getInputStream(), StandardCharsets.UTF_8))) {

            reader.readNext();

            String[] data;
            while ((data = reader.readNext()) != null) {
                AnimalHospital hospital = new AnimalHospital();
                hospital.setName(data[0]);
                hospital.setAddress(data[1]);
                hospital.setOperatingHours(data[2]);
                hospital.setContact(data[3]);

                try {
                    if (data.length > 5 && !data[5].isEmpty()) {
                        hospital.setLatitude(Double.parseDouble(data[5]));
                    }
                    if (data.length > 6 && !data[6].isEmpty()) {
                        hospital.setLongitude(Double.parseDouble(data[6]));
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid coordinate for: " + data[0]);
                }
                hospitalRepository.save(hospital);
            }
        }
    }
}
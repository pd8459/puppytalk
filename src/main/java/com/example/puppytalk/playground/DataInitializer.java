package com.example.puppytalk.playground;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final PlaygroundRepository playgroundRepository;

    @PostConstruct
    public void loadData() {
        if (playgroundRepository.count() > 0) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new ClassPathResource("playgrounds.csv").getInputStream(), StandardCharsets.UTF_8))) {

            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                Playground playground = new Playground();
                playground.setName(data[0]);
                playground.setAddress(data[1]);
                playground.setOperatingHours(data[3]);
                playground.setContact(data[4]);

                try {
                    if (data.length > 6 && !data[6].isEmpty()) {
                        playground.setLatitude(Double.parseDouble(data[6]));
                    }
                    if (data.length > 7 && !data[7].isEmpty()) {
                        playground.setLongitude(Double.parseDouble(data[7]));
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid coordinate format for: " + data[0]);
                }

                playgroundRepository.save(playground);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
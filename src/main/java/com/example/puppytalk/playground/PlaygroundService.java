package com.example.puppytalk.playground;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaygroundService {

    private final PlaygroundRepository playgroundRepository;

    @Transactional(readOnly = true)
    public List<PlaygroundResponseDto> getAllPlaygrounds() {
        return playgroundRepository.findAll().stream()
                .map(PlaygroundResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PlaygroundResponseDto> searchPlaygrounds(String keyword) {
        return playgroundRepository.findByNameContainingOrAddressContaining(keyword, keyword).stream()
                .map(PlaygroundResponseDto::new)
                .collect(Collectors.toList());
    }
}

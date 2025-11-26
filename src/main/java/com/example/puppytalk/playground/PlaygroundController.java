package com.example.puppytalk.playground;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlaygroundController {

    private final PlaygroundService playgroundService;

    @GetMapping("/api/playgrounds")
    public ResponseEntity<List<PlaygroundResponseDto>> getAllPlaygrounds() {
        List<PlaygroundResponseDto> playgrounds = playgroundService.getAllPlaygrounds();
        return ResponseEntity.ok(playgrounds);
    }

    @GetMapping("/api/playgrounds/search")
    public ResponseEntity<List< PlaygroundResponseDto>> searchPlaygrounds(@RequestParam String keyword) {
        return ResponseEntity.ok(playgroundService.searchPlaygrounds(keyword));
    }
}

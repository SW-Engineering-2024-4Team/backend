package com.example.agricola.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class TestController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/test/send")
    public String sendTestMessage(@RequestParam String destination, @RequestParam String message) {
        simpMessagingTemplate.convertAndSend(destination, Map.of("message", message));
        return "Message sent to " + destination;
    }

    @GetMapping("/test/validPositions")
    public String sendValidPositions(@RequestParam String playerId) {
        // 예시 유효한 위치 리스트
        Set<int[]> validPositions = Set.of(
                new int[]{0, 0},
                new int[]{1, 1},
                new int[]{2, 2}
        );

        simpMessagingTemplate.convertAndSend("/topic/validPositions", Map.of(
                "playerId", playerId,
                "validPositions", validPositions.stream()
                        .map(pos -> Map.of("x", pos[0], "y", pos[1]))
                        .toList()
        ));
        return "Valid positions sent to /topic/validPositions";
    }

    @GetMapping("/test/fenceRequest")
    public String sendFenceRequest(@RequestParam String playerId) {
        simpMessagingTemplate.convertAndSend("/topic/fenceRequest", Map.of(
                "playerId", playerId,
                "actionType", "buildFence"
        ));
        return "Fence request sent to /topic/fenceRequest";
    }
}

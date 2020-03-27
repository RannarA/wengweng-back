package com.rannar.wengweng.rest;

import com.rannar.wengweng.entity.GameState;
import com.rannar.wengweng.service.impl.GameServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game")
public class GameResource {
    private final GameServiceImpl gameService;

    public GameResource(GameServiceImpl gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/start")
    public ResponseEntity<GameState> startGame() {
        return ResponseEntity.ok(gameService.startGame());
    }
}

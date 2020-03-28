package com.rannar.wengweng.rest;

import com.rannar.wengweng.dto.GameStateDTO;
import com.rannar.wengweng.entity.GameState;
import com.rannar.wengweng.service.impl.GameServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameResource {
    private final GameServiceImpl gameService;

    public GameResource(GameServiceImpl gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/start")
    public ResponseEntity<GameStateDTO> startGame() {
        return ResponseEntity.ok(new GameStateDTO(gameService.startGame()));
    }

    @GetMapping
    public ResponseEntity<GameState> getCurrentGame() {
        return ResponseEntity.ok(gameService.getCurrentGame());
    }

    @DeleteMapping
    public ResponseEntity<Object> endGame() {
        this.gameService.endGame();
        return ResponseEntity.accepted().build();
    }
}

package com.rannar.wengweng.socket;

import com.rannar.wengweng.dto.GameStateDTO;
import com.rannar.wengweng.dto.Move;
import com.rannar.wengweng.entity.GameState;
import com.rannar.wengweng.entity.Player;
import com.rannar.wengweng.service.GameService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class GameSocketController {
    private final GameService gameService;

    public GameSocketController(GameService gameService) {
        this.gameService = gameService;
    }

    @MessageMapping("/start")
    @SendTo("/game/start")
    public GameStateDTO start() {
        return new GameStateDTO(this.gameService.startGame());
    }

    @MessageMapping("/state")
    @SendTo("/game/state")
    public GameStateDTO gameState() {
        GameState gameState = this.gameService.getCurrentGame();
        if (gameState == null) {
            return null;
        }
        return new GameStateDTO(this.gameService.getCurrentGame());
    }

    @MessageMapping("/player")
    @SendToUser("/game/player")
    public Player getPlayer(String name) {
        return this.gameService.getPlayer(name);
    }

    @MessageMapping("/move")
    @SendToUser("/game/move")
    public GameStateDTO move(Move move) {
        return new GameStateDTO(gameService.move(move));
    }
}

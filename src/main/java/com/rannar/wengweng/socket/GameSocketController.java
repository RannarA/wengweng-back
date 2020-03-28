package com.rannar.wengweng.socket;

import com.rannar.wengweng.dto.GameStateDTO;
import com.rannar.wengweng.entity.Player;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class GameSocketController {

    @MessageMapping("/move")
    @SendToUser("/game/move")
    public Player move(Player player) {
        return player;
    }

    @MessageMapping("/state")
    @SendTo("/game/state")
    public GameStateDTO gameState() {
        return new GameStateDTO();
    }
}

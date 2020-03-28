package com.rannar.wengweng.service;

import com.rannar.wengweng.dto.Move;
import com.rannar.wengweng.entity.GameState;
import com.rannar.wengweng.entity.Player;

public interface GameService {
    GameState startGame();

    GameState getCurrentGame();

    GameState move(Move move);

    // move to separate service
    Player getPlayer(String name);

    void endGame();

    GameState roundStart();
}

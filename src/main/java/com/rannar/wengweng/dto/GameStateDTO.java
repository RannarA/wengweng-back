package com.rannar.wengweng.dto;

import com.rannar.wengweng.entity.GameState;
import com.rannar.wengweng.entity.Player;
import com.rannar.wengweng.entity.Round;
import com.rannar.wengweng.entity.Row;

import java.util.List;
import java.util.stream.Collectors;

public class GameStateDTO {
    private List<Row> pyramid;
    private Round currentRound;
    private List<String> playerNames;

    public GameStateDTO() {}

    public GameStateDTO(GameState gameState) {
        this.pyramid = gameState.getPyramid();
        this.currentRound = gameState.getCurrentRound();
        this.playerNames = gameState.getPlayers()
                .stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    public List<Row> getPyramid() {
        return pyramid;
    }

    public void setPyramid(List<Row> pyramid) {
        this.pyramid = pyramid;
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(Round currentRound) {
        this.currentRound = currentRound;
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }

    public void setPlayerNames(List<String> playerNames) {
        this.playerNames = playerNames;
    }
}

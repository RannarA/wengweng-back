package com.rannar.wengweng.dto;

import com.rannar.wengweng.entity.GameState;
import com.rannar.wengweng.entity.Round;
import com.rannar.wengweng.entity.Row;

import java.util.List;

public class GameStateDTO {
    private String id;
    private List<Row> pyramid;
    private Round currentRound;

    public GameStateDTO() {}

    public GameStateDTO(GameState gameState) {
        this.pyramid = gameState.getPyramid();
    }

    public List<Row> getPyramid() {
        return pyramid;
    }

    public void setPyramid(List<Row> pyramid) {
        this.pyramid = pyramid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(Round currentRound) {
        this.currentRound = currentRound;
    }
}

package com.rannar.wengweng.dto;

import com.rannar.wengweng.entity.GameState;
import com.rannar.wengweng.entity.Row;

import java.util.List;

public class GameStateDTO {
    private String id;
    private List<Row> pyramid;

    public GameStateDTO() {}

    public GameStateDTO(GameState gameState) {
        this.id = gameState.getId();
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
}

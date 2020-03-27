package com.rannar.wengweng.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

public class GameState {
    @Id
    private String id;
    private List<Row> pyramid;
    private List<Player> players;

    public GameState(List<Row> pyramid, List<Player> players) {
        this.pyramid = pyramid;
        this.players = players;
    }

    public List<Row> getPyramid() {
        return pyramid;
    }

    public void setPyramid(List<Row> pyramid) {
        this.pyramid = pyramid;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

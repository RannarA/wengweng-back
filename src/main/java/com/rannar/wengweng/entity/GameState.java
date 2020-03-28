package com.rannar.wengweng.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

public class GameState {
    @Id
    private String id;
    private List<Row> pyramid;
    private List<Player> players;
    private Round currentRound;

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

    public Round getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(Round currentRound) {
        this.currentRound = currentRound;
    }

    public Player getPlayer(String name) {
        return getPlayers()
                .stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public Player removeCard(String name, Card card) {
        Player player = getPlayer(name);
        player.getCards().removeIf(c -> c.getId() == card.getId());
        return player;
    }
}

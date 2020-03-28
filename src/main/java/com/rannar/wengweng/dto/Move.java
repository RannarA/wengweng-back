package com.rannar.wengweng.dto;

import com.rannar.wengweng.entity.Card;
import com.rannar.wengweng.entity.Player;

import java.util.List;

public class Move {
    private Player player;
    private List<Card> cards;
    private List<String> victims;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public List<String> getVictims() {
        return victims;
    }

    public void setVictims(List<String> victims) {
        this.victims = victims;
    }
}

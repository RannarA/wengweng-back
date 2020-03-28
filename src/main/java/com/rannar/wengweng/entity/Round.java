package com.rannar.wengweng.entity;

import java.util.ArrayList;
import java.util.List;

public class Round {
    private Card currentCard;
    private List<String> finishedPlayers;

    public Card getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    public List<String> getFinishedPlayers() {
        if (finishedPlayers == null) {
            finishedPlayers = new ArrayList<>();
        }
        return finishedPlayers;
    }

    public void setFinishedPlayers(List<String> finishedPlayers) {
        this.finishedPlayers = finishedPlayers;
    }
}

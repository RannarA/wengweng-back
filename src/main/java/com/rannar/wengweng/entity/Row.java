package com.rannar.wengweng.entity;

import java.util.List;

public class Row {
    private List<Card> cards;

    public Row() {}

    public Row(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}

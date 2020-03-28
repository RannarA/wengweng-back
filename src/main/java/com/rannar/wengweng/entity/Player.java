package com.rannar.wengweng.entity;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int drinkCount;
    private List<Card> cards;

    public Player() {}

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Card> getCards() {
        if (cards == null) {
            cards = new ArrayList<>();
        }
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public int getDrinkCount() {
        return drinkCount;
    }

    public void setDrinkCount(int drinkCount) {
        this.drinkCount = drinkCount;
    }

    public void addDrinks(int count) {
        drinkCount += count;
    }
}

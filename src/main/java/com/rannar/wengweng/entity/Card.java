package com.rannar.wengweng.entity;

public class Card {
    private int id;
    private String suit;
    private String rank;
    private boolean turned;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public boolean isTurned() {
        return turned;
    }

    public void setTurned(boolean turned) {
        this.turned = turned;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", suit='" + suit + '\'' +
                ", rank='" + rank + '\'' +
                ", turned=" + turned +
                '}';
    }
}

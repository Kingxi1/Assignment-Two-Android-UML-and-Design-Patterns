package com.example.assignment2;

public class Player {

    private String name;
    private int avatarResId;
    private int score;

    public Player(String name, int avatarResId, int score) {
        this.name = name;
        this.avatarResId = avatarResId;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getAvatarResId() {
        return avatarResId;
    }

    public int getScore() {
        return score;
    }
}
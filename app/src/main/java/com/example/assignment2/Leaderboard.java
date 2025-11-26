package com.example.assignment2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Leaderboard {

    private static Leaderboard instance;

    private ArrayList<Player> players = new ArrayList<>();

    private Leaderboard() {}

    public static Leaderboard getInstance() {
        if (instance == null) {
            instance = new Leaderboard();
        }
        return instance;
    }

    public void updateLeaderboard(Player newPlayer) {
        players.add(newPlayer);

        // Sorting: Scores from highest to lowest
        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return p2.getScore() - p1.getScore();
            }
        });

        //Keep it as it is
        if (players.size() > 5) {
            players = new ArrayList<>(players.subList(0, 5));
        }
    }

    public ArrayList<Player> getTopFive() {
        return players; // It has been confirmed that a maximum of 5 people will attend.
    }
}

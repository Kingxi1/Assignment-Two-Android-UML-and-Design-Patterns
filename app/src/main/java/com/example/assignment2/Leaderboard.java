package com.example.assignment2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Leaderboard {

    private static Leaderboard leaderboardInstance;
    private static final int MAX_LEADERBOARD_SIZE = 5;
    private ArrayList<Player>  leaderboard = new ArrayList<>();

    private Leaderboard() {}

    public static Leaderboard getInstance() {
        if (leaderboardInstance  == null) {
            leaderboardInstance  = new Leaderboard();
        }
        return leaderboardInstance ;
    }

    public void updateLeaderboard(Player newPlayer) {
        leaderboard.add(newPlayer);

        // Sorting: Scores from highest to lowest
        Collections.sort( leaderboard, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return p2.getScore() - p1.getScore();
            }
        });

        //Keep it as it is
        if (leaderboard.size() > MAX_LEADERBOARD_SIZE) {
            leaderboard = new ArrayList<>(leaderboard.subList(0, MAX_LEADERBOARD_SIZE));
        }
    }

    public ArrayList<Player>  getLeaderboard() {
        return  leaderboard; // It has been confirmed that a maximum of 5 people will attend.
    }
}

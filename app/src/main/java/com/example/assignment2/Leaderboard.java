package com.example.assignment2;

import java.util.ArrayList;
import java.util.Collections;

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

    public void updateLeaderboard(Player player) {
        players.add(player);

        // 最高分排前面
        Collections.sort(players, (p1, p2) ->
                Integer.compare(p2.getScore(), p1.getScore()));

        // 排行榜最多 5 个
        if (players.size() > 5) {
            players.remove(players.size() - 1);
        }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
}
package com.example.assignment2;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {

    private ImageView[] avatarViews;
    private TextView[] nameViews;
    private TextView[] scoreViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use the ranking layout that we have designed ourselves
        setContentView(R.layout.activity_leaderboard);

        //Bind 5 rows of view controls (the ID should be consistent with activity_leaderboard.xml)
        avatarViews = new ImageView[]{
                findViewById(R.id.iv_leaderboard_avatar1),
                findViewById(R.id.iv_leaderboard_avatar2),
                findViewById(R.id.iv_leaderboard_avatar3),
                findViewById(R.id.iv_leaderboard_avatar4),
                findViewById(R.id.iv_leaderboard_avatar5)
        };

        nameViews = new TextView[]{
                findViewById(R.id.tv_leaderboard_name1),
                findViewById(R.id.tv_leaderboard_name2),
                findViewById(R.id.tv_leaderboard_name3),
                findViewById(R.id.tv_leaderboard_name4),
                findViewById(R.id.tv_leaderboard_name5)
        };

        scoreViews = new TextView[]{
                findViewById(R.id.tv_leaderboard_score1),
                findViewById(R.id.tv_leaderboard_score2),
                findViewById(R.id.tv_leaderboard_score3),
                findViewById(R.id.tv_leaderboard_score4),
                findViewById(R.id.tv_leaderboard_score5)
        };

        //  2. Retrieve the top 5 from the Leaderboard singleton
        ArrayList<Player> topPlayers = Leaderboard.getInstance().getLeaderboard();


        for (int i = 0; i < topPlayers.size() && i < avatarViews.length; i++) {
            Player p = topPlayers.get(i);

            avatarViews[i].setImageResource(p.getAvatarResId());
            nameViews[i].setText(p.getName() + " scored:");
            scoreViews[i].setText(String.valueOf(p.getScore()));
        }
    }
}
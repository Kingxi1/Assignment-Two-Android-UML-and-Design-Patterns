package com.example.assignment2;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class GameActivity extends AppCompatActivity {

    // UML：GameActivity have GameLogic member
    private GameLogic gameLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Bind the timing and score text together
        TextView tvTimer = findViewById(R.id.tv_timer_text);
        TextView tvScore = findViewById(R.id.tv_score_text);

        // 2. Collect the ImageView of 9 holes
        ArrayList<ImageView> moleViews = new ArrayList<>();
        moleViews.add(findViewById(R.id.iv_without_mole_01));
        moleViews.add(findViewById(R.id.iv_without_mole_02));
        moleViews.add(findViewById(R.id.iv_without_mole_03));
        moleViews.add(findViewById(R.id.iv_without_mole_04));
        moleViews.add(findViewById(R.id.iv_without_mole_05));
        moleViews.add(findViewById(R.id.iv_without_mole_06));
        moleViews.add(findViewById(R.id.iv_without_mole_07));
        moleViews.add(findViewById(R.id.iv_without_mole_08));
        moleViews.add(findViewById(R.id.iv_without_mole_09));

        // 3. Create GameLogic
        gameLogic = new GameLogic(
                this,       // Context
                moleViews,  // Nine holes
                tvScore,    //  Score TextView
                tvTimer     //Timer TextView
        );

        // 4. Start Game
        gameLogic.startGame();

        // 5. Add Click event
        for (int i = 0; i < moleViews.size(); i++) {
            final int index = i;
            ImageView holeView = moleViews.get(i);
            holeView.setOnClickListener(v -> gameLogic.onMoleClicked(index));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gameLogic != null) {
            gameLogic.stopMoleLoop();
        }
    }
}
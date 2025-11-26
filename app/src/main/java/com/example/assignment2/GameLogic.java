package com.example.assignment2;

import android.content.Context;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import android.content.Intent;

public class GameLogic {
    private final Context context;
    private final ArrayList<ImageView> moleViews;
    private final ArrayList<Mole> moleList;

    private final TextView tvScore;
    private final TextView tvTimer;

    private int score = 0;
    private int timeLeft = 30;

    private final Handler handler = new Handler();
    private final Random random = new Random();

    private Runnable moleRunnable;
    private Runnable timerRunnable;

    private boolean isGameRunning = false;

    // IMG Resource
    private final int imgWithMole = R.drawable.img_with_mole;
    private final int imgWithoutMole = R.drawable.img_without_mole;


    public GameLogic(Context context,
                     ArrayList<ImageView> moleViews,
                     TextView scoreText,
                     TextView timerText) {

        this.context = context;
        this.moleViews = moleViews;
        this.tvScore = scoreText;
        this.tvTimer = timerText;

        //Create Mole
        moleList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            moleList.add(new Mole());
        }


        updateScore();
        updateTimer();
    }

    //                 Game Start
    public void startGame() {
        isGameRunning = true;
        startTimer();
        startMoleLoop();
    }

    //                 Game over
    public void stopGame() {
        isGameRunning = false;
        if (moleRunnable != null) {
            handler.removeCallbacks(moleRunnable);
        }
        if (timerRunnable != null) {
            handler.removeCallbacks(timerRunnable);
        }
    }
    private void endGame() {
        isGameRunning = false;

        // Stop all loops
        if (moleRunnable != null) {
            handler.removeCallbacks(moleRunnable);
        }
        if (timerRunnable != null) {
            handler.removeCallbacks(timerRunnable);
        }

        tvTimer.setText("Time: 0");

        //Jump to the PlayerActivity and pass the final score
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra("FINAL_SCORE", score);
        context.startActivity(intent);
    }
    //                 Timer logic
    private void startTimer() {
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isGameRunning) return;

                if (timeLeft > 0) {
                    timeLeft--;
                    updateTimer();
                    handler.postDelayed(this, 1000);
                } else {
                    endGame();
                }
            }
        };

        handler.post(timerRunnable);
    }

    private void updateTimer() {
        tvTimer.setText("Time: " + timeLeft);
    }

    // Ground squirrels appear Refresh loop
    private void startMoleLoop() {

        moleRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isGameRunning) return;

                hideAllMoles();

                // Randomly select a hole to allow the mole to emerge.
                int index = random.nextInt(9);
                moleList.get(index).show();
                moleViews.get(index).setImageResource(imgWithMole);

                // Refresh every 500 milliseconds
                handler.postDelayed(this, 600);
            }
        };

        handler.post(moleRunnable);
    }

    private void hideAllMoles() {
        for (int i = 0; i < 9; i++) {
            moleList.get(i).hide();
            moleViews.get(i).setImageResource(imgWithoutMole);
        }
    }

    //  The player clicks on the mole.
    public void onMoleClicked(int index) {
        if (!isGameRunning) return;
        if (index < 0 || index >= moleList.size()) return;

        Mole mole = moleList.get(index);

        if (mole.isVisible()) {
            score++;
            updateScore();

            mole.hide();
            moleViews.get(index).setImageResource(imgWithoutMole);
        }
    }

    private void updateScore() {
        tvScore.setText("Score: " + score);
    }
}

package com.example.assignment2;

import android.content.Context;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

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

    // 图片资源
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

        // 创建 9 个 Mole
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
        handler.removeCallbacks(moleRunnable);
        handler.removeCallbacks(timerRunnable);
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
                    stopGame();
                    tvTimer.setText("Time: 0");
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

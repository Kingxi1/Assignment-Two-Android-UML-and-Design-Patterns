package com.example.assignment2;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.CountDownTimer;
import android.os.Looper;

import java.util.ArrayList;
import java.util.Random;
import android.content.Intent;

public class GameLogic {

    // UML constant field usage
    private static final long MOLE_DISPLAY_TIME = 600L;    // The appearance time of each ground squirrel (in milliseconds)
    private static final long GAME_DURATION = 30_000L;     //Total game duration 30 seconds

    // UML: currentScore, timeRemaining
    private int currentScore = 0;
    private int timeRemaining = 30;
    private int currentMoleIndex = -1;

    //  UI
    private final Context context;
    private final TextView scoreTextView;
    private final TextView timerTextView;

    // Mole list (UML: moles : ArrayList<Mole>)
    private final ArrayList<Mole> moles = new ArrayList<>();

    // ===== 随机 & Handler（UML: random, moleHandler）=====
    private final Random random = new Random();
    private final Handler moleHandler = new Handler(Looper.getMainLooper());
    private Runnable moleRunnable;

    // Timer (UML: gameTimer : CountDownTimer)
    private CountDownTimer gameTimer;

    // Others
    private boolean isGameRunning = false;

    // // Image resources
    private final int imgWithMole = R.drawable.img_with_mole;
    private final int imgWithoutMole = R.drawable.img_without_mole;



    public GameLogic(Context context,
                     ArrayList<ImageView> moleViews,
                     TextView scoreText,
                     TextView timerText) {

        this.context = context;
        this.scoreTextView = scoreText;
        this.timerTextView = timerText;

        //According to UML: Create an Mole object (index, imageView) for each hole.
        for (int i = 0; i < moleViews.size(); i++) {
            ImageView view = moleViews.get(i);
            moles.add(new Mole(i, view));
        }
        // 初始化 UI
        updateScoreText();
        updateTimerText();

    }

    //                 Game Start
    public void startGame() {
        if (isGameRunning) return;
        isGameRunning = true;

        startTimer();
        startMoleLoop();
    }

    //                 Game over
    public void stopGame() {
        isGameRunning = false;

        if (moleRunnable != null) {
            moleHandler.removeCallbacks(moleRunnable);
        }

        if (gameTimer != null) {
            gameTimer.cancel();
        }
    }

    //The player clicks on a certain hole
    public void onMoleClicked(int index) {
        if (!isGameRunning) return;
        if (index < 0 || index >= moles.size()) return;

        Mole mole = moles.get(index);
        if (mole.isVisible()) {
            currentScore++;
            updateScoreText();
            //The hit ground squirrel immediately vanished.
            hideMole(index);
        }
    }

    //                 Timer logic
    private void startTimer() {
        gameTimer = new CountDownTimer(GAME_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = (int) (millisUntilFinished / 1000);
                updateTimerText();
            }

            @Override
            public void onFinish() {
                timeRemaining = 0;
                updateTimerText();
                endGame();
            }
        };
        gameTimer.start();
    }


    private void updateTimerText() {
        timerTextView.setText("Time: " + timeRemaining);
    }

    // Ground squirrels appear Refresh loop
    private void startMoleLoop() {

        moleRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isGameRunning) return;

                hideAllMoles();

                //Randomly select a hole to reveal the groundhog
                currentMoleIndex = random.nextInt(moles.size());
                showMole(currentMoleIndex);

                // Repeat at the set time
                moleHandler.postDelayed(this, MOLE_DISPLAY_TIME);
            }
        };

        moleHandler.post(moleRunnable);
    }

    // Display the mole with the specified index (UML: showMole(index))
    private void showMole(int index) {
        Mole mole = moles.get(index);
        mole.setVisible(true);
        mole.getImageView().setImageResource(imgWithMole);
    }
    // Hide the mole at the specified index
    private void hideMole(int index) {
        Mole mole = moles.get(index);
        mole.setVisible(false);
        mole.getImageView().setImageResource(imgWithoutMole);
    }

    private void hideAllMoles() {
        for (Mole mole : moles) {
            mole.setVisible(false);
            mole.getImageView().setImageResource(imgWithoutMole);
        }
    }
    // Score logic

    private void updateScoreText() {
        scoreTextView.setText("Score: " + currentScore);
    }

    // Pause the mole cycle and timer (corresponding to UML: stopMoleLoop())
    public void stopMoleLoop() {
        isGameRunning = false;

        if (moleRunnable != null) {
            moleHandler.removeCallbacks(moleRunnable);
        }
        if (gameTimer != null) {
            gameTimer.cancel();
        }
    }
    // First, stop the loop, then jump to PlayerActivity
    private void endGame() {
        // First, call the function "stopMoleLoop()" to ensure that all have stopped.
        stopMoleLoop();

        // Jump to the PlayerActivity and pass the final score
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra("FINAL_SCORE", currentScore);
        context.startActivity(intent);

        // Close GameActivity
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }

}

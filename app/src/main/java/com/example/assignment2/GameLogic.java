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

    // ===== UML 常量字段 =====
    private static final long MOLE_DISPLAY_TIME = 600L;    // 每只地鼠出现时间（毫秒）
    private static final long GAME_DURATION = 30_000L;     // 游戏总时长 30 秒

    // ===== 游戏状态字段（UML: currentScore, timeRemaining 等）=====
    private int currentScore = 0;
    private int timeRemaining = 30;    // 以秒为单位
    private int currentMoleIndex = -1;

    // ===== UI 相关 =====
    private final Context context;
    private final TextView scoreTextView;
    private final TextView timerTextView;

    // ===== 地鼠列表（UML: moles : ArrayList<Mole>）=====
    private final ArrayList<Mole> moles = new ArrayList<>();

    // ===== 随机 & Handler（UML: random, moleHandler）=====
    private final Random random = new Random();
    private final Handler moleHandler = new Handler(Looper.getMainLooper());
    private Runnable moleRunnable;

    // ===== 计时器（UML: gameTimer : CountDownTimer）=====
    private CountDownTimer gameTimer;

    // ===== 其他 =====
    private boolean isGameRunning = false;

    // 图片资源（可以保持不变）
    private final int imgWithMole = R.drawable.img_with_mole;
    private final int imgWithoutMole = R.drawable.img_without_mole;



    public GameLogic(Context context,
                     ArrayList<ImageView> moleViews,
                     TextView scoreText,
                     TextView timerText) {

        this.context = context;
        this.scoreTextView = scoreText;
        this.timerTextView = timerText;

        // 按 UML：为每个洞创建一个 Mole(index, imageView)
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

    // 玩家点击某个洞
    public void onMoleClicked(int index) {
        if (!isGameRunning) return;
        if (index < 0 || index >= moles.size()) return;

        Mole mole = moles.get(index);
        if (mole.isVisible()) {
            currentScore++;
            updateScoreText();
            // 被打中的地鼠立即消失
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

                // 随机选一个坑显示地鼠
                currentMoleIndex = random.nextInt(moles.size());
                showMole(currentMoleIndex);

                // 按设定时间重复
                moleHandler.postDelayed(this, MOLE_DISPLAY_TIME);
            }
        };

        moleHandler.post(moleRunnable);
    }

    // 显示指定 index 的地鼠（UML: showMole(index)）
    private void showMole(int index) {
        Mole mole = moles.get(index);
        mole.setVisible(true);
        mole.getImageView().setImageResource(imgWithMole);
    }
    // 隐藏指定 index 的地鼠
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
    // ================== 分数逻辑 ==================

    private void updateScoreText() {
        scoreTextView.setText("Score: " + currentScore);
    }

    // ================== 结束游戏 ==================

    private void endGame() {
        isGameRunning = false;

        if (moleRunnable != null) {
            moleHandler.removeCallbacks(moleRunnable);
        }
        if (gameTimer != null) {
            gameTimer.cancel();
        }

        // 跳转到 PlayerActivity，并传递最终分数
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra("FINAL_SCORE", currentScore);
        context.startActivity(intent);

        // 可选：结束当前 GameActivity（防止 Back 回到游戏中间状态）
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }
}

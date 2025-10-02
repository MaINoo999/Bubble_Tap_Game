package com.iot.bubbletapgame2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView titleTextView;
    private static final String PREFS_NAME = "BubbleTapGamePrefs";
    private static final String HIGH_SCORES_KEY = "HighScores";

    private ConstraintLayout gameLayout;
    private Button startBtn;
    private TextView scoreTextView, timeTextView, gameOverTextView, highScoresTextView;

    private int score = 0;
    private final int bubbleSizeDp = 60;

    private Handler handler = new Handler();
    private List<Runnable> bubbleRunnables = new ArrayList<>();
    private Random random = new Random();

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 30000; // 30초
    private boolean timerRunning = false;

    private final long minInterval = 200;  // 버블 생성 최소 딜레이 (ms)
    private final long maxInterval = 700;  // 버블 생성 최대 딜레이 (ms)

    private Runnable bubbleRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameLayout = findViewById(R.id.gameLayout);
        startBtn = findViewById(R.id.startBtn);
        scoreTextView = findViewById(R.id.scoreTextView);
        timeTextView = findViewById(R.id.timeTextView);
        gameOverTextView = findViewById(R.id.gameOverTextView);
        highScoresTextView = findViewById(R.id.highScoresTextView);
        titleTextView = findViewById(R.id.titleTextView);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        updateScore();
        updateTimerText();
    }

    private void startGame() {
        score = 0;
        updateScore();
        clearBubbles();

        timeLeftInMillis = 30000;
        updateTimerText();

        gameOverTextView.setVisibility(View.GONE);
        highScoresTextView.setVisibility(View.GONE);
        startBtn.setVisibility(View.GONE);
        titleTextView.setVisibility(View.GONE);
        startBtn.setEnabled(false);

        timerRunning = true;

        bubbleRunnable = new Runnable() {
            @Override
            public void run() {
                if (!timerRunning) return;

                int bubblesToCreate = random.nextInt(4) + 1; // 1~4개 생성
                for (int i = 0; i < bubblesToCreate; i++) {
                    createBubble();
                }

                long nextDelay = minInterval + random.nextInt((int) (maxInterval - minInterval + 1));
                handler.postDelayed(this, nextDelay);
            }
        };
        handler.post(bubbleRunnable);
        bubbleRunnables.add(bubbleRunnable);

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                startBtn.setVisibility(View.VISIBLE);
                startBtn.setEnabled(true);
                titleTextView.setVisibility(View.VISIBLE);
                clearBubbles();
                gameOverTextView.setVisibility(View.VISIBLE);
                timeLeftInMillis = 0;
                updateTimerText();

                saveScore(score);
                showHighScores();
            }
        }.start();
    }

    private void createBubble() {
        final Button bubble = new Button(this);
        int sizePx = dpToPx(bubbleSizeDp);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(sizePx, sizePx);
        bubble.setLayoutParams(params);
        bubble.setBackground(getDrawable(R.drawable.bubble_shape));
        bubble.setText("");
        bubble.setElevation(8f);

        bubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameLayout.removeView(bubble);
                score++;
                updateScore();
            }
        });

        gameLayout.post(new Runnable() {
            @Override
            public void run() {
                int maxX = gameLayout.getWidth() - sizePx;
                int maxY = gameLayout.getHeight() - sizePx;

                if (maxX <= 0 || maxY <= 0) {
                    return;
                }

                int randomX = random.nextInt(maxX + 1);
                int randomY = random.nextInt(maxY + 1);

                bubble.setX(randomX);
                bubble.setY(randomY);

                gameLayout.addView(bubble);

                // 1초 후 버블 제거
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gameLayout.removeView(bubble);
                    }
                }, 1000);
            }
        });
    }

    private void updateScore() {
        scoreTextView.setText("Score: " + score);
    }

    private void updateTimerText() {
        int seconds = (int) (timeLeftInMillis / 1000);
        timeTextView.setText("Time: " + seconds + "s");
    }

    private void clearBubbles() {
        List<View> toRemove = new ArrayList<>();
        int childCount = gameLayout.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View view = gameLayout.getChildAt(i);
            if (view instanceof Button && view.getBackground() != null) {
                if (view.getBackground().getConstantState() != null &&
                        view.getBackground().getConstantState().equals(getDrawable(R.drawable.bubble_shape).getConstantState())) {
                    toRemove.add(view);
                }
            }
        }
        for (View v : toRemove) {
            gameLayout.removeView(v);
        }

        for (Runnable r : bubbleRunnables) {
            handler.removeCallbacks(r);
        }
        bubbleRunnables.clear();

        if (bubbleRunnable != null) {
            handler.removeCallbacks(bubbleRunnable);
        }
    }

    private void saveScore(int newScore) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String scoresString = prefs.getString(HIGH_SCORES_KEY, "");
        List<Integer> scores = new ArrayList<>();

        if (!scoresString.isEmpty()) {
            String[] scoreArr = scoresString.split(",");
            for (String s : scoreArr) {
                try {
                    scores.add(Integer.parseInt(s));
                } catch (NumberFormatException e) {
                    // 무시
                }
            }
        }

        scores.add(newScore);
        Collections.sort(scores, Collections.reverseOrder());

        if (scores.size() > 5) {
            scores = scores.subList(0, 5);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < scores.size(); i++) {
            sb.append(scores.get(i));
            if (i < scores.size() - 1) {
                sb.append(",");
            }
        }

        prefs.edit().putString(HIGH_SCORES_KEY, sb.toString()).apply();
    }

    private void showHighScores() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String scoresString = prefs.getString(HIGH_SCORES_KEY, "");
        if (scoresString.isEmpty()) {
            highScoresTextView.setText("No High Scores");
        } else {
            String[] scoreArr = scoresString.split(",");
            StringBuilder sb = new StringBuilder();
            sb.append("Top 5 High Scores:\n");
            for (int i = 0; i < scoreArr.length; i++) {
                sb.append(String.format("%d위. %s\n", i + 1, scoreArr[i]));
            }
            highScoresTextView.setText(sb.toString());
        }
        highScoresTextView.setVisibility(View.VISIBLE);
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearBubbles();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}

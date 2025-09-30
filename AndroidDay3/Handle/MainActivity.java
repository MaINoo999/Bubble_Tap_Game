package com.iot.handle;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    public static final long DELAY_MS = 100;
    public static final int WHAT_PROGRESS = 1;
    private ProgressBar progressBar;
    private TextView textView;
    private final Handler handler =new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (progressBar.getProgress() < 100) {
                progressBar.setProgress(progressBar.getProgress() + 1);
                textView.setText("Progress: " + progressBar.getProgress() + "%");

                sendEmptyMessageDelayed(WHAT_PROGRESS, DELAY_MS);
            }
        }
    };
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_main);

            progressBar = findViewById(R.id.progressBar);
            textView = findViewById(R.id.textView);
            handler.sendEmptyMessageDelayed(WHAT_PROGRESS, DELAY_MS);
        }
    }

package com.iot.ball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = GameView.class.getSimpleName();

    private final SurfaceHolder holder;
    private Ball ball;
    private boolean goOnPlay = true;

    private final Thread renderer = new Thread() {
        @Override
        public void run() {
            super.run();
            Drawable drawable = getResources().getDrawable(R.drawable.bg_space, null);
            drawable.setBounds(holder.getSurfaceFrame());

            ball.setDelta(15, 30);

            while (goOnPlay) {
                ball.move(holder.getSurfaceFrame());
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    drawable.draw(canvas);
                    ball.draw(canvas);
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    };

    public GameView(Context context) {
        super(context);
        Log.i(TAG, "GameView created");
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        ball = new Ball();
        ball.setImage(getResources().getDrawable(R.drawable.red_ball, null));
        ball.setSize(new Point(100, 100));
        ball.setPoint(new Point(0, 0));
        renderer.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        // 필요시 구현
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        goOnPlay = false;
        try {
            renderer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package com.iot.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomView extends View {
    private static final String TAG = "CustomView";
    private static final int IMAGE_SIZE = 250;      // 그림 크기
    private static final int WHAT_UPDATE = 1;       // 핸들러 반복 메세지
    private static final long DELAY_MS = 33;        // 30프레임
    private static final int DELTA = 20;            // 이동 픽셀 기본값
    private int direction = 1;                      // 이동 방향(1: down, -1: up)
    private final Drawable drawable;                // 그릴 영역
    private final Rect rect = new Rect();           // 그림 영역
    private final Point point = new Point();        // 터치 좌표
    private final Point size = new Point();         // 화면 크기
    private final Handler handler = new Handler(Looper.getMainLooper()){

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (direction == 1){
                if (point.y + DELTA + IMAGE_SIZE <= size.y) point.y += DELTA;
                else {
                    point.y = size.y - IMAGE_SIZE;
                    direction *= -1;
                }
            }   else if (direction == -1){
                if (point.y + DELTA >= 0) point.y -= DELTA;
                else {
                    point.y = 0;
                    direction *= -1;
                }
            }
            invalidate();               // onDraw() 호출, 화면을 다시 그림
            sendEmptyMessageDelayed(WHAT_UPDATE, DELAY_MS);
        }
    };

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.i(TAG,"CustomView() Called");
        drawable = getResources().getDrawable(R.drawable.pd, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        size.y = h;
        size.x = w;
        point.x = (size.x = IMAGE_SIZE) / 2;
        point.y = (size.y = IMAGE_SIZE) / 2;

        Log.i(TAG, "size=" + size);
        Log.i(TAG, "point="+ point);

        if(handler.hasMessages(WHAT_UPDATE)){
            handler.sendEmptyMessageDelayed(WHAT_UPDATE, DELAY_MS);
        }
    }
    // 화면을 그리기 위한 onDraw()
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        rect.left = point.x;
        rect.top = point.y;
        rect.right = point.x + IMAGE_SIZE;
        rect.bottom = point.y + IMAGE_SIZE;
        drawable.setBounds(rect);
        drawable.draw(canvas);
    }
    // 터치 처리를 위한 이벤트
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        point.x = (int) event.getX() - IMAGE_SIZE / 2;          // 터치한 x 좌표
        point.y = (int) event.getY() - IMAGE_SIZE / 2;          // 터치한 Y 좌표
        Log.i(TAG, "onTouchEvent"+ point);
        invalidate();                                           // 시스템이 onDrow 호충
        return super.onTouchEvent(event);
    }
}

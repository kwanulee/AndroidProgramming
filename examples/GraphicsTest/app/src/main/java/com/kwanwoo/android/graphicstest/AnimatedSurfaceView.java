package com.kwanwoo.android.graphicstest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class AnimatedSurfaceView  extends SurfaceView // SurfaceView 상속
                    implements SurfaceHolder.Callback {

    private Thread thread;
    private ArrayList<Ball> arBall = new ArrayList<Ball>();

    SurfaceHolder holder;

    public AnimatedSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);

        thread = new Thread() {
            public void run() {
                draw();
            }
        };
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        thread.start();
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {}

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        try {
            thread.join();
        } catch (InterruptedException e) {}
    }


    private void draw() {
        while (true) {
            //1. 볼을 그릴 캔버스를 lockCanvas() 메소드를 통해 참조하고 캔버스에 락을 걸어 둠
            Canvas canvas  = holder.lockCanvas(null);

            //2. 앞에서 얻은 캔버스에 모든 볼을 이동시키고 그림
            canvas.drawColor(Color.WHITE); // cavas 지우기- 흰색으로 채우기
            synchronized (holder) {
                for (int idx=0; idx<arBall.size(); idx++) {
                    Ball B = arBall.get(idx);
                    B.move(getWidth(),getHeight());
                    B.draw(canvas);
                }
            }

            // 3. 캔버스 객체에 락을 풀어줌
            holder.unlockCanvasAndPost(canvas);
        }
    }


    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x =  (int)event.getX();
            int y =  (int)event.getY();

            arBall.add(new Ball(x, y));

            return true;
        }
        return false;
    }
}

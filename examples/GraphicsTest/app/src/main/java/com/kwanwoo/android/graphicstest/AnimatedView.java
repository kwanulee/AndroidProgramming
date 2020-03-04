package com.kwanwoo.android.graphicstest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;


public class AnimatedView extends View {
    private Paint paint;
    private ArrayList<Ball> arBall = new ArrayList<Ball>();

    public AnimatedView(Context context) {
        super(context);
        init();
    }

    public AnimatedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 4. arBall 리스트에 있는 모든 Ball 객체 그리기
        for (int idx = 0; idx < arBall.size(); idx++) {
            Ball B = arBall.get(idx);  // 5. arBall 리스트에서 볼을 추출
            B.draw(canvas);            // 6. 추출된 볼을 캔버스에 그림
        }
        updateAnimation(); // 7. updateAnimation 메소드 호출
    }

    public boolean onTouchEvent(MotionEvent event) {
        // 1. View 터치 시
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // 2. 터치 위치에 볼 생성하여 arBall 리스트에 추가
            arBall.add(new Ball((int) event.getX(), (int) event.getY()));
            invalidate();  // 3. onDraw() 간접 호출
            return true;
        }
        return false;
    }

    public void updateAnimation() {
        for (int idx = 0; idx < arBall.size(); idx++) {
            Ball B = arBall.get(idx);    // 8. arBall 리스트에서 볼을 추출
            B.move(getWidth(), getHeight());  // 9. 추출된 볼을 애니메이트
        }
        invalidate();  // 10. 다시 onDraw() 간접 호출 (4~10 무한 반복)
    }
}


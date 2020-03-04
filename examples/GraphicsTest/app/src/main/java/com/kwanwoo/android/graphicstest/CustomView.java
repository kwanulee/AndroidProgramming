package com.kwanwoo.android.graphicstest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CustomView extends View {
    private Paint paint;
    // View 클래스의 생성자 재정의
    public CustomView(Context context) {
        super(context);
        init();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // 페인트 객체 초기화
    public void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(100, 100, 200, 200, paint);
        Bitmap bitmap= BitmapFactory.decodeResource( getResources(), R.drawable.ball );
        canvas.drawBitmap(bitmap, 100,300,null);
    }
}

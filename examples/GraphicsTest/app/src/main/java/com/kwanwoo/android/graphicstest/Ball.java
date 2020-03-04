package com.kwanwoo.android.graphicstest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class Ball {
    final int RAD = 24;     // 볼의 반지름
    int mX, mY;             // 볼의 중심 좌표
    int mWidth, mHeight;    // 볼의 넓이와 높이

    int dx, dy;             // 볼의 x축 혹은 y축  이동 방향, 이동 속도 값
    int color;

    public Ball(int x, int y) {
        mX = x;
        mY = y;

        mWidth = mHeight = RAD * 2;           // 원의 반지름 (RAD)의 2배가 Ball의 폭과 높이

        Random Rnd = new Random();
        do {
            dx = Rnd.nextInt(11) - 5;     // -5 ~ 5 중 난수로 x방향 속도 설정
            dy = Rnd.nextInt(11) - 5;     // -5 ~ 5 중 난수로 y방향 속도 설정
        } while (dx == 0 || dy == 0);        //  0은 제외

        // 임의의 색상 설정
        color = Color.rgb(Rnd.nextInt(256), Rnd.nextInt(256), Rnd.nextInt(256));

    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();

        // 바깥쪽은 흐릿하게 안쪽은 진하게 그려지는 원
        for (int r = RAD, alpha = 1; r > 4; r--, alpha += 5) {
            paint.setColor(Color.argb(alpha,
                        Color.red(color),
                        Color.green(color),
                        Color.blue(color)));
            canvas.drawCircle(mX + RAD, mY + RAD, r, paint);
        }
    }

    void move(int width, int height) {
        mX += dx;       // x 좌표값을 dx 만큼 증가
        mY += dy;       // y 좌표값을 dy 만큼 증가

        if (mX < 0 || mX > width - mWidth) {   // 화면 좌우 경계에 닿은 경우
            dx *= -1;                       // 좌우 방향 반전
        }
        if (mY < 0 || mY > height - mHeight) {    // 화면 상하 경계에 닿은 경우
            dy *= -1;                           // 상하 방향 반전

        }
    }
}


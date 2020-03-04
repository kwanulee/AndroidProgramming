package com.example.kwanwoo.animationtest;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    final String TAG = "AnimationTest";
    FrameLayout mFrame;
    ImageView mRocket;
    ImageView mFirework;
    ImageView mCountDown;
    int mScreenHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFrame = (FrameLayout)findViewById(R.id.activity_main);
        mCountDown = (ImageView) findViewById(R.id.countdown);
        mFirework = (ImageView) findViewById(R.id.fire);
        mRocket = (ImageView) findViewById(R.id.rocket);


    }

    @Override
    protected void onResume() {
        super.onResume();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mScreenHeight = displaymetrics.heightPixels;


        startCountDownFrameAnimation();
        startFireTweenAnimation();

        /**
         * 아래 4가지 startRocket 애니메이션 중에 하나를 선택하여 테스트해 보세요.
         */
        startRocketTweenAnimation();
//      startRocketObjectPropertyAnimation();
//      startRocketPropertyAnimationByXML();
//      startRocketValuePropertyAnimation();

    }

    private void startCountDownFrameAnimation() {
        mCountDown.setBackgroundResource(R.drawable.frame_anim);
        AnimationDrawable countdownAnim = (AnimationDrawable) mCountDown.getBackground();
        countdownAnim.start();
    }

    private void startFireTweenAnimation() {
        Animation fire_anim = AnimationUtils.loadAnimation(this, R.anim.fire);
        mFirework.startAnimation(fire_anim);
        fire_anim.setAnimationListener(animationListener);
    }

    private void startRocketTweenAnimation() {
        Animation rocket_anim = AnimationUtils.loadAnimation(this, R.anim.rocket);
        mRocket.startAnimation(rocket_anim);
    }



    private void startRocketObjectPropertyAnimation() {
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(mRocket, "translationY",
                0, -mScreenHeight);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(mRocket,"scaleX",1,0.1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(mRocket,"scaleY",1,0.1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(positionAnimator,scaleXAnimator,scaleYAnimator);

        animatorSet.setDuration(2000);
        animatorSet.setStartDelay(2000);
        animatorSet.start();
        animatorSet.addListener(animatorListener);

    }

    private void startRocketPropertyAnimationByXML() {
        AnimatorSet rocketDogSet = new AnimatorSet();

        AnimatorSet rocketAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.rocket);
        rocketAnimator.setTarget(mRocket);

        rocketAnimator.setStartDelay(2000);
        rocketAnimator.start();
        rocketAnimator.addListener(animatorListener);


    }

    private void startRocketValuePropertyAnimation() {
        ValueAnimator positionAnimator = ValueAnimator.ofFloat(0,-mScreenHeight);
        positionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator){
                float value = (float) valueAnimator.getAnimatedValue();
                mRocket.setTranslationY(value);
            }
        });

        ValueAnimator scaleAnimator = ValueAnimator.ofFloat(1.0f, 0.1f);
        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                mRocket.setScaleX(value);
                mRocket.setScaleY(value);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.play(positionAnimator).with(scaleAnimator);
        animatorSet.setStartDelay(2000);
        animatorSet.setDuration(2000);
        animatorSet.start();
        animatorSet.addListener(animatorListener);

    }

    Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            Log.i(TAG, "onAnimationStart");
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Log.i(TAG, "onAnimationEnd");
            finish();
            startActivity(new Intent(getApplicationContext(), SecondActivity.class));
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            Log.i(TAG, "onAnimationRepeat");
        }
    };

    Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            Log.i(TAG, "onAnimationStart");
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            Log.i(TAG, "onAnimationEnd");
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            Log.i(TAG, "onAnimationCancel");
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
            Log.i(TAG, "onAnimationRepeat");
        }
    };
}

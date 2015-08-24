package com.deepwares.floatinghearts;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Interpolator;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import java.util.Random;

/**
 * Created by Deepak on 8/23/2015.
 */
public class Hearter extends FrameLayout {
    private static final String TAG = Heart.class.getName();
    private Heart mSpawner;
    private int mHeartSize;
    private LayoutParams mHeartLayoutParams;
    public Hearter(Context context) {
        super(context);
        init();
    }

    private static final Random sRandomizer = new Random();

    public Hearter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Hearter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Hearter(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    private void init(){
        mHeartSize = 100;
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setPadding(10, 10, 10, 10);
        mSpawner = new Heart(getContext());
        mSpawner.setTextSize(mHeartSize);
        mHeartLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mHeartLayoutParams.gravity= Gravity.BOTTOM|Gravity.RIGHT;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                addHeart();
            }
        });
        addView(mSpawner, mHeartLayoutParams);

    }

    private void addHeart(){
        final Heart heart = new Heart(getContext());
        heart.setTextSize(mHeartSize);
        AnimatorSet animatorSet = new AnimatorSet();
        boolean bubble = sRandomizer.nextBoolean();
        ObjectAnimator translateX;
        if(bubble){
            boolean mirror = sRandomizer.nextBoolean();
            boolean startLeft = sRandomizer.nextBoolean();
            int leftX = startLeft ? -sRandomizer.nextInt(200) : sRandomizer.nextInt(200) ;
            int rightX = mirror? -leftX : - sRandomizer.nextInt(Math.abs(leftX));
            translateX = ObjectAnimator.ofFloat(heart,TRANSLATION_X,leftX,rightX);
            translateX.setInterpolator(new CycleInterpolator(1));
        } else {
            translateX = ObjectAnimator.ofFloat(heart,TRANSLATION_X,0,0);
        }
        DecelerateInterpolator scaleInterpolator = new DecelerateInterpolator();
        ObjectAnimator scaleXInterpolator = ObjectAnimator.ofFloat(heart,SCALE_X,1.4f,1);
        scaleXInterpolator.setInterpolator(scaleInterpolator);
        ObjectAnimator scaleYInterpolator = ObjectAnimator.ofFloat(heart,SCALE_Y,1.4f,1);
        scaleYInterpolator.setInterpolator(scaleInterpolator);
        ObjectAnimator translateY = ObjectAnimator.ofFloat(heart,TRANSLATION_Y,0,-1000f);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(heart, ALPHA, 1, 0);
        //animatorSet.setInterpolator(new CycleInterpolator(5));
        addView(heart,mHeartLayoutParams);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                Log.d(TAG,"onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Log.d(TAG,"onAnimationEnd");
                removeView(heart);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.setDuration(2000);
        animatorSet.playTogether(translateX,translateY,alphaAnimator,scaleXInterpolator,scaleYInterpolator);
        animatorSet.start();
    }
}

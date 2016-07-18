package com.deepwares.floatinghearts;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Deepak on 8/23/2015.
 */
public class Hearter extends FrameLayout {
    private static final String TAG = Heart.class.getName();
    private Heart mSpawner;
    private int mHeartSize;
    private LayoutParams mHeartLayoutParams;
    private boolean mLeftAligned, mHeartVisible;
    private int mHeartColor;
    private String mText;

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

    public Hearter(Context context, boolean left, boolean heartVisible) {
        super(context);
        mHeartVisible = heartVisible;
        mLeftAligned = left;
        init();
    }

    private void init() {
        mHeartSize = mWidth;
        //setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //setPadding(10, 10, 10, 10);
        mSpawner = new Heart(getContext());
        mSpawner.setTextSize(mHeartSize);
        mHeartLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final Spinner spinner = new Spinner(getContext());
        spinner.setVisibility(GONE);
        FrameLayout.LayoutParams choicesLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        choicesLayoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        choicesLayoutParams.bottomMargin = mHeartSize * 2;
        addView(spinner, choicesLayoutParams);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getContext(), getResources().getIdentifier("dropdown", "layout", getContext().getPackageName()));
        ;
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final List<String> choices = new ArrayList<>();
        choices.add(getResources().getString(getResources().getIdentifier("heart", "string", getContext().getPackageName())));
        choices.add(new String(Character.toChars(0x1F44D)));
        choices.add(new String(Character.toChars(0x1F44F)));
        choices.add(new String(Character.toChars(0x1F60A)));
        choices.add(new String(Character.toChars(0x1F620)));
        mText = choices.get(0);
        //adapter.clear();
        adapter.addAll(choices);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mText = choices.get(position);
                mSpawner.setText(mText);
                spinner.setVisibility(GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner.setVisibility(GONE);
            }
        });


        mHeartLayoutParams.gravity = Gravity.BOTTOM | (mLeftAligned ? Gravity.LEFT : Gravity.RIGHT);
        if (mHeartVisible) {

            mSpawner.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    animateView(getHeart());
                }
            });
            mSpawner.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    spinner.setVisibility(VISIBLE);
                    return true;
                }
            });
            mHeartLayoutParams.bottomMargin = 10;
            mHeartLayoutParams.rightMargin = 10;
            mHeartLayoutParams.leftMargin = 10;

            addView(mSpawner, mHeartLayoutParams);
        } else {
            mSpawner.setVisibility(GONE);
        }
    }

    public void animateView(final View view) {

        AnimatorSet animatorSet = new AnimatorSet();
        boolean bubble = sRandomizer.nextBoolean();
        ObjectAnimator translateX;
        if (bubble) {
            boolean mirror = sRandomizer.nextBoolean();
            boolean startLeft = sRandomizer.nextBoolean();
            int val = sRandomizer.nextInt(200);
            //int leftX = mLeftAligned ? -sRandomizer.nextInt(200) : sRandomizer.nextInt(200);
            int rightX = mLeftAligned ? val : -val;
            translateX = ObjectAnimator.ofFloat(view, TRANSLATION_X, 0, rightX);
            translateX.setInterpolator(new CycleInterpolator(1));
        } else {
            translateX = ObjectAnimator.ofFloat(view, TRANSLATION_X, 0, 0);
        }
        DecelerateInterpolator scaleInterpolator = new DecelerateInterpolator();
        ObjectAnimator scaleXInterpolator = ObjectAnimator.ofFloat(view, SCALE_X, 1.4f, 1);
        scaleXInterpolator.setInterpolator(scaleInterpolator);
        ObjectAnimator scaleYInterpolator = ObjectAnimator.ofFloat(view, SCALE_Y, 1.4f, 1);
        scaleYInterpolator.setInterpolator(scaleInterpolator);
        ObjectAnimator translateY = ObjectAnimator.ofFloat(view, TRANSLATION_Y, 0, -1000f);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, ALPHA, 1, 0);
        //animatorSet.setInterpolator(new CycleInterpolator(5));
        addView(view,mHeartLayoutParams);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                Log.d(TAG, "onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Log.d(TAG, "onAnimationEnd");
                removeView(view);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.setDuration(mDuration);
        animatorSet.playTogether(translateX, translateY, alphaAnimator, scaleXInterpolator, scaleYInterpolator);
        animatorSet.start();
    }

    public View getHeart() {
        final Heart heart = new Heart(getContext());
        heart.setTextSize(mHeartSize);
        if (mHeartColor != 0) {
            heart.setTextColor(mHeartColor);
        }
        if (mText != null) {
            heart.setText(mText);
        }
        heart.setLayoutParams(mHeartLayoutParams);
        return heart;
    }

    int mWidth = 50, mDuration = 2000;

    public void setWidth(int width) {
        mWidth = width;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public void setHeartColor(int color) {
        mHeartColor = color;
        mSpawner.setTextColor(color);
    }
}

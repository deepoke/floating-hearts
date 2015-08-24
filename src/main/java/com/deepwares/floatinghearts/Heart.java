package com.deepwares.floatinghearts;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by Deepak on 8/23/2015.
 */
public class Heart extends TextView {
    public static final Random random = new  Random();

    private static final String HEART = "&#x2764;";
    public Heart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Heart(Context context) {
        super(context);
        init();
    }

    public Heart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Heart(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        setText(R.string.heart);
        setTextColor(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
    }

}

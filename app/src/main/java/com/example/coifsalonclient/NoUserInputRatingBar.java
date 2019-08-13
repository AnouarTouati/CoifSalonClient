package com.example.coifsalonclient;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RatingBar;

public class NoUserInputRatingBar extends android.support.v7.widget.AppCompatRatingBar {


    public NoUserInputRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}

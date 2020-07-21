package com.example.coifsalonclient;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoUserInputRatingBar extends androidx.appcompat.widget.AppCompatRatingBar {


    public NoUserInputRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

}

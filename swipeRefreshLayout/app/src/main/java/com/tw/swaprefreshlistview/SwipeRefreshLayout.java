package com.tw.swaprefreshlistview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class SwipeRefreshLayout extends android.support.v4.widget.SwipeRefreshLayout {
    private static final String TAG = "twDragTest-swipe";

    public SwipeRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public SwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        boolean result = super.onInterceptTouchEvent(e);
        Log.d(TAG, "onInterceptTouchEvent:" + e.getAction() + ", result:" + result);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        boolean result = super.onTouchEvent(e);
        Log.d(TAG, "touchEvent:" + e.getAction() + ", result:" + result);
        return result;
    }

}

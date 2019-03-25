package com.tw.swaprefreshlistview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class RecycleView extends RecyclerView {
    private static final String TAG = "twDragTest-recycle";
    public RecycleView(@NonNull Context context) {
        super(context);
    }

    public RecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        boolean result = super.onTouchEvent(e);
        Log.d(TAG, "touchEvent:" + e.getAction() + ", result:" + result);
        return result;
    }
}

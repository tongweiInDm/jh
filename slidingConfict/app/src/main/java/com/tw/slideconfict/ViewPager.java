package com.tw.slideconfict;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ViewPager extends android.support.v4.view.ViewPager {
    public ViewPager(@NonNull Context context) {
        super(context);
    }

    public ViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private int lastXIntercept, lastYIntercept;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //super.onInterceptTouchEvent(MotionEvent)默认是处理了滑动冲突的
        boolean intercepted = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            /*如果拦截了Down事件,则子类不会拿到这个事件序列*/
            case MotionEvent.ACTION_DOWN:
                lastXIntercept = x;
                lastYIntercept = y;
                intercepted = false;
                super.onInterceptTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                final int deltaX = x - lastXIntercept;
                final int deltaY = y - lastYIntercept;
                /*根据条件判断是否拦截该事件*/
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    intercepted = true;
                } else {
                    intercepted = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;

        }
        lastXIntercept = x;
        lastYIntercept = y;
        return intercepted;

    }
}

package com.tw.image.preview;

import android.support.v4.view.ViewPager;

import com.tw.image.ItemUIEventHandlerImpl;

public abstract class UIEventHandler extends ItemUIEventHandlerImpl
        implements ViewPager.OnPageChangeListener {
    public abstract void onOkBtnClicked();
}

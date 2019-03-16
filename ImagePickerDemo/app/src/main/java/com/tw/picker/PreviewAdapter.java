package com.tw.picker;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;

public class PreviewAdapter extends PagerAdapter {
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return false;
    }
}

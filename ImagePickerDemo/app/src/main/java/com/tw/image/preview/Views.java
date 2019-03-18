package com.tw.image.preview;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.tw.imagepickdemo.R;

public class Views {
    Activity mActivity;
    ViewPager mViewPager;
    Button mButtonOk;

    UIEventHandler mUIEventHandler;

    public Views(Activity activity) {
        mActivity = activity;

        mViewPager = activity.findViewById(R.id.vp_image_pages);
        mButtonOk = activity.findViewById(R.id.btn_ok);

        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUIEventHandler != null) {
                    mUIEventHandler.onOkBtnClicked();
                }
            }
        });
    }

    public void setUIEventHandler(UIEventHandler uiEventHandler) {
        mUIEventHandler = uiEventHandler;
        if (mUIEventHandler != null) {
            mViewPager.removeOnPageChangeListener(mUIEventHandler);
        }
        mViewPager.addOnPageChangeListener(mUIEventHandler);
    }

}

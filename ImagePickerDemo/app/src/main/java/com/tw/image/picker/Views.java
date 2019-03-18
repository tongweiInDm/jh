package com.tw.image.picker;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.tw.imagepickdemo.R;

public class Views {
    Activity mActivity;
    Button mOkButton;
    RecyclerView mRecyclerView;
    UIEventHandler mUIEventHandler;

    public Views(Activity activity) {
        mActivity = activity;
        mOkButton = activity.findViewById(R.id.btn_ok);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUIEventHandler != null) {
                    mUIEventHandler.onOkBtnClicked();
                }
            }
        });
        mRecyclerView = activity.findViewById(R.id.recycle_view_image_grid);
    }

    public void setUIEventHandler(UIEventHandler uiEventHandler) {
        mUIEventHandler = uiEventHandler;
    }

}

package com.tw.image.preview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tw.image.ImageItem;
import com.tw.image.PickResult;
import com.tw.imagepickdemo.R;

import java.lang.ref.SoftReference;
import java.util.List;

public class PreviewActivity extends AppCompatActivity {
    private static final String TAG = "PreviewActivity";
    private static final String EXTRA_INDEX = "index";

    public static void start(Context context, List<ImageItem> imageItems, int currentItemIndex) {
        if (imageItems == null) {
            Log.e(TAG, "Fail to start preview activity, imageItems is null.");
            return;
        }
        if (imageItems.size() == 0) {
            Log.e(TAG, "Fail to start preview activity, imageItems is empty.");
            return;
        }
        sImageItemsHolder = new SoftReference<>(imageItems);
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putExtra(EXTRA_INDEX, currentItemIndex);
        context.startActivity(intent);
    }
    private static SoftReference<List<ImageItem>> sImageItemsHolder;

    private Views mViews;
    private Presenter mDataPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<ImageItem> imageItems = sImageItemsHolder != null ? sImageItemsHolder.get() : null;
        if (imageItems == null) {
            Log.e(TAG, "Fail to start preview activity, imageItems's soft ref return null.");
            finish();
            return;
        }

        setContentView(R.layout.activity_preview);

        int currentItemIndex = getIntent().getIntExtra(EXTRA_INDEX, 0);

        mDataPresenter = new Presenter(this);
        mViews = new Views(this);

        mViews.setUIEventHandler(mDataPresenter.getUIEventHandler());
        mDataPresenter.present(imageItems, PickResult.Holder.sInstance, currentItemIndex, mViews);
    }
}

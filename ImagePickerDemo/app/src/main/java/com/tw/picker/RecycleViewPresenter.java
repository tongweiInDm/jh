package com.tw.picker;

import android.util.Log;

import java.util.List;

/**
 * 这个类负责监听ui的事件，根据ui事件更新pickResult
 * 并在pickResult发生变化时，更新RecycleView
 */
public class RecycleViewPresenter {
    private static final String TAG = "RecycleViewPresenter";

    List<ImageItem> mImageItems;
    PickResult mPickResult;
    RecycleViewAdapter mAdapter;
    boolean mPickResultChanged;
    public RecycleViewPresenter(RecycleViewAdapter adapter) {
        mPickResult = new PickResult(9);
        mAdapter = adapter;

        adapter.setUIListener(new RecycleViewAdapter.UIEventListener() {
            @Override
            public void onItemClicked(ImageItem imageItem, boolean checked) {
                if (checked) {
                    mPickResult.addItem(imageItem);
                } else {
                    mPickResult.removeItem(imageItem);
                }
            }
        });

        mPickResult.registerListener(new PickResult.ChangeListener() {
            @Override
            public void onItemAdded(ImageItem item) {
                mPickResultChanged = true;
            }

            @Override
            public void onItemRemoved(ImageItem item) {
                mPickResultChanged = true;
            }
        });
    }

    void onDataLoaded(List<ImageItem> imageItems) {
        mImageItems = imageItems;
        mAdapter.setData(RecycleViewAdapter.createCellData(imageItems, mPickResult));
        for (ImageItem imageItem : imageItems) {
            Log.d(TAG, "imageItem:" + imageItem.path);
        }
    }

    void update() {
        if (mPickResultChanged) {
            mPickResultChanged = false;
            mAdapter.setData(RecycleViewAdapter.createCellData(mImageItems, mPickResult));
        }
    }

}

package com.tw.image.picker;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.tw.image.ImageItem;
import com.tw.image.MediaStoreDataLoader;
import com.tw.image.PickResult;
import com.tw.image.preview.PreviewActivity;
import com.tw.image.utils.FileProvider;

import java.io.File;
import java.util.List;

/**
 * 这个类负责监听ui的事件，根据ui事件更新pickResult
 * 并在pickResult发生变化时，更新RecycleView
 */
public class Presenter {
    private static final String TAG = "RecycleViewPresenter";

    Activity mActivity;
    List<ImageItem> mImageItems;
    Adapter mAdapter;
    GridLayoutManager mLayoutManager;
    boolean mPickResultChanged;
    PickResult.ChangeListener mChangeListener;

    PickResult mPickResult;
    public Presenter(PickerActivity pickerActivity) {
        mActivity = pickerActivity;
        mAdapter = new Adapter(pickerActivity);
        mAdapter.setUIEventHandler(getUIEventHandler());

        mLayoutManager = new GridLayoutManager(mActivity, 3);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mAdapter.getSpanSize(position);
            }
        });

        mChangeListener = new PickResult.ChangeListener() {
            @Override
            public void onItemAdded(ImageItem item) {
                mPickResultChanged = true;
            }

            @Override
            public void onItemRemoved(ImageItem item) {
                mPickResultChanged = true;
            }
        };
    }

    public UIEventHandler getUIEventHandler() {
        return new UIEventHandler() {
            @Override
            public void onOkBtnClicked() {
                List<ImageItem> imageItems = mPickResult.get();
                if (imageItems == null || imageItems.size() == 0) {
                    Toast.makeText(mActivity, "至少选择一张图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                File file = new File(imageItems.get(0).path);
                Uri uri = FileProvider.getUriForFile(mActivity, file);
                ClipData clipData = ClipData.newRawUri("", uri);
                for (int i = 1;i < imageItems.size();i++) {
                    ImageItem imageItem = imageItems.get(i);
                    file = new File(imageItem.path);
                    uri = FileProvider.getUriForFile(mActivity, file);
                    clipData.addItem(new ClipData.Item(uri));
                }
                Intent intent = new Intent();
                intent.setClipData(clipData);
                mActivity.setResult(Activity.RESULT_OK, intent);
                mActivity.finish();
            }

            @Override
            public void onImageItemClicked(ImageItem imageItem, int position) {
                PreviewActivity.start(mActivity, mImageItems, mImageItems.indexOf(imageItem));
            }

            @Override
            public boolean onUserAddItem(ImageItem imageItem) {
                boolean result = super.onUserAddItem(imageItem);
                Log.v(TAG, "User add " + imageItem.path + ", result " + result);
                return result;
            }

            @Override
            public boolean onUserRemoveItem(ImageItem imageItem) {
                boolean result = super.onUserRemoveItem(imageItem);
                Log.v(TAG, "User remove " + imageItem.path + ", result " + result);
                return result;
            }
        };
    }

    public void present(MediaStoreDataLoader dataLoader, PickResult pickResult, final Views views) {
        mPickResult = pickResult;
        dataLoader.startLoad(new MediaStoreDataLoader.OnImagesLoadedListener() {
            @Override
            public void onImagesLoaded(List<ImageItem> imageItems) {
                onDataLoaded(imageItems, views);
            }
        });
        mPickResult.unregisterListener(mChangeListener);//防止重复的监听
        mPickResult.registerListener(mChangeListener);
    }

    private void onDataLoaded(List<ImageItem> imageItems, Views views) {
        mImageItems = imageItems;

        views.mRecyclerView.setLayoutManager(mLayoutManager);
        views.mRecyclerView.setAdapter(mAdapter);

        mAdapter.setData(Adapter.createCellData(imageItems, mPickResult));
        for (ImageItem imageItem : imageItems) {
            Log.v(TAG, "imageItem, path:" + imageItem.path + ", dateModify:" + imageItem.dateModify);
        }
    }

    public void update() {
        if (mPickResultChanged) {
            mPickResultChanged = false;
            mAdapter.setData(Adapter.createCellData(mImageItems, mPickResult));
        }
    }
}

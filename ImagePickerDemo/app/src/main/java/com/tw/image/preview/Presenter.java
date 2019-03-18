package com.tw.image.preview;

import android.app.Activity;
import android.util.Log;

import com.tw.image.ImageItem;
import com.tw.image.PickResult;

import java.util.List;

/**
 * 负责监听PreviewActivity的ui事件（选中或者取消选中），更新PickerResult
 * 这里监听PickerResult的变化，更新PreviewActivity的提示文字（已选中X项）
 */
public class Presenter {
    private static final String TAG = "PreviewPresenter";

    private Activity mActivity;
    private Views mViews;
    private Adapter mAdapter;

    public Presenter(Activity activity) {
        mActivity = activity;
    }

    public void present(List<ImageItem> itemList, PickResult pickResult, int currentIndex, Views views) {
        mViews = views;
        mAdapter = new Adapter(mActivity);
        mAdapter.setUIEventHandler(getUIEventHandler());//用于监听adapter中动态生成的view所产生的事件
        mAdapter.setData(itemList, pickResult);
        mViews.mViewPager.setAdapter(mAdapter);
        mViews.mViewPager.setCurrentItem(currentIndex);
    }

    public UIEventHandler getUIEventHandler() {
        //PreviewActivity中所有的ui事件处理
        return new UIEventHandler() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                Log.v(TAG, "onPageScrolled");
            }

            @Override
            public void onPageSelected(int i) {
                Log.v(TAG, "onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                Log.v(TAG, "onPageScrollStateChanged");
            }

            @Override
            public void onOkBtnClicked() {
                Log.v(TAG, "User add finish preview.");
                mActivity.finish();
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

}
package com.tw.image;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 负责记录所有被选中的图片项目，由ImagePickerActivity创建实例，
 */
public class PickResult {
    private static final String TAG = "ImagePickResult";

    public static class Holder {
        public static final PickResult sInstance = new PickResult();
    }

    private int mPickLimit;
    private List<ImageItem> mResults = new ArrayList<>();
    private List<ChangeListener> mListeners = new ArrayList<>();

    private PickResult() {
    }

    public void setPickLimit(int pickLimit) {
        mPickLimit = pickLimit;
    }

    public int getPickLimit() {
        return mPickLimit;
    }

    public void registerListener(ChangeListener listener) {
        mListeners.add(listener);
    }

    public void unregisterListener(ChangeListener listener) {
        mListeners.remove(listener);
    }

    public int getSelectedCount() {
        return mResults.size();
    }

    public boolean contain(ImageItem imageItem) {
        return mResults.contains(imageItem);
    }

    public boolean addItem(ImageItem item) {
        if (!mResults.contains(item)) {
            if (mResults.size() >= mPickLimit) {
                Log.e(TAG, "Fail to add item to pick result, pick limit " + mPickLimit
                        + " exhausted.");
                return false;
            }
            boolean result = mResults.add(item);
            if (result) {
                notifyItemAdded(item);
            }
            return result;
        }
        return false;
    }

    public boolean removeItem(ImageItem item) {
        boolean result = mResults.remove(item);
        if (result) {
            notifyItemRmoved(item);
        }
        return result;
    }

    public List<ImageItem> get() {
        return new ArrayList<>(mResults);
    }

    private void notifyItemAdded(ImageItem item) {
        for (ChangeListener listener : mListeners) {
            if (listener != null) {
                listener.onItemAdded(item);
            }
        }
    }

    private void notifyItemRmoved(ImageItem item) {
        for (ChangeListener listener : mListeners) {
            if (listener != null) {
                listener.onItemRemoved(item);
            }
        }
    }

    public interface ChangeListener {
        void onItemAdded(ImageItem item);
        void onItemRemoved(ImageItem item);
    }
}
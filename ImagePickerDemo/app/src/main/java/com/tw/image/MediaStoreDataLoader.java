package com.tw.image;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MediaStoreDataLoader {
    private static String TAG = "MediaStoreDataLoader";

    private static final int IMAGE_LOADER_ID = 0;

    private static final int INDEX_DISPLAY_NAME = 0;
    private static final int INDEX_DATA = 1;
    private static final int INDEX_SIZE = 2;
    private static final int INDEX_WIDTH = 3;
    private static final int INDEX_HEIGHT = 4;
    private static final int INDEX_MIME_TYPE = 5;
    private static final int INDEX_DATE_MODIFIED = 6;

    private final String[] IMAGE_PROJECTION = {     //查询图片需要的数据列
            MediaStore.Images.Media.DISPLAY_NAME,   //图片的显示名称  aaa.jpg
            MediaStore.Images.Media.DATA,           //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
            MediaStore.Images.Media.SIZE,           //图片的大小，long型  132492
            MediaStore.Images.Media.WIDTH,          //图片的宽度，int型  1920
            MediaStore.Images.Media.HEIGHT,         //图片的高度，int型  1080
            MediaStore.Images.Media.MIME_TYPE,      //图片的类型     image/jpeg
            MediaStore.Images.Media.DATE_MODIFIED};    //图片最近一次被修改的时间，long型  1450518608

    private FragmentActivity mActivity;
    private LoaderManager mLoaderManager;

    public MediaStoreDataLoader(FragmentActivity activity) {
        this.mActivity = activity;
        this.mLoaderManager = LoaderManager.getInstance(activity);
    }

    public void startLoad(final OnImagesLoadedListener listener) {
        mLoaderManager.initLoader(IMAGE_LOADER_ID, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @NonNull
                    @Override
                    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle bundle) {
                        return MediaStoreDataLoader.this.onCreateLoader(id, bundle);
                    }

                    @Override
                    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
                        MediaStoreDataLoader.this.onLoadFinished(loader, cursor, listener);
                        mLoaderManager.destroyLoader(IMAGE_LOADER_ID);
                    }

                    @Override
                    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
                        MediaStoreDataLoader.this.onLoaderReset(loader);
                    }
                });
    }

    private Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mActivity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_PROJECTION, null, null,
                IMAGE_PROJECTION[INDEX_DATE_MODIFIED] + " DESC");
    }

    private void onLoadFinished(Loader<Cursor> loader, Cursor data, OnImagesLoadedListener listener) {
        if (data == null) {
            Log.e(TAG, "Unexpected null cursor when load finished.");
            return;
        }
        if (listener == null) {
            Log.e(TAG, "Unexpected null load listener when load finished.");
            return;
        }
        ArrayList<ImageItem> allImages = new ArrayList<>();
        while (data.moveToNext()) {
            //查询数据
            String imageName = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[INDEX_DISPLAY_NAME]));
            String imagePath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[INDEX_DATA]));

            File file = new File(imagePath);
            if (!file.exists() || file.length() <= 0) {
                continue;
            }

            long imageSize = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[INDEX_SIZE]));
            int imageWidth = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[INDEX_WIDTH]));
            int imageHeight = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[INDEX_HEIGHT]));
            String imageMimeType = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[INDEX_MIME_TYPE]));
            long imageDateModify = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[INDEX_DATE_MODIFIED]));
            //封装实体
            ImageItem imageItem = new ImageItem();
            imageItem.name = imageName;
            imageItem.path = imagePath;
            imageItem.size = imageSize;
            imageItem.width = imageWidth;
            imageItem.height = imageHeight;
            imageItem.mimeType = imageMimeType;
            imageItem.dateModify = imageDateModify;
            allImages.add(imageItem);
        }

        listener.onImagesLoaded(allImages);
    }

    private void onLoaderReset(Loader<Cursor> loader) {
        Log.v(TAG, "Loader reset.");
    }

    public void release() {
        if (mLoaderManager != null) {
            mLoaderManager.destroyLoader(IMAGE_LOADER_ID);
            mLoaderManager = null;
        }
    }

    /**
     * 所有图片加载完成的回调接口
     */
    public interface OnImagesLoadedListener {
        void onImagesLoaded(List<ImageItem> imageItems);
    }
}
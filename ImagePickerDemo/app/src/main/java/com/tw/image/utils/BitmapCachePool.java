package com.tw.image.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.LruCache;

import java.lang.ref.SoftReference;

public class BitmapCachePool {
    private static final int MAX_POOL_SIZE = 10 * 1024 * 1024;

    public static class HOLDER {
        public static final BitmapCachePool sInstance = new BitmapCachePool();
    }

    private LruCache<String, BitmapHolder> mBitmapCache;

    private BitmapCachePool() {
        mBitmapCache = new LruCache<String, BitmapHolder>(MAX_POOL_SIZE) {
            @Override
            protected int sizeOf(String path, BitmapHolder holder) {
                return holder.bitmapSize;
            }
        };
    }

    public void fetchBitmap(final String filePath, final int reqWidth, final int reqHeight,
                            final BitmapConsumer consumer) {
        BitmapHolder bitmapHolder = mBitmapCache.get(filePath);
        Bitmap bitmap = bitmapHolder == null ? null : bitmapHolder.bitmapRef.get();
        if (bitmap == null) {
            //FIXME:针对同一个任务可能出现重复投放的问题,在整个流程跑通之前暂时不管整个问题
            new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... voids) {
                    return Utils.decodeSampledBitmap(filePath, reqWidth, reqHeight);
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    if (bitmap != null) {
                        mBitmapCache.remove(filePath);
                        BitmapHolder holder = new BitmapHolder();
                        holder.path = filePath;
                        holder.bitmapSize = bitmap.getByteCount();
                        holder.bitmapRef = new SoftReference<>(bitmap);
                        mBitmapCache.put(filePath, holder);
                    }
                    if (consumer != null) {
                        consumer.consume(bitmap);
                    }
                }

            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            consumer.consume(bitmap);
        }
    }

    public interface BitmapConsumer {
        void consume(Bitmap bitmap);
    }

    private static class BitmapHolder {
        String path;
        SoftReference<Bitmap> bitmapRef;
        int bitmapSize;
    }
}

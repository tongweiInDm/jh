package com.tw.image.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static void displayImage(final ImageView imageView, final String filePath, final int reqWidth, final int reqHeight) {
        BitmapCachePool.HOLDER.sInstance.fetchBitmap(filePath, reqWidth, reqHeight, new BitmapCachePool.BitmapConsumer() {
            @Override
            public void consume(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        });
    }

    /**
     * 根据屏幕宽度与密度计算GridView显示的列数， 最少为三列，并获取Item宽度
     */
    public static int getImageItemWidth(Activity activity) {
        int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
        int densityDpi = activity.getResources().getDisplayMetrics().densityDpi;
        int cols = screenWidth / densityDpi;
        cols = cols < 3 ? 3 : cols;
        int columnSpace = (int) (2 * activity.getResources().getDisplayMetrics().density);
        return (screenWidth - columnSpace * (cols - 1)) / cols;
    }

    public static Bitmap decodeSampledBitmap(String filePath, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);

    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }

        int wight = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;

        if (height > reqHeight || wight > reqWidth) {

            int halfHeight = height / 2;
            int halfWidth = wight / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private static SimpleDateFormat sSimpleDateFormat;//用于将一个时间

    /**
     * @param timeStamp 以秒为单位
     * @return
     */
    public static String getDate(long timeStamp) {
        if (sSimpleDateFormat == null) {
            sSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        return sSimpleDateFormat.format(new Date(timeStamp * 1000));
    }
}

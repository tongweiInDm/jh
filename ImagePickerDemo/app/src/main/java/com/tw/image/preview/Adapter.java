package com.tw.image.preview;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.tw.image.ImageItem;
import com.tw.image.IItemUIEventHandler;
import com.tw.image.PickResult;
import com.tw.image.utils.Utils;
import com.tw.imagepickdemo.R;

import java.util.List;

public class Adapter extends PagerAdapter {

    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private IItemUIEventHandler mUIEventHandler;
    private int mImageSize;

    private List<ImageItem> mImageItems;
    private PickResult mPickResult;

    public Adapter(Activity activity) {
        mActivity = activity;
        mLayoutInflater = LayoutInflater.from(activity);
        mImageSize = activity.getResources().getDisplayMetrics().widthPixels;
    }

    public void setUIEventHandler(IItemUIEventHandler itemUIEventHandler) {
        mUIEventHandler = itemUIEventHandler;
    }

    public void setData(List<ImageItem> imageItems, PickResult pickResult) {
        mImageItems = imageItems;
        mPickResult = pickResult;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mImageItems.size();
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mLayoutInflater.inflate(R.layout.page_preview, container, false);
        final ImageItem imageItem = mImageItems.get(position);
        ImageView imageView = view.findViewById(R.id.iv_image);
        final CheckBox checkBox = view.findViewById(R.id.cb_selected);
        Utils.displayImage(imageView, imageItem.path, mImageSize, mImageSize);
        checkBox.setChecked(mPickResult.contain(imageItem));
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    if (!mUIEventHandler.onUserAddItem(imageItem)) {
                        //添加失败了，可能的原因，所选的文件数超过上限了
                        checkBox.setChecked(false);
                    }
                } else {
                    mUIEventHandler.onUserRemoveItem(imageItem);
                }
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
}

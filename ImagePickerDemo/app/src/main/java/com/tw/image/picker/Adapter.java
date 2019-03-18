package com.tw.image.picker;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tw.image.ImageItem;
import com.tw.image.PickResult;
import com.tw.image.utils.Utils;
import com.tw.imagepickdemo.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private static final int VIEW_TYPE_DIVIDER = 0;
    private static final int VIEW_TYPE_IMAGE = 1;

    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private UIEventHandler mUIEventHandler;
    private int mImageSize;

    private List<CellData> mCellData;

    public Adapter(Activity activity) {
        mActivity = activity;
        mLayoutInflater = LayoutInflater.from(activity);
        mImageSize = Utils.getImageItemWidth(activity);
    }

    public void setUIEventHandler(UIEventHandler uiEventHandler) {
        mUIEventHandler = uiEventHandler;
    }

    public void setData(List<CellData> cellData) {
        mCellData = cellData;
        notifyDataSetChanged();
    }

    public int getSpanSize(int position) {
        if (mCellData != null) {
            int viewType = mCellData.get(position).getType();
            if (viewType == VIEW_TYPE_DIVIDER) {
                return 3;
            }
        }
        return 1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DIVIDER) {
            View divideViewCell = mLayoutInflater.inflate(R.layout.item_divider, parent,
                    false);
            return new DividerViewHolder(divideViewCell);
        } else if (viewType == VIEW_TYPE_IMAGE) {
            View imageViewCell = mLayoutInflater.inflate(R.layout.item_image, parent, false);
            return new ImageViewHolder(imageViewCell, mImageSize);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(mCellData.get(position), position);
    }

    @Override
    public int getItemViewType(int position) {
        return mCellData == null ? VIEW_TYPE_IMAGE : mCellData.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mCellData == null ? 0 : mCellData.size();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        //TODO:主动清理view使用的Bitmap
    }

    public static abstract class ViewHolder extends RecyclerView.ViewHolder {
        int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(CellData cellData, int position) {
            this.position = position;
        }
    }

    private class DividerViewHolder extends ViewHolder {
        TextView textViewTitle;

        public DividerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_title);
        }

        @Override
        public void bind(CellData cellData, int position) {
            super.bind(cellData, position);
            DividerCellData dividerCellData = (DividerCellData) cellData;
            textViewTitle.setText(dividerCellData.getDisplayName());
        }
    }

    private class ImageViewHolder extends ViewHolder implements
            CompoundButton.OnCheckedChangeListener, View.OnClickListener {
        ImageView imageView;
        View maskView;
        CheckBox checkBoxSelected;
        int reqSize;

        public ImageViewHolder(@NonNull View itemView, int size) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_image);
            maskView = itemView.findViewById(R.id.v_image_mask);
            checkBoxSelected = itemView.findViewById(R.id.cb_image_status);
            reqSize = size;
            itemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageSize)); //让图片是个正方形
        }

        @Override
        public void bind(CellData cellData, int position) {
            super.bind(cellData, position);
            ImageCellData imageCellData = (ImageCellData) cellData;
            Utils.displayImage(imageView, imageCellData.item.path, reqSize, reqSize);
            maskView.setVisibility(imageCellData.selected ? View.VISIBLE : View.GONE);
            checkBoxSelected.setOnCheckedChangeListener(null);
            checkBoxSelected.setChecked(imageCellData.selected);
            checkBoxSelected.setOnCheckedChangeListener(this);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            maskView.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            ImageCellData imageCellData = (ImageCellData) mCellData.get(position);
            if (isChecked) {
                if (!mUIEventHandler.onUserAddItem(imageCellData.item)) {
                    //添加失败了，可能的原因，所选的文件数超过上限了
                    checkBoxSelected.setChecked(false);
                }
            } else {
                mUIEventHandler.onUserRemoveItem(imageCellData.item);
            }
        }

        @Override
        public void onClick(View v) {
            ImageCellData imageCellData = (ImageCellData) mCellData.get(position);
            mUIEventHandler.onImageItemClicked(imageCellData.item, position);
        }
    }

    private static final Comparator<ImageItem> sImageItemComparator = new Comparator<ImageItem>() {
        @Override
        public int compare(ImageItem o1, ImageItem o2) {
            return (int) (o2.dateModify - o1.dateModify);
        }
    };

    private static final Comparator<String> sDateComparator = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return o2.compareTo(o1);
        }
    };

    public static List<CellData> createCellData(List<ImageItem> imageItems, PickResult pickResult) {
        SortedMap<String, TreeSet<ImageItem>> dailyImageGroup = new TreeMap<>(sDateComparator);
        for (ImageItem imageItem : imageItems) {
            String date = Utils.getDate(imageItem.dateModify);
            TreeSet<ImageItem> itemSet = dailyImageGroup.get(date);
            if (itemSet == null) {
                itemSet = new TreeSet<>(sImageItemComparator);
                dailyImageGroup.put(date, itemSet);
            }
            itemSet.add(imageItem);
        }

        List<CellData> cellDataList = new ArrayList<>();
        for (Map.Entry<String, TreeSet<ImageItem>> entry : dailyImageGroup.entrySet()) {
            DividerCellData dividerCellData = new DividerCellData();
            dividerCellData.name = entry.getKey();
            cellDataList.add(dividerCellData);
            TreeSet<ImageItem> imageItemList = entry.getValue();
            for (ImageItem imageItem : imageItemList) {
                ImageCellData imageCellData = new ImageCellData();
                imageCellData.item = imageItem;
                cellDataList.add(imageCellData);
            }
        }

        return cellDataList;
    }

    public static abstract class CellData {
        public abstract int getType();
    }

    public static class DividerCellData extends CellData {
        String name;

        @Override
        public int getType() {
            return VIEW_TYPE_DIVIDER;
        }

        public String getDisplayName() {
            //TODO:需要把当天的日期转成今天，昨天的日期转成昨天，其他日期不变
            return name;
        }
    }

    public static class ImageCellData extends CellData {
        ImageItem item;
        boolean selected;

        @Override
        public int getType() {
            return VIEW_TYPE_IMAGE;
        }
    }
}

package com.tw.image.picker;

import com.tw.image.ItemUIEventHandlerImpl;
import com.tw.image.ImageItem;

import java.util.List;

public abstract class UIEventHandler extends ItemUIEventHandlerImpl {
    public abstract void onOkBtnClicked();
    public abstract void onImageItemClicked(ImageItem imageItem, int position);
    public abstract List<Adapter.ImageCellData> onUserAddItemList(List<Adapter.ImageCellData> imageItemList);
    public abstract boolean onUserRemoveItemList(List<Adapter.ImageCellData> imageItemList);
}

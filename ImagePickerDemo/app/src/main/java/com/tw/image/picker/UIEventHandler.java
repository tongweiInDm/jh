package com.tw.image.picker;

import com.tw.image.ItemUIEventHandlerImpl;
import com.tw.image.ImageItem;

public abstract class UIEventHandler extends ItemUIEventHandlerImpl {
    public abstract void onOkBtnClicked();
    public abstract void onImageItemClicked(ImageItem imageItem, int position);
}

package com.tw.image;

public class ItemUIEventHandlerImpl implements IItemUIEventHandler {
    @Override
    public boolean onUserAddItem(ImageItem imageItem) {
        return PickResult.Holder.sInstance.addItem(imageItem);
    }

    @Override
    public boolean onUserRemoveItem(ImageItem imageItem) {
        return PickResult.Holder.sInstance.removeItem(imageItem);
    }
}

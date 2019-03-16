package com.tw.picker;

/**
 * 负责监听PreviewActivity的ui事件（选中或者取消选中），更新PickerResult
 * 这里暂时不需要监听PickerResult的变化，更新PreviewActivity
 */
public class PreviewPresenter {

    private PickResult mPickResult;
    private PreviewAdapter mPreviewAdapter;
    public PreviewPresenter(PickResult result, PreviewAdapter adapter) {
        mPickResult = result;
        mPreviewAdapter = adapter;
    }
}
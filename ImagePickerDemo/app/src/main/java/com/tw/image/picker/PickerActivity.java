package com.tw.image.picker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.tw.image.MediaStoreDataLoader;
import com.tw.image.PickResult;
import com.tw.imagepickdemo.R;

public class PickerActivity extends AppCompatActivity {
    private static final String TAG = "PickerActivity";
    private static final int PERMISSION_REQ_CODE = 1234;
    static final int REQ_PREVIEW_CODE = 1235;

    private static final String EXTRA_PICK_LIMIT = "pick_limit";
    private static final int DEFAULT_PICK_LIMIT = 9;

    private Views mViews;
    private MediaStoreDataLoader mMediaStoreDataLoader;
    private Presenter mDataPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);
        mMediaStoreDataLoader = new MediaStoreDataLoader(this);
        int pickLimit = getIntent().getIntExtra(EXTRA_PICK_LIMIT, DEFAULT_PICK_LIMIT);
        PickResult pickResult = PickResult.Holder.sInstance;
        pickResult.setPickLimit(pickLimit);
        pickResult.clear();

        mViews = new Views(this);
        mDataPresenter = new Presenter(this);
        mViews.setUIEventHandler(mDataPresenter.getUIEventHandler());
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
            return;
        }
        mDataPresenter.present(mMediaStoreDataLoader, pickResult, mViews);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "hashCode():" + hashCode());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_CODE
                && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            mDataPresenter.present(mMediaStoreDataLoader, PickResult.Holder.sInstance, mViews);
        } else {
            Toast.makeText(PickerActivity.this,
                    getString(R.string.picker_activity_permission_tip), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_PREVIEW_CODE) {
            mDataPresenter.update();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaStoreDataLoader != null) {
            mMediaStoreDataLoader.release();
            mMediaStoreDataLoader = null;
        }
    }
}
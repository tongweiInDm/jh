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

/**
 * TODO:目前需要考虑和处理PickerActivity被启多个实例的情况
 * TODO:以及PickerActivity被PreviewActivity所覆盖时，PickerActivity被系统杀死，之后又重启的情况
 */
public class PickerActivity extends AppCompatActivity {
    private static final String TAG = "PickerActivity";
    private static final int PERMISSION_REQ_CODE = 1234;
    private static final int REQ_PREVIEW_CODE = 1235;

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
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "hashCode():" + hashCode());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
            Toast.makeText(PickerActivity.this, "需要读取sd卡的权限",
                    Toast.LENGTH_SHORT).show();
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
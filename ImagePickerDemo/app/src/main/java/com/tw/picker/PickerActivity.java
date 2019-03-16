package com.tw.picker;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tw.imagepickdemo.R;

import java.io.File;
import java.util.List;

public class PickerActivity extends AppCompatActivity {
    private static final String TAG = "PickerActivity";
    private static final int PERMISSION_REQ_CODE = 1234;
    private static final int REQ_PREVIEW_CODE = 1235;

    private RecycleViewPresenter mPresenter;
    private MediaStoreDataLoader mMediaStoreDataLoader;
    private RecycleViewAdapter mDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);
        mMediaStoreDataLoader = new MediaStoreDataLoader(this);
        mDataAdapter = new RecycleViewAdapter(this);
        mPresenter = new RecycleViewPresenter(mDataAdapter);
        Button okButton = findViewById(R.id.btn_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ImageItem> imageItems = mPresenter.mPickResult.get();
                if (imageItems == null || imageItems.size() == 0) {
                    Toast.makeText(PickerActivity.this, "至少选择一张图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                File file = new File(imageItems.get(0).path);
                Uri uri = PickedFileProvider.getUriForFile(PickerActivity.this, file);
                ClipData clipData = ClipData.newRawUri("", uri);
                for (int i = 1;i < imageItems.size();i++) {
                    ImageItem imageItem = imageItems.get(i);
                    file = new File(imageItem.path);
                    uri = PickedFileProvider.getUriForFile(PickerActivity.this, file);
                    clipData.addItem(new ClipData.Item(uri));
                }
                Intent intent = new Intent();
                intent.setClipData(clipData);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycle_view_image_grid);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mDataAdapter.getSpanSize(position);
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mDataAdapter);

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
            return;
        }
        loadData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_CODE
                && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            loadData();
        } else {
            Toast.makeText(PickerActivity.this, "需啊读取sd卡的权限",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_PREVIEW_CODE) {
            mPresenter.update();
        }
    }

    private void loadData() {
        mMediaStoreDataLoader.startLoad(new MediaStoreDataLoader.OnImagesLoadedListener() {
            @Override
            public void onImagesLoaded(List<ImageItem> imageItems) {
                mPresenter.onDataLoaded(imageItems);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaStoreDataLoader.release();
        mMediaStoreDataLoader = null;
        mDataAdapter = null;
    }
}

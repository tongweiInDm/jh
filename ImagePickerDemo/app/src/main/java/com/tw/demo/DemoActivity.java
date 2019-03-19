package com.tw.demo;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tw.image.picker.PickerActivity;
import com.tw.imagepickdemo.R;

public class DemoActivity extends AppCompatActivity {

    private static final int REQ_CODE_PICK = 1234;
    private TextView mTextViewPickResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_pick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DemoActivity.this, PickerActivity.class);
                startActivityForResult(intent, REQ_CODE_PICK);
            }
        });
        mTextViewPickResult = findViewById(R.id.tv_pick_result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQ_CODE_PICK) {
            if (resultCode != RESULT_OK) {
                String cancelTip = getString(R.string.picker_activity_user_cancel);
                Toast.makeText(DemoActivity.this, cancelTip, Toast.LENGTH_SHORT).show();
                mTextViewPickResult.setText(cancelTip);
                return;
            }
            String pickResult;
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0;i < clipData.getItemCount();i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri uri = item.getUri();
                    builder.append(String.valueOf(uri) + "\n");
                }
                pickResult = builder.toString();
            } else {
                pickResult = "clipData not found.";
            }
            mTextViewPickResult.setText(pickResult);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
package com.tw.picker;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import java.io.File;

public class PickedFileProvider extends FileProvider {
    public static Uri getUriForFile(@NonNull Context context, @NonNull File file) {
        return getUriForFile(context, context.getPackageName() + ".pick.provider", file);
    }
}

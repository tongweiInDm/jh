package com.tw.image.utils;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;

public class FileProvider extends android.support.v4.content.FileProvider {
    public static Uri getUriForFile(@NonNull Context context, @NonNull File file) {
        return getUriForFile(context, context.getPackageName() + ".file.provider", file);
    }
}

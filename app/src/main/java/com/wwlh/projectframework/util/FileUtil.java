package com.wwlh.projectframework.util;

import android.text.TextUtils;

import java.io.File;

public class FileUtil {

    public static File getFileByPath(final String filePath) {
        return TextUtils.isEmpty(filePath) ? null : new File(filePath);
    }

    public static boolean isFileExists(final String filePath) {
        return isFileExists(getFileByPath(filePath));
    }

    public static boolean isFileExists(final File file) {
        return file != null && file.exists();
    }

}

package com.bloodsoul.projectframework.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.bloodsoul.projectframework.common.MainApplication;

/**
 *  1. 根据不同模块定义SharedPreferences文件名
 *  2. 根据功能定义方法名
 *  3. 一个SharedPreferences文件不能存储过多信息, 视情况而定
 */
public class SharepreferenceUtil {

    // SharedPreferences文件名
    private static final String SP_FILENAME_COMMON = "sp_filename_common";
    // KEY
    private static final String KEY_LANGUAGE = "language";

    private static SharedPreferences getSharedPreferences(String spName) {
        return MainApplication.getInstance().getApplicationContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor(String spName) {
        return getSharedPreferences(spName).edit();
    }

    private static String getString(String spName, String key) {
        return getSharedPreferences(spName).getString(key, null);
    }

    private static void putString(String spName, String key, String value) {
        getEditor(spName).putString(key, value).apply();
    }

    private static int getInt(String spName, String key) {
        return getSharedPreferences(spName).getInt(key, -1);
    }

    private static void putInt(String spName, String key, int value) {
        getEditor(spName).putInt(key, value).apply();
    }

    private static long getLong(String spName, String key) {
        return getSharedPreferences(spName).getLong(key, -1);
    }

    private static void putLong(String spName, String key, long value) {
        getEditor(spName).putLong(key, value).apply();
    }

    private static float getFloat(String spName, String key) {
        return getSharedPreferences(spName).getFloat(key, -1f);
    }

    private static void putFloat(String spName, String key, long value) {
        getEditor(spName).putFloat(key, value).apply();
    }

    private static boolean getBoolean(String spName, String key) {
        return getSharedPreferences(spName).getBoolean(key, false);
    }

    private static void putBoolean(String spName, String key, boolean value) {
        getEditor(spName).putBoolean(key, value).apply();
    }

    private static SharedPreferences getCommonSharedPreferences() {
        return getSharedPreferences(SP_FILENAME_COMMON);
    }

    private static SharedPreferences.Editor getCommonEditor() {
        return getSharedPreferences(SP_FILENAME_COMMON).edit();
    }

    /* 上面为固定代码(除了添加SharedPreferences文件名), 定义方法名从下面开始 */

    public static String getLanuage() {
        return getCommonSharedPreferences().getString(KEY_LANGUAGE, "ch");
    }

    public static void putLanuage(String value) {
        getCommonEditor().putString(KEY_LANGUAGE, value).apply();
    }

}

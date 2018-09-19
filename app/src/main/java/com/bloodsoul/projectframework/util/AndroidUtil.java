package com.bloodsoul.projectframework.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import com.bloodsoul.projectframework.common.MainApplication;

public class AndroidUtil {

    private static final String TAG = "AndroidUtil";

    public static final int ICE_CREAM_SANDWICH = 14;
    public static final int ICE_CREAM_SANDWICH_MR1 = 15;
    public static final int JELLY_BEAN = 16;
    public static final int JELLY_BEAN_MR1 = 17;
    public static final int JELLY_BEAN_MR2 = 18;
    public static final int KITKAT = 19; // 4.4
    public static final int KITKAT_WATCH = 20;
    public static final int LOLLIPOP = 21;
    public static final int LOLLIPOP_MR1 = 22;
    public static final int MARSH_MALLOW = 23;
    public static final int N = 24; // 7.0
    public static final int N1 = 25;
    public static final int O = 26; // 8.0
    public static final int O1 = 27;
    public static final int P = 28; // 9.0

    public static String getDensity(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        return screenHeight + "*" + screenWidth;
    }

    public static int getScreenHeight() {
        Context context = MainApplication.getInstance();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenHeight = wm.getDefaultDisplay().getHeight();
        return screenHeight;
    }

    public static int getScreenOrientation(Context context) {
        return context.getResources().getConfiguration().orientation;
    }

    public static boolean isAboveSpecifiedVersion(int versionCode) {
        return Build.VERSION.SDK_INT >= versionCode;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((px - 0.5) / scale);
    }

}

package com.wwlh.projectframework.ui.statusbar;

import android.app.Activity;
import android.os.Build;

public class StatusBar {

    public static void setStatusBarColor(Activity activity, int statusColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusbarLollipop.setStatusBarColor(activity, statusColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            StatusbarKitKat.setStatusBarColor(activity, statusColor);
        }
    }

}

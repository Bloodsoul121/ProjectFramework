package com.wwlh.projectframework.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.wwlh.projectframework.common.MainApplication;

import java.io.File;

public class InstallUtil {

    private static Context getContext() {
        return MainApplication.getInstance().getApplicationContext();
    }

    public static boolean isInstallApp(final String packageName) {
        return !TextUtils.isEmpty(packageName) && getLaunchAppIntent(packageName) != null;
    }

    public static void installApp(final String filePath, final String authority) {
        installApp(FileUtil.getFileByPath(filePath), authority);
    }

    private static void installApp(final File file, final String authority) {
        if (!FileUtil.isFileExists(file)) return;
        getContext().startActivity(getInstallAppIntent(file, authority));
    }

    /**
     *  静默安装 App , 目前用不上
     */
    public static boolean installAppSilent(final String filePath) {
        File file = FileUtil.getFileByPath(filePath);
        if (!FileUtil.isFileExists(file)) return false;
        boolean isRoot = isDeviceRooted();
        String command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install " + filePath;
        ShellUtil.CommandResult commandResult = ShellUtil.execCmd(command, isRoot);
        if (commandResult.successMsg != null && commandResult.successMsg.toLowerCase().contains("success")) {
            return true;
        } else {
            command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib64 pm install " + filePath;
            commandResult = ShellUtil.execCmd(command, isRoot, true);
            return commandResult.successMsg != null && commandResult.successMsg.toLowerCase().contains("success");
        }
    }

    private static boolean isDeviceRooted() {
        String su = "su";
        String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
                "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/"};
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }

    private static Intent getLaunchAppIntent(final String packageName) {
        Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) return null;
        return getIntent(intent, false);
    }

    private static Intent getInstallAppIntent(final File file, final String authority) {
        if (file == null) return null;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        String type = "application/vnd.android.package-archive";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            data = Uri.fromFile(file);
        } else {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            data = FileProvider.getUriForFile(getContext(), authority, file);
        }
        intent.setDataAndType(data, type);
        return getIntent(intent, true);
    }

    private static Intent getIntent(final Intent intent, final boolean isNewTask) {
        return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
    }

}

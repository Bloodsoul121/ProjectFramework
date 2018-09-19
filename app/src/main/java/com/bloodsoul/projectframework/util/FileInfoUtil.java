package com.bloodsoul.projectframework.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import java.util.HashMap;

public class FileInfoUtil {

    public static final String SIZE = "FILE_SIZE";

    public static FileAPKInfo getAPKInfo(Context context, String filePath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
        if (pkgInfo != null) {
            ApplicationInfo appInfo = pkgInfo.applicationInfo;
            appInfo.sourceDir = filePath;
            appInfo.publicSourceDir = filePath;
            FileAPKInfo info = new FileAPKInfo();
            info.appName = pm.getApplicationLabel(appInfo).toString();
            info.packageName = appInfo.packageName;
            info.version = pkgInfo.versionName;
            info.versionCode = pkgInfo.versionCode;
            info.icon = pm.getApplicationIcon(appInfo);
            return info;
        }
        return null;
    }

    public static boolean isAppInstalled(Context context, String filePath) {
        boolean installed = false;
        FileAPKInfo apkInfo = getAPKInfo(context, filePath);
        String packageName = null;
        String version = null;
        if (apkInfo != null) {
            packageName = apkInfo.packageName;
            version = apkInfo.version;
        }
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (packageInfo != null) {
            String versionName = packageInfo.versionName;
            if (versionName.equals(version)) {
                installed = true;
            }
        }
        return installed;
    }

    public static class FileAPKInfo {
        /**
         * 应用名称
         */
        public String appName;
        /**
         * 包名
         */
        public String packageName;
        /**
         * 版本信息
         */
        public String version;
        /**
         * 图标信息
         */
        public Drawable icon;
        /**
         * 版本信息号
         */
        public int versionCode;
    }

    public static String getSize(HashMap<String,String> otherArg){
        if (otherArg!=null&&!otherArg.isEmpty()){
            String size = otherArg.get(FileInfoUtil.SIZE);
            if (!TextUtils.isEmpty(size)){
                long tsize  = -1;
                try {
                    tsize = Long.valueOf(size);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (tsize>0){
                    return NumFormatUtil.bytes2kb(tsize);
                }
            }
        }
        return "";
    }

}

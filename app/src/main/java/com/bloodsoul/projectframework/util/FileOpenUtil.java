package com.bloodsoul.projectframework.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.bloodsoul.projectframework.common.Config;
import com.bloodsoul.projectframework.common.MainApplication;

import java.io.File;

public class FileOpenUtil {

    public static final String NOT_SUPPORTED_ACTION = "android.content.Intent.notsupported";

    public static Intent openFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists())
            return null;
        /* 取得扩展名 */
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length())
                .toLowerCase();
        /* 依扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf")
                || end.equals("ogg") || end.equals("wav") || end.equals("wma") || end.equals("aiff")) {
            return getAudioFileIntent(filePath);
        } else if (isVideo(end)) {
            return getVideoFileIntent(filePath);
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg")
                || end.equals("bmp")) {
            return getImageFileIntent(filePath);
        } else if (end.equals("apk")) {
            return getApkFileIntent(filePath);
        } else if (end.equals("ppt")) {
            return getPptFileIntent(filePath);
        } else if (end.equals("xls") || end.equals("xlsx")) {
            return getExcelFileIntent(filePath);
        } else if (end.equals("doc") || end.equals("docx")) {
            return getWordFileIntent(filePath);
        } else if (end.equals("pdf")) {
            return getPdfFileIntent(filePath);
        } else if (end.equals("chm")) {
            return getChmFileIntent(filePath);
        } else if (end.equals("txt")) {
            return getTextFileIntent(filePath, false);
        } else if (end.equals("crt") || end.equals("der")) {
            return getCAcertIntent(filePath);
        } else {
            return getOtherIntent(filePath);
        }
    }

    private static Intent getOtherIntent(String filePath) {
        return getAllIntent(filePath);
    }

    // 提示不支持打开文件
    private static Intent getNotSupportIntent() {
        Intent intent = new Intent();
        intent.setAction(NOT_SUPPORTED_ACTION);
        return intent;
    }

    private static boolean isVideo(String suffix) {
        return suffix.equals("3gp") || suffix.equals("3gpp") || suffix.equals("3g2") || suffix.equals("vob")
                || suffix.equals("wmv") || suffix.equals("qt") || suffix.equals("asx") || suffix.equals("wm")
                || suffix.equals("wvx") || suffix.equals("wmx") || suffix.equals("movie")
                || suffix.equals("mov") || suffix.equals("mng") || suffix.equals("mxu")
                || suffix.equals("fli") || suffix.equals("mp4") || suffix.equals("lsx")
                || suffix.equals("lsf") || suffix.equals("dl") || suffix.equals("rm") || suffix.equals("dv")
                || suffix.equals("mkv") || suffix.equals("avi") || suffix.equals("rmvb")
                || suffix.equals("flv") || suffix.equals("mpe") || suffix.equals("mpeg")
                || suffix.equals("mpg") || suffix.equals("acc") || suffix.equals("flac");
    }

    private static Intent getAllIntent(String param) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = getUri(param, intent);
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    // Android获取一个用于打开APK文件的intent
    private static Intent getApkFileIntent(String param) {
        Intent openInstallAppIntent = getOpenInstallAppIntent(param);
        if (openInstallAppIntent != null) {
            return openInstallAppIntent;
        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = getUri(param, intent);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    private static Intent getCAcertIntent(String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = getUri(filePath, intent);
        intent.setDataAndType(uri, "application/x-x509-user-cert");
        return intent;
    }

    private static Intent getOpenInstallAppIntent(String param) {
        Context context = MainApplication.getInstance().getApplicationContext();
        FileInfoUtil.FileAPKInfo apkInfo = FileInfoUtil.getAPKInfo(context, param);
        boolean appInstalled = FileInfoUtil.isAppInstalled(context, param);
        if (!appInstalled) {
            return null;
        }
        return context.getPackageManager().getLaunchIntentForPackage(apkInfo.packageName);
    }

    // Android获取一个用于打开VIDEO文件的intent
    private static Intent getVideoFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = getUri(param, intent);
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    // Android获取一个用于打开AUDIO文件的intent
    private static Intent getAudioFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = getUri(param, intent);
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    // Android获取一个用于打开Html文件的intent
    private static Intent getHtmlFileIntent(String param) {

        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider")
                .scheme("content").encodedPath(param).build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    // Android获取一个用于打开图片文件的intent
    private static Intent getImageFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = getUri(param, intent);
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    // Android获取一个用于打开PPT文件的intent
    private static Intent getPptFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = getUri(param, intent);
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    // Android获取一个用于打开Excel文件的intent
    private static Intent getExcelFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = getUri(param, intent);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    // Android获取一个用于打开Word文件的intent
    private static Intent getWordFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = getUri(param, intent);
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    // Android获取一个用于打开CHM文件的intent
    private static Intent getChmFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri =  getUri(param, intent);
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    // Android获取一个用于打开文本文件的intent
    private static Intent getTextFileIntent(String param, boolean paramBoolean) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean) {
            Uri uri1 = Uri.parse(param);
            intent.setDataAndType(uri1, "text/plain");
        } else {
            Uri uri2 = getUri(param, intent);
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }

    // Android获取一个用于打开PDF文件的intent
    private static Intent getPdfFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = getUri(param,intent);
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    private static Intent getGoToFileManagerIntent(String path) {
        Intent intent = new Intent();
        Uri uri = getUri(path, intent);
        intent.setData(uri);
        intent.putExtra("pkg", PlatformUtil.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    private static Uri getUri(String filePath, Intent intent) {
        Uri uri;
        if (AndroidUtil.isAboveSpecifiedVersion(AndroidUtil.N)) {
            addPrivacyReadUriPermission(intent);
            uri = FileProvider.getUriForFile(MainApplication.getInstance().getApplicationContext(), Config.FILE_PROVIDER_AUTHORITIES, new File(filePath));
        } else {
            uri = Uri.fromFile(new File(filePath));
        }
        return uri;
    }

    private static void addPrivacyReadUriPermission(Intent intent) {
        if (AndroidUtil.isAboveSpecifiedVersion(AndroidUtil.N)) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }
}

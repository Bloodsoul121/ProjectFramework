package com.bloodsoul.projectframework.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NumFormatUtil {

    private static final long MINUTE = 60 * 1000;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;
    private static final long MONTH = 30 * DAY;
    private static final long YEAR = 12 * MONTH;

    public static final int NUM1024 = 1024;
    public static final String KB = "KB";
    public static final String MB = "MB";

    /**
     *  以万为单位,保留小数点一位
     */
    public static String formatNum(int num) {
        if (num < 10000) {
            return String.valueOf(num);
        } else {
            return formatNumToWan(num) + "万";
        }
    }

    private static String formatNumToWan(double num) {
        DecimalFormat df = new DecimalFormat();
        String style = "0.0";
        df.applyPattern(style);
        return df.format(num);
    }

    /**
     *  将时间转换为多久之前
     */
    public static String formatNumToTime(long time) {
        long nowTime = System.currentTimeMillis();
        long subTime = nowTime - time;
        if (subTime < 2 * MINUTE) {
            return "刚刚";
        } else if (subTime < HOUR) {
            return (int)(subTime / MINUTE) + "分钟前";
        } else if (subTime < DAY) {
            return (int)(subTime / HOUR) + "小时前";
        } else {
            return getFormatTime(time);
        }
    }

    private static String getFormatTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日", Locale.getDefault());
        return sdf.format(new Date(time));
    }

    /**
     *  将文件字节数转换为kb单位
     */
    public static String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(NUM1024 * NUM1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP).floatValue();
        if (returnValue > 1)
            return (returnValue + MB);
        BigDecimal kilobyte = new BigDecimal(NUM1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP).floatValue();
        return (returnValue + "KB");
    }

}

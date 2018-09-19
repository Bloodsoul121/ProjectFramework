package com.wwlh.projectframework.util;

import android.util.Log;

public class Logger {

    private static final String TAG_LOGGER = "Logger";

    public static void v(String msg, Object... args)  {
        log("v", msg, args);
    }

    public static void d(String msg, Object... args)  {
        log("d", msg, args);
    }

    public static void i(String msg, Object... args)  {
        log("i", msg, args);
    }

    public static void w(String msg, Object... args)  {
        log("w", msg, args);
    }

    public static void e(String msg, Object... args)  {
        log("e", msg, args);
    }

    private static void log(String level, String msg, Object... args) {
        String logMsg = buildMsg(msg, args);
        switch (level) {
            case "v":
                Log.v(TAG_LOGGER, logMsg);
                break;
            case "d":
                Log.d(TAG_LOGGER, logMsg);
                break;
            case "i":
                Log.i(TAG_LOGGER, logMsg);
                break;
            case "w":
                Log.w(TAG_LOGGER, logMsg);
                break;
            case "e":
                Log.e(TAG_LOGGER, logMsg);
                break;
        }
    }

    private static String buildMsg(String msg, Object... args) {
        return msg;
    }

}

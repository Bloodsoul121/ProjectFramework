package com.wwlh.projectframework.common;

import android.app.Application;

public class MainApplication extends Application{

    private static MainApplication myApplication = null;

    public static MainApplication getInstance() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        init();
    }

    // 初始化配置
    private void init() {

    }
}

package com.wwlh.projectframework.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {

    private ExecutorService mExecutorService;

    private ThreadPoolManager() {
        mExecutorService = Executors.newCachedThreadPool();
    }

    public static ThreadPoolManager getInstance() {
        return Holder.sInstance;
    }

    private static class Holder {
        private static final ThreadPoolManager sInstance = new ThreadPoolManager();
    }

    public void execute(Runnable r) {
        if (mExecutorService == null) {
            Logger.e("ThreadPoolManager : mExecutorService == null !");
            return;
        }
        mExecutorService.execute(r);
    }

    public void executeSchedule(Runnable command, long initialDelay, long delay, TimeUnit unit) {

    }

    public void release() {
        if (mExecutorService != null && !mExecutorService.isShutdown()) {
            mExecutorService.shutdown();
            mExecutorService = null;
        }
    }

}

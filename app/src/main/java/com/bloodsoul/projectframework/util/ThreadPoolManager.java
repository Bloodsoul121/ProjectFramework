package com.bloodsoul.projectframework.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {

    private ExecutorService mExecutorService;

    private ScheduledThreadPoolExecutor mScheduledExecutorService;

    private ThreadPoolManager() {
        if (mExecutorService == null) {
            mExecutorService = Executors.newCachedThreadPool();
        }
        if (mScheduledExecutorService == null) {
            mScheduledExecutorService = new ScheduledThreadPoolExecutor(5);
        }
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

    public void schedule(Runnable r, long initialDelay, long delay, TimeUnit unit) {
        if (mScheduledExecutorService == null) {
            return;
        }
        mScheduledExecutorService.scheduleWithFixedDelay(r, initialDelay, delay, unit);
    }

    public void scheduleDelay(Runnable r, long delay, TimeUnit unit) {
        if (mScheduledExecutorService == null) {
            return;
        }
        mScheduledExecutorService.schedule(r, delay, unit);
    }

    public void release() {
        if (mExecutorService != null && !mExecutorService.isShutdown()) {
            mExecutorService.shutdown();
            mExecutorService = null;
        }
        if (mScheduledExecutorService != null && !mScheduledExecutorService.isShutdown()) {
            mScheduledExecutorService.shutdown();
            mScheduledExecutorService = null;
        }
    }

}

package com.yi.day7.concurrent;

import com.yi.day7.util.ThreadUtil;

import java.util.concurrent.ThreadPoolExecutor;

public class FutureTaskScheduler {
    static ThreadPoolExecutor mixPool = null;
    static {
        mixPool = ThreadUtil.getMixedTargetThreadPool();
    }
    private FutureTaskScheduler() {
    }

    public static void add(ExecuteTask task) {
        mixPool.execute(task::execute);
    }
}

package com.yi.day7.concurrent;

import com.google.common.util.concurrent.*;
import com.yi.day7.util.ThreadUtil;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class CallbackTaskScheduler {
    static ListeningExecutorService gPool = null;

    static {
        ExecutorService jPool = ThreadUtil.getMixedTargetThreadPool();
        gPool = MoreExecutors.listeningDecorator(jPool);
    }

    private CallbackTaskScheduler() {
    }

    public static <R> void add(CallbackTask<R> task) {
        ListenableFuture<R> future = gPool.submit(task::execute);
        Futures.addCallback(future, new FutureCallback<R>() {
            @Override
            public void onSuccess(@Nullable R result) {
                task.onBack(result);
            }

            @Override
            public void onFailure(Throwable t) {
                task.onException(t);
            }
        }, gPool);
    }
}

package com.yi.day7.concurrent;

public interface CallbackTask<R> {
    R execute();
    void onBack(R r);
    void onException(Throwable t);
}

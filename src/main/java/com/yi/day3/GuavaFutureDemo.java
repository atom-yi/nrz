package com.yi.day3;

import com.google.common.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class GuavaFutureDemo {
    private static final int SLEEP_GAP = 1000;
    static class HotWaterJob implements Callable<Boolean> {
        @Override
        public Boolean call() {
            try {
                log.info("洗好水壶");
                log.info("灌上凉水");
                log.info("放在火上");
                TimeUnit.MILLISECONDS.sleep(SLEEP_GAP);
                log.info("水开了");
            } catch (InterruptedException ex) {
                log.error("异常中断");
            }
            log.info("运行结束");
            return true;
        }
    }

    static class WashJob implements Callable<Boolean> {
        @Override
        public Boolean call() {
            try {
                log.info("洗茶壶");
                log.info("洗茶杯");
                log.info("拿茶叶");
                TimeUnit.MILLISECONDS.sleep(SLEEP_GAP);
                log.info("洗完了");
            } catch (InterruptedException ex) {
                log.error("异常中断");
            }
            log.info("运行结束");
            return true;
        }
    }
    
    static class MainJob implements Runnable {
        boolean waterOk = false;
        boolean cupOk = false;
        int gap = SLEEP_GAP / 10;
        @Override
        public void run() {
            while (true) {
                try {
                    log.info("读书中");
                    TimeUnit.MILLISECONDS.sleep(gap);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (waterOk && cupOk) {
                    drinkTea(waterOk, cupOk);
                    break;
                }
            }
        }

        static void drinkTea(boolean waterOk, boolean cpuOk) {
            if (waterOk && cpuOk) {
                log.info("泡茶喝");
            } else if (!cpuOk) {
                log.info("茶具没洗好");
            } else {
                log.info("水没烧好");
            }
        }
    }

    public static void main(String[] args) {
        MainJob mainJob = new MainJob();
        Thread mainThread = new Thread(mainJob);
        mainThread.setName("主线程");
        mainThread.start();

        Callable<Boolean> hotWaterJob = new HotWaterJob();
        Callable<Boolean> washJob = new WashJob();

        ExecutorService jpool = Executors.newFixedThreadPool(4);
        ListeningExecutorService gpool = MoreExecutors.listeningDecorator(jpool);
        ListenableFuture<Boolean> hotFuture = gpool.submit(hotWaterJob);
        Futures.addCallback(hotFuture, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    mainJob.waterOk = true;
                }
            }

            @Override
            public void onFailure(Throwable t) {
                log.info("烧水失败，没有茶喝了");
            }
        }, gpool);
        ListenableFuture<Boolean> washFuture = gpool.submit(washJob);
        Futures.addCallback(washFuture, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(@Nullable Boolean result) {
                if (result) {
                    mainJob.cupOk = true;
                }
            }

            @Override
            public void onFailure(Throwable t) {
                log.info("茶具没洗好，喝不了茶了");
            }
        }, gpool);
    }

}

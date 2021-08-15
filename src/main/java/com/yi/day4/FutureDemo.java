package com.yi.day4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class FutureDemo {
    private static final int SLEEP_GAP = 1000;
    static class HotWaterCallable implements Callable<Boolean> {
        @Override
        public Boolean call() {
            try {
                TimeUnit.MILLISECONDS.sleep(SLEEP_GAP);
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

    static class WashCallable implements Callable<Boolean> {
        @Override
        public Boolean call() {
            try {
                TimeUnit.MILLISECONDS.sleep(SLEEP_GAP);
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

    static void drinkTea(boolean waterOk, boolean cpuOk) {
        if (waterOk && cpuOk) {
            log.info("泡茶喝");
        } else if (!cpuOk) {
            log.info("茶具没洗好");
        } else {
            log.info("水没烧好");
        }
    }

    public static void main(String[] args) {
        Thread.currentThread().setName("主线程");
        FutureTask<Boolean> hTask = new FutureTask<>(new HotWaterCallable());
        Thread hThread = new Thread(hTask);

        FutureTask<Boolean> wTask = new FutureTask<>(new WashCallable());
        Thread wThread = new Thread(wTask);

        hThread.start();
        wThread.start();

        try {
            Boolean waterOk = hTask.get();
            Boolean cupOk = wTask.get();
            drinkTea(waterOk, cupOk);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        log.info("结束");
    }

}

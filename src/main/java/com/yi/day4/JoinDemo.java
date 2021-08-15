package com.yi.day4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class JoinDemo {
    private static final int SLEEP_GAP = 1000;
    static class HotWaterThread extends Thread {
        public HotWaterThread() {
            super("**烧水-Thread");
        }
        @Override
        public void run() {
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
        }
    }

    static class WashThread extends Thread {
        public WashThread() {
            super("$$清洗-Thread");
        }
        @Override
        public void run() {
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
        }
    }

    public static void main(String[] args) {
        Thread.currentThread().setName("主线程");
        Thread hThread = new HotWaterThread();
        Thread wThread = new WashThread();
        hThread.start();
        wThread.start();

        try {
            log.info("加入 join");
            hThread.join();
            log.info("??????");
            wThread.join();
            log.info("泡茶喝");
        } catch (InterruptedException e) {
            log.info("异常中断");
        }
        log.info("运行结束");
    }
}

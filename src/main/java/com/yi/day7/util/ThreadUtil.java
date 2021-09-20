package com.yi.day7.util;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class ThreadUtil {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private static class CustomThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String threadTag;

        public CustomThreadFactory(String threadTag) {
            SecurityManager sm = System.getSecurityManager();
            group = (sm != null) ? sm.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.threadTag = "app-pool-" + poolNumber.getAndIncrement() + "-" + threadTag + "-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(group, r, threadTag + threadNumber.getAndIncrement());
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }
            if (thread.getPriority() != Thread.NORM_PRIORITY) {
                thread.setPriority(Thread.NORM_PRIORITY);
            }
            return thread;
        }
    }

    private static final int KEEP_ALIVE_SECONDS = 30;
    private static final int QUEUE_SIZE = 10000;
    private static final int CORE_POOL_SIZE = 0;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT;
    private static final int IO_MAX = Math.max(2, CPU_COUNT * 2);
    private static final int IO_CORE = 0;
    private static final int MIXED_CORE = 0;
    private static final int MIXED_MAX = 128;
    private static final String MIXED_THREAD_AMOUNT = "mixed.thread.amount";

    /**
     * 获取混合型任务线程池
     *
     * @return 混合型任务线程池
     */
    public static ThreadPoolExecutor getMixedTargetThreadPool() {
        return MixedTargetThreadPoolLazyHolder.EXECUTOR;
    }

    /**
     * 获取 IO 密集型任务线程池
     *
     * @return IO 密集型任务线程池
     */
    public static ThreadPoolExecutor getIOIntenseTargetThreadPool() {
        return IOIntenseTargetThreadPoolLazyHolder.EXECUTOR;
    }

    /**
     * 获取 CPU 密集型型任务线程池
     *
     * @return CPU 密集型任务线程池
     */
    public static ThreadPoolExecutor getCpuIntenseTargetThreadPool() {
        return CpuIntenseTargetThreadPoolLazyHolder.EXECUTOR;
    }

    /**
     * 获取顺序执行或定时执行任务的线程池
     *
     * @return 顺序执行或定时执行任务的线程池
     */
    public static ScheduledThreadPoolExecutor getScheduledThreadPool() {
        return SeqOrScheduledTargetThreadPoolLazyHolder.EXECUTOR;
    }

    /**
     * 顺序执行
     *
     * @param task 任务
     */
    public static void seqExecutor(Runnable task) {
        getScheduledThreadPool().execute(task);
    }

    /**
     * 延迟执行
     *
     * @param task 任务
     * @param time 延迟时间
     * @param unit 时间单位
     */
    public static void delayRun(Runnable task, int time, TimeUnit unit) {
        getScheduledThreadPool().schedule(task, time, unit);
    }

    /**
     * 固定频率执行
     *
     * @param task 任务
     * @param time 间隔时间
     * @param unit 时间单位
     */
    public static void scheduleAtFixedRate(Runnable task, int time, TimeUnit unit) {
        getScheduledThreadPool().scheduleAtFixedRate(task, time, time, unit);
    }

    /**
     * 线程睡眠
     *
     * @param seconds 睡眠时间：秒
     */
    public static void sleepSeconds(int seconds) {
        LockSupport.parkNanos(seconds * 1000L * 1000L * 1000L);
    }

    /**
     * 获取当前线程名称
     *
     * @return 当前线程名称
     */
    public static String getCurThreadName() {
        return Thread.currentThread().getName();
    }

    /**
     * 获取当前线程 ID
     *
     * @return 当前线程 ID
     */
    public static long getCurThreadId() {
        return Thread.currentThread().getId();
    }

    /**
     * 获取当前线程
     *
     * @return 当前线程
     */
    public static Thread getCurThread() {
        return Thread.currentThread();
    }

    /**
     * 获取调用栈中的类名
     *
     * @param level 层级 1 - 当前方法，2 - 当前方法的上一级防范，n - 第 n 层级方法
     * @return 当前栈指定层级的类名
     */
    public static String stackClassName(int level) {
        return Thread.currentThread().getStackTrace()[level].getClassName();
    }

    /**
     * 获取调用栈中的方法名
     *
     * @param level 层级 1 - 当前方法，2 - 当前方法的上一级防范，n - 第 n 层级方法
     * @return 当前栈指定层级的方法名
     */
    public static String stackMethodName(int level) {
        return Thread.currentThread().getStackTrace()[level].getMethodName();
    }

    // 懒汉式单例创建线程池：用于CPU密集型任务
    private static class CpuIntenseTargetThreadPoolLazyHolder {
        private static final ThreadPoolExecutor EXECUTOR =
                new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS,
                        TimeUnit.SECONDS, new LinkedBlockingQueue<>(QUEUE_SIZE),
                        new CustomThreadFactory("cpu"));

        static {
            setThreadPoolShutdownHook("CPU密集型任务线程池", EXECUTOR);
        }
    }

    // 懒汉式单例创建线程池：用于IO密集型任务
    private static class IOIntenseTargetThreadPoolLazyHolder {
        private static final ThreadPoolExecutor EXECUTOR =
                new ThreadPoolExecutor(IO_CORE, IO_MAX, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(QUEUE_SIZE),
                        new CustomThreadFactory("io"));

        static {
            setThreadPoolShutdownHook("IO密集型任务线程池", EXECUTOR);
        }
    }

    // 懒汉式单例创建线程池：用于混合型任务
    private static class MixedTargetThreadPoolLazyHolder {
        private static final int max = (System.getProperty(MIXED_THREAD_AMOUNT) == null) ?
                MIXED_MAX : Integer.parseInt(System.getProperty(MIXED_THREAD_AMOUNT));
        private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
                MIXED_CORE, max, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(QUEUE_SIZE),
                new CustomThreadFactory("mixed")
        );

        static {
            setThreadPoolShutdownHook("混合型任务线程池", EXECUTOR);
        }
    }

    // 懒汉式单例创建线程池：用于定时任务，顺序执行排队任务
    static class SeqOrScheduledTargetThreadPoolLazyHolder {
        private static final ScheduledThreadPoolExecutor EXECUTOR
                = new ScheduledThreadPoolExecutor(1, new CustomThreadFactory("seq"));

        static {
            setThreadPoolShutdownHook("定时和顺序任务线程池", EXECUTOR);
        }
    }

    private static void setThreadPoolShutdownHook(String threadPoolName, ThreadPoolExecutor executor) {
        executor.allowCoreThreadTimeOut(true);
        Runtime.getRuntime().addShutdownHook(new ShutdownHookThread<Void>(threadPoolName,
                () -> {
                    shutdownThreadPoolGracefully(executor);
                    return null;
                }));
    }

    private static void shutdownThreadPoolGracefully(ExecutorService executor) {
        if (executor.isTerminated()) {
            return;
        }

        // 停止接收新任务
        try {
            executor.shutdown();
        } catch (SecurityException | NullPointerException e) {
            return;
        }

        try {
            // 等待60s，等待线程池中的任务执行完成
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                // 如果还未结束，再等60s等待任务执行结束
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.out.println("线程池任务未正常执行结束");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        // 如果线程池仍未关闭，循环关闭1000次，每次等待 10 ms
        if (!executor.isTerminated()) {
            try {
                for (int i = 0; i < 1000; i++) {
                    if (executor.awaitTermination(10, TimeUnit.MILLISECONDS)) {
                        break;
                    }
                    executor.shutdownNow();
                }
            } catch (Throwable e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static class ShutdownHookThread<R> extends Thread {
        private volatile boolean hasShutdown = false;
        private static final AtomicInteger shutdownTimes = new AtomicInteger(0);
        private final Callable<R> callback;

        public ShutdownHookThread(String name, Callable<R> callback) {
            super("JVM 退出钩子(" + name + ")");
            this.callback = callback;
        }

        @Override
        public void run() {
            synchronized (this) {
                System.out.println(getName() + " starting...");
                if (!hasShutdown) {
                    this.hasShutdown = true;
                    shutdownTimes.incrementAndGet();
                    long beginTime = System.currentTimeMillis();
                    try {
                        this.callback.call();
                    } catch (Exception e) {
                        System.out.println(getName() + " error: " + e.getMessage());
                    }
                    long consumingTimeTotal = System.currentTimeMillis() - beginTime;
                    System.out.println(getName() + " 耗时(ms): " + consumingTimeTotal);
                    System.out.println("关闭" + shutdownTimes.get() + "次");
                }
            }
        }
    }
}

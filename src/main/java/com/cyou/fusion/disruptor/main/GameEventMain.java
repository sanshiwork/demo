package com.cyou.fusion.disruptor.main;

import com.cyou.fusion.disruptor.GameEventEngine;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * GameEventMain
 * <p>
 * Created by zhanglei_js on 2018/2/5.
 */
public class GameEventMain {

    // 异步任务并发数
    private static final int POOL_SIZE = 3;

    // 环形队列大小数
    private static final int BUFFER_SIZE = 1024;

    // 异步任务回调个数
    private static final int CALLBACK_SIZE = 10;

    public static void main(String[] args) throws Exception {

        // 实例化一个异步引擎对象并启动
        GameEventEngine engine = new GameEventEngine(BUFFER_SIZE, POOL_SIZE);
        engine.start();

        // 模拟游戏Tick线程（间隔100毫秒）
        ScheduledExecutorService tick = Executors.newScheduledThreadPool(1, r -> {
            Thread thread = new Thread(r);
            thread.setName("Game-Main-Tick-Thread");
            return thread;
        });
        tick.scheduleAtFixedRate(() -> {

            // 每个Tick产生一个异步事件
            engine.publish(params -> {
                try {
                    Process process = Runtime.getRuntime().exec("dir");
                    process.waitFor();
                    return new Object[]{process.getInputStream()};
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }, results -> {

            }, System.currentTimeMillis());

            // 每个Tick处理10个异步回调
            engine.callback(CALLBACK_SIZE);


        }, 0, 100, TimeUnit.MILLISECONDS);

        // 模拟5秒后停服
        TimeUnit.SECONDS.sleep(5);

        // 停服处理
        tick.shutdown();
        engine.stop();
    }
}




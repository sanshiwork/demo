package com.cyou.fusion.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 异步事件引擎
 * <p>
 * Created by zhanglei_js on 2018/2/7.
 */
public class GameEventEngine {

    /**
     * 异步任务线程标识
     */
    private static final AtomicInteger GAME_THREAD_INDEX = new AtomicInteger(0);

    /**
     * disruptor对象
     */
    private final Disruptor<GameEvent> disruptor;

    /**
     * 回调结果
     */
    private final Queue<GameEventResult> results;

    /**
     * 构造方法
     *
     * @param buffer 队列大小
     * @param pool   任务池大小
     */
    public GameEventEngine(int buffer, int pool) {
        disruptor = new Disruptor<>(GameEvent::new, buffer, r -> {
            Thread thread = new Thread(r);
            thread.setName("Game-Event-Engine-Thread-" + GAME_THREAD_INDEX.getAndAdd(1));
            return thread;
        }, ProducerType.SINGLE, new YieldingWaitStrategy());
        results = new ConcurrentLinkedQueue<>();

        GameEventHandler[] handlers = new GameEventHandler[pool];
        for (int i = 0; i < pool; i++) {
            handlers[i] = new GameEventHandler(results);
        }
        disruptor.handleEventsWithWorkerPool(handlers);
    }

    /**
     * 开始
     */
    public void start() {
        disruptor.start();
    }

    /**
     * TODO 发布事件
     */
    public void publish(Execute execute, Callback callback, Object... args) {
        RingBuffer<GameEvent> ringBuffer = disruptor.getRingBuffer();
        ringBuffer.publishEvent((event, sequence, e, c, a) -> {
            event.setExecute(e);
            event.setCallback(c);
            event.setArgs(a);
        }, execute, callback, args);
    }

    /**
     * 处理回调
     */
    public void callback(int size) {
        // 回调异步任务结果
        for (int i = 0; i < size; i++) {
            GameEventResult result = results.poll();
            if (result == null) {
                break;
            } else {
                result.getCallback().callback(result.getArgs());
            }
        }
    }

    /**
     * 关闭
     */
    public void stop() {
        disruptor.shutdown();
    }
}




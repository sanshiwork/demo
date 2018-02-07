package com.cyou.fusion.disruptor;

import com.lmax.disruptor.WorkHandler;

import java.util.Queue;

/**
 * GameEventHandler
 * <p>
 * Created by zhanglei_js on 2018/2/7.
 */
public class GameEventHandler implements WorkHandler<GameEvent> {

    private Queue<GameEventResult> results;

    public GameEventHandler(Queue<GameEventResult> results) {
        this.results = results;
    }

    @Override
    public void onEvent(GameEvent event) throws Exception {
        // 耗时操作，返回结果写入处理线程线程
        Object[] result = event.getExecute().execute(event.getArgs());
        if (event.getCallback() != null) {
            results.add(new GameEventResult(result, event.getCallback()));
        }
    }
}

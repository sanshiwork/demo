package com.cyou.fusion.disruptor;

import java.util.Arrays;

/**
 * 异步结果
 * <p>
 * Created by zhanglei_js on 2018/2/7.
 */
public class GameEventResult {

    // 参数
    private Object[] args;

    // 回调
    private Callback callback;

    public GameEventResult(Object[] args, Callback callback) {
        this.args = args;
        this.callback = callback;
    }

    public Object[] getArgs() {
        return args;
    }

    public Callback getCallback() {
        return callback;
    }

    @Override
    public String toString() {
        return "GameEventResult{" +
                "args=" + Arrays.toString(args) +
                ", callback=" + callback +
                '}';
    }
}



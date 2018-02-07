package com.cyou.fusion.disruptor;

import java.util.Arrays;

/**
 * 异步事件
 * <p>
 * Created by zhanglei_js on 2018/2/7.
 */
public class GameEvent {

    /**
     * 异步参数
     */
    private Object[] args;

    /**
     * 异步逻辑
     */
    private Execute execute;

    /**
     * 异步回调
     */
    private Callback callback;

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Execute getExecute() {
        return execute;
    }

    public void setExecute(Execute execute) {
        this.execute = execute;
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public String toString() {
        return "GameEvent{" +
                "args=" + Arrays.toString(args) +
                ", execute=" + execute +
                ", callback=" + callback +
                '}';
    }
}

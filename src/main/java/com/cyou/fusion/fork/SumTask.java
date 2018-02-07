package com.cyou.fusion.fork;

import java.util.concurrent.RecursiveTask;

/**
 * SumTask
 * <p>
 * Created by zhanglei_js on 2018/2/7.
 */
class SumTask extends RecursiveTask<Integer> {

    private int from = 100;

    private int to = 200;

    public SumTask(int from, int to) {
        this.from = from;
        this.to = to;
    }

    /**
     * The main computation performed by this task.
     *
     * @return the result of the computation
     */
    @Override
    protected Integer compute() {
        if (from == to) {
            return from;
        } else if (this.to - this.from == 1) {
            return this.to + this.from;
        } else {
            int middle = (to + from) / 2;
            SumTask leftTask = new SumTask(from, middle);
            SumTask rightTask = new SumTask(middle + 1, to);
            this.invokeAll(leftTask, rightTask);
            return leftTask.join() + rightTask.join();
        }
    }
}
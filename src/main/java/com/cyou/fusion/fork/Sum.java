package com.cyou.fusion.fork;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * Sum
 * <p>
 * Created by zhanglei_js on 2018/1/29.
 */
public class Sum {

    public static void main(String[] args) {
        int from = 0;
        int to = 65535;

        long point1 = System.currentTimeMillis();
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<Integer> task = new SumTask(from, to);
        pool.submit(task);
        System.out.println("ForkJoin result:" + task.join());
        long point2 = System.currentTimeMillis();
        int sum = 0;
        for (int i = from; i <= to; i++) {
            sum += i;
        }
        System.out.println("Loop result:" + sum);
        long point3 = System.currentTimeMillis();

        System.out.println("ForkJoin time:" + (point2 - point1));
        System.out.println("Loop time:" + (point3 - point2));

    }
}

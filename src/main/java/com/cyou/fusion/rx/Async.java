package com.cyou.fusion.rx;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Async
 * <p>
 * Created by zhanglei_js on 2018/1/10.
 */
public class Async {

    private static void base() {
        Observable<String> observable = Observable.create(observableEmitter -> {
            observableEmitter.onNext("1");
            observableEmitter.onNext("2");
            observableEmitter.onNext("3");
            observableEmitter.onComplete();
        });


        Observer<String> observer = new Observer<String>() {

            @Override
            public void onSubscribe(Disposable disposable) {
                System.out.println("onSubscribe");
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext");
                System.out.println(s);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError");
                throwable.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };

        observable.subscribe(observer);
    }

    private static void simple() throws InterruptedException {

        // 数组
        // Observable.just("just1", "just2").subscribe(System.out::println);

        // 集合
        // String[] froms = new String[]{"from1", "from2", "from3"};
        // Observable.fromArray(froms).subscribe(System.out::println);

        // 延迟创建
        // Observable.defer(() -> Observable.fromIterable(Arrays.asList("defer1", "defer2", "defer3"))).subscribe(System.out::println);

        // 间隔一段时间创建
        Observable.interval(1, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(System.out::println);
        TimeUnit.SECONDS.sleep(10);

        // 特定范围
        // Observable.range(1, 20).subscribe(System.out::println);

        // 延迟一定时间
        // Observable.timer(2, TimeUnit.SECONDS).subscribe(System.out::println);
        // TimeUnit.SECONDS.sleep(10);

        // 重复
        // Observable.just("repeat").repeat(10).subscribe(System.out::println);
    }

    private static void option() throws InterruptedException {

        Observable.just("S1", "S2", "S3")
                .map(s -> s + ":" + s)
                .flatMap(s -> Observable.fromArray(s.split(":")))
                .filter(s -> !s.endsWith("1"))
                .take(2)
                .doOnNext(s -> System.out.println(s + ":" + System.currentTimeMillis()))
                .subscribe(System.out::println);

    }

    private static void scheduler() throws InterruptedException {

        ExecutorService service = Executors.newFixedThreadPool(2);

        Disposable disposable = Observable.create((ObservableOnSubscribe<String>) observableEmitter -> {
            System.out.println("Observable -->" + Thread.currentThread().getName());
            observableEmitter.onNext("L1");
            observableEmitter.onNext("L2");
            observableEmitter.onNext("L3");
        })
                .subscribeOn(Schedulers.from(service))
                .observeOn(Schedulers.from(service))
                .subscribe(s -> System.out.println("Observer(" + s + ") -->" + Thread.currentThread().getName()));

        TimeUnit.SECONDS.sleep(2);

        disposable.dispose();
        service.shutdown();
    }

    private static void backPressure() throws InterruptedException {

        final Subscription[] sub = new Subscription[1];

        Flowable<String> flowable = Flowable.create(emitter -> {
            for (int i = 0; i < 10000; i++) {
                emitter.onNext(String.valueOf(i));
            }
            emitter.onComplete();
        }, BackpressureStrategy.LATEST);


        Subscriber<String> subscriber = new Subscriber<String>() {

            @Override
            public void onSubscribe(Subscription subscription) {
                sub[0] = subscription;
                System.out.println("onSubscribe");
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext");
                System.out.println(s);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError");
                throwable.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };

        flowable.onBackpressureBuffer().subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(subscriber);
        sub[0].request(50);
        sub[0].request(50);
        sub[0].request(50);
        TimeUnit.SECONDS.sleep(2);

    }

    public static void main(String[] args) throws InterruptedException {
        // base();

        // simple();

        // option();

        // scheduler();

        backPressure();

    }
}

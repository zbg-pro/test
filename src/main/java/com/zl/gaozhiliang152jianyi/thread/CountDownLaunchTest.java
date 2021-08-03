package com.zl.gaozhiliang152jianyi.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class CountDownLaunchTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int num = 10;
        CountDownLatch begin = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(num);
        ExecutorService executorService = Executors.newCachedThreadPool();

        List<Future<Integer>> scores = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            scores.add(executorService.submit(new Runner(begin, end)));
        }

        begin.countDown();

        end.await();

        int count = 0;

        for (Future<Integer> future: scores) {
            count += future.get();
        }
        System.out.println("平均："+count/num);
    }
}

class Runner implements Callable<Integer> {
    private CountDownLatch begin;
    private CountDownLatch end;
    public Runner(CountDownLatch begin, CountDownLatch end){
        this.begin = begin;
        this.end = end;
    }


    @Override
    public Integer call() throws Exception {
        Integer score = new Random().nextInt(25);
        begin.await();
        TimeUnit.SECONDS.sleep(score);
        end.countDown();
        return score;
    }
}

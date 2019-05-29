package com.aca.sharedcounter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class SharedCounterTest {
    public static void main(String[] args) {
        SharedCounterTest test = new SharedCounterTest();
        test.test();
    }

    private void test() {
        List<Runnable> runnables = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            runnables.add(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 10; i++) {
                        SharedCounter.increment();
                    }
                }
            });
        }

        ExecutorService threadPool = Executors.newFixedThreadPool(runnables.size());
        try {
            for (Runnable runnable : runnables) {
                threadPool.submit(runnable);
            }

            threadPool.shutdown();
            threadPool.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        assert SharedCounter.getCounter().equals(100);
    }
}

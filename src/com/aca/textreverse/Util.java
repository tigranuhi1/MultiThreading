package com.aca.textreverse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

class Util {
    private static final Integer MAX_THREAD_COUNT = 20;

    static String reverseText(String text) {
        Integer initialThreadCount = getThreadCount(text.length());
        List<Callable<String>> callables = getCallableList(text, initialThreadCount);
        ExecutorService threadPool = Executors.newFixedThreadPool(callables.size());
        try {
            List<Future<String>> futureValues = threadPool.invokeAll(callables);
            List<String> values = new ArrayList<>();
            for (Future<String> futureValue : futureValues) {
                try {
                    values.add(futureValue.get());
                } catch (ExecutionException ex) {
                    ex.printStackTrace();
                }
            }
            threadPool.shutdown();
            Collections.reverse(values);
            String result = values.toString();
            return result.substring(1, result.length() - 1);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        threadPool.shutdown();
        return null;


    }

    private static List<Callable<String>> getCallableList(String text, Integer threadCount) {
        List<Callable<String>> callables = new ArrayList<>();
        Integer chunkSize = getChunkSize(text.length(), threadCount);
        Integer from = 0;
        do {
            Integer to = from + (from + chunkSize <= text.length() ? chunkSize : text.length());
            final String subText = text.substring(from, to);
            callables.add(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return new StringBuilder(subText).reverse().toString();
                }
            });
            from += chunkSize;
        } while (from < text.length());
        return callables;
    }

    private static Integer getChunkSize(Integer length, Integer thread_count) {
        return length / thread_count;
    }

    private static Integer getThreadCount(Integer length) {
        Integer threadCount = length / 1000 + (length % 1000 > 0 ? 1 : 0);
        return threadCount < MAX_THREAD_COUNT ? threadCount : MAX_THREAD_COUNT;
    }
}

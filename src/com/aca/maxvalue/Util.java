package com.aca.maxvalue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class Util {
    private static final Integer MAX_THREAD_COUNT = 20;

    static Integer getMaxValueFromArray(List<Integer> array) {
        Integer initialThreadCount = getThreadCount(array.size());
        List<Callable<Integer>> callables = getCallableList(array, initialThreadCount);
        ExecutorService threadPool = Executors.newFixedThreadPool(callables.size());
        try {
            List<Future<Integer>> futureValues = threadPool.invokeAll(callables);
            List<Integer> values = new ArrayList<>();
            for (Future<Integer> futureValue : futureValues) {
                try {
                    values.add(futureValue.get());
                } catch (ExecutionException ex) {
                    ex.printStackTrace();
                }
            }
            threadPool.shutdown();
            return Collections.max(values);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        threadPool.shutdown();
        return null;
    }


    private static List<Callable<Integer>> getCallableList(List<Integer> array, Integer threadCount) {
        List<Callable<Integer>> callables = new ArrayList<>();
        Integer chunkSize = getChunkSize(array.size(), threadCount);
        Integer from = 0;
        do {
            Integer to = from + (from + chunkSize <= array.size() ? chunkSize : array.size());
            final List<Integer> arr = array.subList(from, to);
            callables.add(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return Collections.max(arr);
                }
            });
            from += chunkSize;
        } while (from < array.size());


//        for (int i = 0; i < threadCount; i++) {
//            Integer from = i * CHUNK_SIZE;
//            Integer to = from + (from + CHUNK_SIZE <= array.size() ? CHUNK_SIZE : array.size());
//            final List<Integer> arr = array.subList(from, to);
//            callables.add(new Callable<Integer>() {
//                @Override
//                public Integer call() throws Exception {
//                    return Collections.max(arr);
//                }
//            });
//        }
        return callables;
    }

    private static Integer getChunkSize(Integer length, Integer thread_count) {
        return length / thread_count;
    }

    private static Integer getThreadCount(Integer length) {
        Integer threadCount = length / 1000 + (length % 1000 > 0 ? 1 : 0);
        return threadCount < MAX_THREAD_COUNT ? threadCount : MAX_THREAD_COUNT;
    }

    public static List<Integer> getArrayList(Integer length) {
        System.out.println("Adding elements to array...");
        if (length <= 1000) {
            return new ElementAdder(new ArrayList<Integer>(), length).call();
        }
        Integer threadCount = getThreadCount(length);
        Integer chunkSize = getChunkSize(length, threadCount);
        List<ElementAdder> callables = new ArrayList<>();
        Integer i = 0;
        do {
            Integer size = length - i >= chunkSize ? chunkSize : length - i;
            callables.add(new ElementAdder(new ArrayList<Integer>(), size));
            i += chunkSize;
        } while (i < length);

        ExecutorService threadPool = Executors.newFixedThreadPool(callables.size());
        try {
            List<Future<List<Integer>>> futureLists = new ArrayList<>();
            for (Callable<List<Integer>> callable : callables) {
                futureLists.add(threadPool.submit(callable));
            }
            List<Integer> list = new ArrayList<>();
            for (Future<List<Integer>> futureList : futureLists) {
                try {
                    list.addAll(futureList.get());
                } catch (ExecutionException ex) {
                    ex.printStackTrace();
                }
            }
            threadPool.shutdown();
            return list;
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        threadPool.shutdown();
        return null;
    }


}

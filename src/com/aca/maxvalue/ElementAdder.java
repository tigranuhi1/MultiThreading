package com.aca.maxvalue;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

public class ElementAdder implements Callable<List<Integer>> {
    List<Integer> list;
    Integer size;

    public ElementAdder(List<Integer> list, Integer size) {
        this.list = list;
        this.size = size;
    }

    @Override
    public List<Integer> call() {
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            list.add(random.nextInt());
            if(i%100==0){
                System.out.println("Thread " + Thread.currentThread().getName() + " adding " + i);
            }
        }
        System.out.println("Thread " + Thread.currentThread().getName() + " finished ");
        return list;
    }
}

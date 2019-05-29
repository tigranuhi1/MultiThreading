package com.aca.sharedcounter;

class SharedCounter {
    private static final Boolean lock = false;
    private static Integer counter = 0;

    static void increment() {
        synchronized (lock) {
            counter++;
        }
    }

    static Integer getCounter() {
        synchronized (lock) {
            return counter;
        }
    }
}

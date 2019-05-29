package com.aca.maxvalue;

import java.util.ArrayList;
import java.util.List;

public class MaxValueTest {
    public static void main(String[] args) {
        MaxValueTest test = new MaxValueTest();
        test.test();
    }

    private void test() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(100);
        list.add(500);
        list.add(-1);
        list.add(0);
        list.add(24);
        list.add(25687);
        list.add(112345654);
        list.add(14);
        assert Util.getMaxValueFromArray(list).equals(112345654);
    }
}

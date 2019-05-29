package com.aca.maxvalue;

import java.util.List;

class Main {
    public static void main(String[] args) {
        List<Integer> array = Util.getArrayList(10000000);
        System.out.println(String.format("Max value of Array = %s", Util.getMaxValueFromArray(array)));
    }
}

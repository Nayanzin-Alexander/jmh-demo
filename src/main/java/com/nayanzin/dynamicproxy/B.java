package com.nayanzin.dynamicproxy;

public class B implements A {

    int number;

    @Override
    public int add(int c) {
        number += c;
        return number;
    }
}

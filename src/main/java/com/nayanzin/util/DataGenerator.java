package com.nayanzin.util;

import static java.util.stream.IntStream.iterate;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class DataGenerator {

    private final static Random rand = new Random(47);

    private DataGenerator() {}

    public static List<Integer> generateNumbers(int size) {
        return iterate(0, i -> i + 1)
            .limit(size)
            .mapToObj(i -> rand.nextInt())
            .collect(Collectors.toList());
    }
}

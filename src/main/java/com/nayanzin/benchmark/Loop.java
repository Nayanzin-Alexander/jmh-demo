package com.nayanzin.benchmark;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 3, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 3)
@Measurement(iterations = 3)
public class Loop {

    @Param("1000")
    private int n;

    private List<String> testData;

    public static void main(String... args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(Loop.class.getSimpleName())
            .forks(1)
            .build();

        new Runner(opt).run();
    }

    @Setup
    public void setup() {
        testData = createData();
    }

    @Benchmark
    public void loopFor(Blackhole bh) {
        for (int i = 0; i < testData.size(); i++) {
            String s = testData.get(i);
            bh.consume(s);
        }
    }

    @Benchmark
    public void loopWhile(Blackhole bh) {
        int i = 0;
        while (i < testData.size()) {
            String s = testData.get(i);
            bh.consume(s);
            i++;
        }
    }

    @Benchmark
    public void loopForEach(Blackhole bh) {
        for (String s : testData) {
            bh.consume(s);
        }
    }

    @Benchmark
    public void loopIterator(Blackhole bh) {
        Iterator<String> it = testData.iterator();
        while (it.hasNext()) {
            String s = it.next();
            bh.consume(s);
        }
    }

    private List<String> createData() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            data.add("Number : " + i);
        }
        return data;
    }
}

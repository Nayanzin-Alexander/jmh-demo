package com.nayanzin.benchmark;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 1)
@Measurement(iterations = 1)
public class VolatileWrite {

    @Param("0")
    int tokens;

    private int v;
    private volatile int vv;

    public static void main(String... args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(VolatileWrite.class.getSimpleName())
            .forks(1)
            .build();

        new Runner(opt).run();
    }

    @Benchmark
    public void baseline0() {
        Blackhole.consumeCPU(tokens);
    }

    @Benchmark
    public int baseline1() {
        Blackhole.consumeCPU(tokens);
        return 42;
    }

    @Benchmark
    public int incrPlain() {
        Blackhole.consumeCPU(tokens);
        return v++;
    }

    @Benchmark
    public int incVolatile() {
        Blackhole.consumeCPU(tokens);
        return vv++;
    }
}

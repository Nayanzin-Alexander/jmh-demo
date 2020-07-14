package com.nayanzin.benchmark;

import com.nayanzin.util.DataGenerator;
import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 1)
@Measurement(iterations = 1)
public class Stream {

    private final List<Integer> numbers = DataGenerator.generateNumbers(500_000);
    private final ForkJoinPool customThreadPool = new ForkJoinPool(50);


    public static void main(String... args) throws RunnerException {
        new Runner(new OptionsBuilder()
            .include(Stream.class.getSimpleName())
            .build()).run();
    }

    @Benchmark
    public OptionalDouble baseLine() {
        return numbers.stream()
            .mapToInt(Integer::intValue)
            .average();
    }

    @Benchmark
    public OptionalDouble parallelStream() {
        return numbers.stream()
            .parallel()
            .mapToInt(Integer::intValue)
            .average();
    }

    @Benchmark
    public OptionalDouble customThreadPoolParallelStream() throws ExecutionException, InterruptedException {
        return customThreadPool.submit(
            () -> numbers.stream()
                .parallel()
                .mapToInt(Integer::intValue)
                .average()
        ).get();
    }
}

package com.nayanzin.benchmark;

import com.nayanzin.dynamicproxy.A;
import com.nayanzin.dynamicproxy.AbcDynamicInvocationHandler;
import com.nayanzin.dynamicproxy.B;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 1)
@Measurement(iterations = 1)
public class JDKDynamicProxy {

    private int number = -100;

    private A notProxied = new B();
    private A proxied = (A) Proxy.newProxyInstance(
        JDKDynamicProxy.class.getClassLoader(),
        new Class[]{A.class},
        new AbcDynamicInvocationHandler(new B())
    );


    public static void main(String... args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(JDKDynamicProxy.class.getSimpleName())
            .forks(1)
            .build();
        new Runner(opt).run();
    }

    @Benchmark
    public int baseLine() {
        int n = Math.abs(number);
        return notProxied.add(n);
    }

    @Benchmark
    public long withJDKDynamicProxy() {
        return proxied.add(1);
    }
}

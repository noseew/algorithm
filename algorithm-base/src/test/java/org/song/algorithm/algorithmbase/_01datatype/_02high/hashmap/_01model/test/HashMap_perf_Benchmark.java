package org.song.algorithm.algorithmbase._01datatype._02high.hashmap._01model.test;

import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.song.algorithm.algorithmbase._01datatype._02high.hashmap._01model.HashMap_base_05;
import org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_base.test.SkipListMap_perf_Benchmark;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime) // 模式, 平均操作时间
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class HashMap_perf_Benchmark {
    int num = 1_0000;


    /*
    自己实现的红黑树HashMap VS JDK HashMap
    不严谨测试: HashMap 更快
    Benchmark        Mode  Cnt     Score     Error  Units
    HashMap          avgt    5  8286.686 ± 313.177  us/op
    HashMap_base_05  avgt    5  8761.041 ± 672.792  us/op
     */
    @Test
    public void test_perf_HashMap_vs_05() {
        Options build = new OptionsBuilder().include(HashMap_perf_Benchmark.class.getSimpleName())
                .forks(1)
                .measurementIterations(5) // 测量执行次数
                .warmupIterations(5) // 预热次数, 建议不超过5
                .build();
        try {
            new Runner(build).run();
        } catch (RunnerException e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void HashMap() {
        HashMap<String, String> map2 = new HashMap<>();
        for (int i = 0; i < num; i++) {
            String s = UUID.randomUUID().toString();
            map2.put(s, "");
            map2.get(s);
        }
    }

    @Benchmark
    public void HashMap_base_05() {
        HashMap_base_05<String, String> map2 = new HashMap_base_05<>();
        for (int i = 0; i < num; i++) {
            String s = UUID.randomUUID().toString();
            map2.put(s, "");
            map2.get(s);
        }
    }

}

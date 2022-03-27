package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_base.test;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree05RB01;
import org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_base.SkipListMapArray;
import org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_base.SkipListMapLinked;
import org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_redis.SkipListLinked01;
import org.song.algorithm.algorithmbase.utils.StopWatchUtils;
import org.springframework.util.StopWatch;

import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.AverageTime, Mode.Throughput}) // 模式, 平均操作时间
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class SkipListMap_perf_Benchmark {
    int maxVal = 100_0000;
    int num = 1_0000;

    private Random r = new Random();

    /*
    简单版跳表, 数组 VS 链表
    数组效率高2倍
    红黑树比跳表(数组)高约10倍, 跳表写的有问题?
    
    结果 put
    Benchmark             Mode  Cnt      Score     Error  Units
    SkipListMapArrayPut   avgt    5  24237.124 ± 427.691  us/op (单位每个操作耗时微秒)
    SkipListMapLinkedPut  avgt    5  50323.837 ± 771.523  us/op
    Tree05RB01Add         avgt    5   1861.837 ±  49.739  us/op
    
    结果 put + get
    Benchmark             Mode  Cnt      Score      Error  Units
    SkipListMapArrayPut   avgt    5  27168.461 ±  383.098  us/op
    SkipListMapLinkedPut  avgt    5  55106.277 ± 3542.265  us/op
    Tree05RB01Add         avgt    5   3459.509 ±  485.018  us/op
     */
    @Test
    public void test_perf_array_vs_link() {
        Options build = new OptionsBuilder().include(SkipListMap_perf_Benchmark.class.getSimpleName())
                .forks(1)
                .measurementIterations(3) // 测量执行次数
                .warmupIterations(3) // 预热次数, 建议不超过5
                .build();
        try {
            new Runner(build).run();
        } catch (RunnerException e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void SkipListMapArrayPut() {
        SkipListMapArray<Integer, Integer> skip2 = new SkipListMapArray<>();
        for (int i = 0; i < num; i++) {
            int key = r.nextInt(maxVal);
            skip2.put(key, 0);
            skip2.get(key);
        }
    }

    @Benchmark
    public void SkipListMapLinkedPut() {
        SkipListMapLinked<Integer, Integer> skip1 = new SkipListMapLinked<>();
        for (int i = 0; i < num; i++) {
            int key = r.nextInt(maxVal);
            skip1.put(key, 0);
            skip1.get(key);
        }
    }

    @Benchmark
    public void Tree05RB01Add() {
        Tree05RB01<Integer> rbTree1 = new Tree05RB01<>(Comparator.comparing(Integer::doubleValue));
        for (int i = 0; i < num; i++) {
            int key = r.nextInt(maxVal);
            rbTree1.add(key);
            rbTree1.get(key);
        }
    }
}

package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_base.test;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree05RB01;
import org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_base.SkipListMapArray02OptLevel;
import org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_base.SkipListMapLinked03OptLevel;
import org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_base.SkipListMapLinkedFromJDK;

import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SkipListMap_perf_Benchmark {

    /*
    第一次:
    Benchmark          Mode  Cnt       Score        Error  Units
    SkipListMapArray   avgt    5  526953.566 ± 187789.179  us/op
    SkipListMapLinked  avgt    5  550490.565 ± 201475.567  us/op
    Tree05RB01         avgt    5  205296.161 ± 138917.605  us/op
    
    第二次:
    Benchmark          Mode  Cnt       Score      Error  Units
    Tree05RB01         avgt    5   62545.689 ± 5586.528  us/op
    SkipListMapLinked  avgt    5  141903.320 ± 7064.124  us/op
    SkipListMapArray   avgt    5  143580.056 ± 9696.941  us/op
    SkipListMapJDK     avgt    5  173983.698 ± 9774.712  us/op

     */
    @Test
    public void test_perf() {
        Options build = new OptionsBuilder().include(SkipListIndexBenchmark.class.getSimpleName())
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

    @BenchmarkMode({Mode.AverageTime}) // 模式, 平均操作时间
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @State(Scope.Thread)
    public static class SkipListIndexBenchmark {
        int maxVal = 10000_0000;
        int num = 10_0000;

        private Random r = new Random();

        @Benchmark
        public void SkipListMapArray() {
            SkipListMapArray02OptLevel<Integer, Integer> skip2 = new SkipListMapArray02OptLevel<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip2.put(key, 0);
                skip2.get(key);
            }
        }

        @Benchmark
        public void Tree05RB01() {
            Tree05RB01<Integer> rbTree1 = new Tree05RB01<>(Comparator.comparing(Integer::doubleValue));
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                rbTree1.add(key);
                rbTree1.get(key);
            }
        }

        @Benchmark
        public void SkipListMapLinked() {
            SkipListMapLinked03OptLevel<Integer, Integer> skip1 = new SkipListMapLinked03OptLevel<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip1.put(key, 0);
                skip1.get(key);
            }
        }

        @Benchmark
        public void SkipListMapJDK() {
            SkipListMapLinkedFromJDK<Integer, Integer> jdk2 = new SkipListMapLinkedFromJDK<>(Comparator.comparing(Integer::doubleValue));
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                jdk2.put(key, 0);
                jdk2.get(key);
            }
        }
    }

}

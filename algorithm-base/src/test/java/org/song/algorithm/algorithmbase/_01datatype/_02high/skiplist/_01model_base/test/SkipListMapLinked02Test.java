package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_base.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_base.ConcurrentSkipListMap02;
import org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_base.SkipListMapLinked;
import org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_base.SkipListMapLinked02;

import java.util.Random;

public class SkipListMapLinked02Test {

    private int maxVal = 10000;
    private int maxSize = 500;

    private Random r = new Random();

    @Test
    public void test01() {

//        SkipListMapLinked02<Integer, Integer> skip1 = new SkipListMapLinked02<>();
        ConcurrentSkipListMap02<Integer, Integer> skip1 = new ConcurrentSkipListMap02<>();
        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            int key = r.nextInt(maxVal / 10);
            skip1.get(key);
            skip1.put(key, val);

            if (i >= maxSize - 5) {
                System.out.println(skip1);
                Integer v2 = skip1.remove(key);
                System.out.println(v2);
            }


        }
        System.out.println(skip1.toString());

    }

    @Test
    public void testAuto01() {

        int maxVal = 10_0000;
        int maxSize = 5000;

        for (int i = 0; i < 50; i++) {

//            SkipListMapLinked02<Integer, Integer> skip1 = new SkipListMapLinked02<>();
            ConcurrentSkipListMap02<Integer, Integer> skip1 = new ConcurrentSkipListMap02<>();
            for (int j = 0; j < maxSize; j++) {
                int key = r.nextInt(maxVal);
                int val = r.nextInt(maxVal);
                skip1.put(key, val);

                boolean equals = Integer.valueOf(val).equals(skip1.get(key));
                if (!equals) {
                    System.out.println(skip1);
                    skip1.get(key);
                    skip1.put(key, val);
                    skip1.get(key);
                    assert equals;
                }

                if (j % 10 == 0) {
                    skip1.remove(j);
                    equals = skip1.get(j) == null;
                    if (!equals) {
                        System.out.println(skip1);
                        assert equals;
                    }
                }
            }
            System.out.println("put get remove OK");

        }



    }
}

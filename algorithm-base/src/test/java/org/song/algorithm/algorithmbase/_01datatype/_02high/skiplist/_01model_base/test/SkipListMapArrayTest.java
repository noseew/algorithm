package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_base.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_base.SkipListMapArray;
import org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_base.SkipListMapArray02OptLevel;

import java.util.Random;

public class SkipListMapArrayTest {

    private int maxVal = 10000;
    private int maxSize = 500;

    private Random r = new Random();

    @Test
    public void test01() {

//        SkipListMapArray<Integer, Integer> skip1 = new SkipListMapArray<>();
        SkipListMapArray02OptLevel<Integer, Integer> skip1 = new SkipListMapArray02OptLevel<>();
        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            int key = r.nextInt(maxVal / 10);
            skip1.put(key, val);

            if (i >= maxSize - 5) {
                Integer v2 = skip1.remove(key);
                System.out.println(v2);
            }


        }
        System.out.println(skip1.toString());

    }

    /**
     * -XX:+UnlockCommercialFeatures IDEA查看运行火焰图
     */
    @Test
    public void testAuto01() {

        int maxVal = 100_0000;
        int maxSize = 50000;

        for (int i = 0; i < 50; i++) {

//            SkipListMapArray<Integer, Integer> skip1 = new SkipListMapArray<>();
            SkipListMapArray02OptLevel<Integer, Integer> skip1 = new SkipListMapArray02OptLevel<>();
            for (int j = 0; j < maxSize; j++) {
                int key = r.nextInt(maxVal);
                int val = r.nextInt(maxVal);
                skip1.put(key, val);

                boolean equals = Integer.valueOf(val).equals(skip1.get(key));
                if (!equals) {
                    System.out.println(skip1);
                    skip1.get(key);
                    assert equals;
                }

                if (j % 10 == 0) {
                    skip1.remove(key);
                    equals = skip1.get(key) == null;
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

package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.ArrayBase01;
import org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model.SkipListBase01;

import java.util.Random;

public class SkipListTest {

    private int maxVal = 1000;
    private int maxSize = 100;

    private Random r = new Random();
    
    @Test
    public void test01() {

        SkipListBase01<Integer, Integer> skip1 = new SkipListBase01<>();
        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            int key = r.nextInt(maxVal);
            int score = r.nextInt(maxVal / 50);
            skip1.put(key, val, score);
            System.out.println();
            System.out.println(skip1.toString());
            
        }
        System.out.println(skip1.toString());

        ArrayBase01<Integer> values = skip1.getByScore(-1, -1);
        System.out.println(values);

        values = skip1.getByScore(-1, 50);
        System.out.println(values);

        values = skip1.getByScore(50, -1);
        System.out.println(values);

    }
}

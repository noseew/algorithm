package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.ArrayBase01;
import org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model.SkipListBase01;

import java.util.Objects;
import java.util.Random;

public class SkipList01Test {

    private int maxVal = 1000;
    private int maxSize = 50;

    private Random r = new Random();
    
    @Test
    public void test01() {

        SkipListBase01<Integer, Integer> skip1 = new SkipListBase01<>();
        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            int key = r.nextInt(maxVal);
            int score = r.nextInt(maxVal / 5);
            skip1.put(key, val, score);
            System.out.println(skip1.toString());

            if (i >= maxSize - 5) {
                Integer v2 = skip1.remove(key);
                System.out.println(v2);
            }
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
    
    @Test
    public void testAuto01() {

        for (int i = 0; i < 10; i++) {

            SkipListBase01<Integer, Integer> skip1 = new SkipListBase01<>();
            for (int j = 0; j < maxSize; j++) {
                int key = r.nextInt(maxVal);
                int val = r.nextInt(maxVal);
                int score = r.nextInt(maxVal / 5);
                skip1.put(key, val, score);

                boolean equals = Integer.valueOf(val).equals(skip1.get(key));
                if (!equals) {
                    skip1.get(key);
                    assert equals;
                }
            }

            System.out.println("put get OK");

            ArrayBase01<SkipListBase01.Node<Integer, Integer>> byScore = skip1.getNodesByScore(-1, -1);
            for (int k = 0; k < byScore.length() - 1; k++) {
                boolean b = byScore.get(k).getScore() <= byScore.get(k + 1).getScore();
                if (!b) {
                    skip1.getNodesByScore(-1, -1);
                    byScore.get(k);
                    byScore.get(k + 1);
                    assert b;
                }
            }
            int max = maxVal / 10;
            byScore = skip1.getNodesByScore(-1, max);
            assert byScore.get(byScore.length() - 1).getScore() < max;
            int min = maxVal / 10;
            byScore = skip1.getNodesByScore(min, -1);
            assert byScore.get(0).getScore() >= min;


            System.out.println("Score OK");

            for (int j = 0; j < maxSize; j++) {
                int val = r.nextInt(maxVal);
                int key = r.nextInt(maxVal);
                int score = r.nextInt(maxVal / 5);
                skip1.put(key, val, score);
                skip1.remove(key);

                ArrayBase01<SkipListBase01.Node<Integer, Integer>> nodes = skip1.getNodesByScore(score, score + 1);
                boolean noHas = true;
                for (int k = 0; k < nodes.length(); k++) {
                    noHas = noHas && !nodes.get(k).getK().equals(key);
                }
                if (!noHas) {
                    assert noHas;
                }
            }

            System.out.println("remove OK");

            skip1.clean();
            double maxScore = 0;
            double minScore = maxVal;
            for (int j = 0; j < maxSize; j++) {
                int key = r.nextInt(maxVal);
                int val = r.nextInt(maxVal);
                int score = r.nextInt(maxVal / 5);
                maxScore = Math.max(maxScore, score);
                minScore = Math.min(minScore, score);
                
                skip1.put(key, val, score);

                ArrayBase01<Integer> maxList = skip1.getByScore(maxScore, maxScore + 1);
                boolean hasMax = false;
//                Integer maxVal = skip1.getMaxVal();
//                for (int k = 0; k < maxList.length(); k++) {
//                    hasMax = hasMax || Objects.equals(maxList.get(k), maxVal);
//                }
//                if (!hasMax) {
//                    skip1.getMaxVal();
//                    assert hasMax;
//                }

                ArrayBase01<Integer> minList = skip1.getByScore(-1, minScore + 1);
                boolean hasMin = false;
                Integer minVal = skip1.getMinVal();
                for (int k = 0; k < minList.length(); k++) {
                    hasMin = hasMin || Objects.equals(minList.get(k), minVal);
                }
                if (!hasMin) {
                    skip1.getMinVal();
                    assert hasMin;
                }
            }
            System.out.println("getMinVal getMaxVal OK");


            skip1.removeByScore(minScore, maxScore);
            System.out.println("removeByScore OK");
        }


    }
}

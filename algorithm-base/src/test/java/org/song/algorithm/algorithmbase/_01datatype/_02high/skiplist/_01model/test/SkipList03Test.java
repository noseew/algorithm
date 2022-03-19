package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.ArrayBase01;
import org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model.AbstractSkipList;
import org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model.SkipListBase03;

import java.util.Objects;
import java.util.Random;

public class SkipList03Test {

    private int maxVal = 1000;
    private int maxSize = 100;

    private Random r = new Random();

    @Test
    public void test01() {

        SkipListBase03<Integer, Integer> skip1 = new SkipListBase03<>();
        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            int key = r.nextInt(maxVal);
            int score = r.nextInt(maxVal);
            skip1.put(key, val, score);
            System.out.println(skip1.toString());

            if (i >= maxSize - 5) {
                AbstractSkipList.Node<Integer, Integer> v2 = skip1.remove(key);
                System.out.println(v2);
            }
            System.out.println(skip1.toString());


        }
        System.out.println(skip1.toString());

        ArrayBase01<AbstractSkipList.Node<Integer, Integer>> values = skip1.getByScore(-1, -1);
        System.out.println(values);

        values = skip1.getByScore(-1, 50);
        System.out.println(values);

        values = skip1.getByScore(50, -1);
        System.out.println(values);

    }

    @Test
    public void testAuto01() {

        for (int i = 0; i < 10; i++) {

            SkipListBase03<Integer, Integer> skip1 = new SkipListBase03<>();
            for (int j = 0; j < maxSize; j++) {
                int key = r.nextInt(maxVal);
                int val = r.nextInt(maxVal);
                int score = r.nextInt(maxVal / 5);
                skip1.put(key, val, score);

                boolean equals = Integer.valueOf(val).equals(skip1.get(key).getV());
                if (!equals) {
                    skip1.get(key);
                    assert equals;
                }
            }

            System.out.println("put get OK");

            ArrayBase01<SkipListBase03.Node<Integer, Integer>> byScore = skip1.getNodesByScore(-1, -1);
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

                ArrayBase01<SkipListBase03.Node<Integer, Integer>> nodes = skip1.getNodesByScore(score, score + 1);
                boolean noHas = true;
                for (int k = 0; k < nodes.length(); k++) {
                    noHas = noHas && !Objects.equals(nodes.get(k).getV(), val);
                }
                if (!noHas) {
                    skip1.remove(key);
                    skip1.getNodesByScore(score, score + 1);
                    assert noHas;
                }
            }

            System.out.println("remove OK");

            skip1.clean();
            for (int j = 0; j < maxSize; j++) {
                int key = r.nextInt(maxVal);
                int val = r.nextInt(maxVal);
                int score = r.nextInt(maxVal / 5);

                skip1.put(key, val, score);

                double maxScore = skip1.getMaxScore();
                ArrayBase01<AbstractSkipList.Node<Integer, Integer>> maxList = skip1.getByScore(maxScore, maxScore + 1);
                boolean hasMax = false;
                AbstractSkipList.Node<Integer, Integer> maxVal = skip1.getMaxNode();
                for (int k = 0; k < maxList.length(); k++) {
                    hasMax = hasMax || Objects.equals(maxList.get(k).getV(), maxVal.getV());
                }
                if (!hasMax) {
                    skip1.getMaxNode();
                    assert hasMax;
                }

                double minScore = skip1.getMinScore();
                ArrayBase01<AbstractSkipList.Node<Integer, Integer>> minList = skip1.getByScore(minScore, minScore + 2);
                boolean hasMin = false;
                AbstractSkipList.Node<Integer, Integer> minVal = skip1.getMinNode();
                for (int k = 0; k < minList.length(); k++) {
                    hasMin = hasMin || Objects.equals(minList.get(k).getV(), minVal.getV());
                }
                if (!hasMin) {
                    skip1.getMinNode();
                    skip1.getByScore(-1, minScore + 2);
                    assert hasMin;
                }
            }
            System.out.println("getMinVal getMaxVal OK");

        }


    }

    @Test
    public void testAuto01_rank() {
        SkipListBase03<Integer, Integer> skip1 = new SkipListBase03<>();

        for (int j = 0; j < maxSize; j++) {
            int key = r.nextInt(maxVal);
            int val = r.nextInt(maxVal);
            int score = r.nextInt(maxVal / 5);
            skip1.put(key, val, score);

            int rank = skip1.getKeyRank(key);
            AbstractSkipList.Node<Integer, Integer> node = skip1.getByRank(rank);
            if (node == null) {
                skip1.getByRank(rank);
                assert false;
            }
            boolean equals = Objects.equals(key, node.getK());
            if (!equals) {
                skip1.getByRank(rank);
                assert equals;
            }

        }
        System.out.println("Rank OK");
    }
}

package org.song.algorithm.algorithmbase._01datatype._02high.hashmap._01model.test;

import org.junit.Test;
import org.song.algorithm.algorithmbase._01datatype._02high.hashmap._01model.*;
import org.song.algorithm.algorithmbase._01datatype._02high.hashmap.redis.Dict_base_01;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class HashMap_test {

    private int maxVal = 50_0000;
    private int maxSize = 500;

    private Random r = new Random();

    @Test
    public void testHash() {
        // 相等
        System.out.println(System.identityHashCode(1));
        System.out.println(System.identityHashCode(1));
        // 不相等
        System.out.println(System.identityHashCode(129));
        System.out.println(System.identityHashCode(129));
    }

    @Test
    public void test_base() {
        
        /*
        System.identityHashCode() 函数有坑
        
         */
        for (int j = 0; j < 100; j++) {
//            HashMap_base_01<Integer, Integer> map1 = new HashMap_base_01<>();
//            HashMap_base_02<Integer, Integer> map1 = new HashMap_base_02<>();
//            HashMap_base_03<Integer, Integer> map1 = new HashMap_base_03<>();
//            HashMap_base_04<Integer, Integer> map1 = new HashMap_base_04<>();
            HashMap_openAddressing_01<Integer, Integer> map1 = new HashMap_openAddressing_01<>();
//            Dict_base_01<Integer, Integer> map1 = new Dict_base_01<>();
            for (int i = 0; i < maxSize; i++) {
                int k = r.nextInt(maxVal);
                int v = r.nextInt(maxVal);
                if (map1.put(k, v) == null) {
                    Integer v1 = map1.get(k);
                    boolean eq = eq(v, v1);
                    if (!eq) {
                        map1.get(k);
                        map1.put(k, v);
                        assert eq;
                    }
                }
            }

            System.out.println();

            for (int i = 0; i < maxSize; i++) {
                int k = r.nextInt(maxVal / 2);
                map1.remove(k);
            }
            for (int i = 0; i < maxSize; i++) {
                int k = r.nextInt(maxVal);
                int v = r.nextInt(maxVal);
                if (map1.put(k, v) == null) {
                    Integer v1 = map1.get(k);
                    boolean eq = eq(v, v1);
                    if (!eq) {
                        map1.get(k);
                        map1.put(k, v);
                        assert eq;
                    }
                }
            }
            
        }

        System.out.println("OK");
    }

    @Test
    public void test_auto01() {
        HashMap<Integer, Integer> map0 = new HashMap<>();
        HashMap_base_01<Integer, Integer> map1 = new HashMap_base_01<>();
        HashMap_base_02<Integer, Integer> map2 = new HashMap_base_02<>();
        HashMap_base_03<Integer, Integer> map3 = new HashMap_base_03<>();
        HashMap_base_04<Integer, Integer> map4 = new HashMap_base_04<>();
        Dict_base_01<Integer, Integer> map5 = new Dict_base_01<>();
        HashMap_openAddressing_01<Integer, Integer> map6 = new HashMap_openAddressing_01<>();
        HashMap_openAddressing_02<Integer, Integer> map7 = new HashMap_openAddressing_02<>();

        for (int i = 0; i < maxSize; i++) {
            int k = r.nextInt(maxVal);
            int v = r.nextInt(maxVal);

            map0.put(k, v);
            map1.put(k, v);
            map2.put(k, v);
            map3.put(k, v);
            map4.put(k, v);
            map5.put(k, v);
            map6.put(k, v);
            map7.put(k, v);
        }

        for (int i = 0; i < maxSize; i++) {
            Integer v0 = map0.get(i);
            Integer v1 = map1.get(i);
            Integer v2 = map2.get(i);
            Integer v3 = map3.get(i);
            Integer v4 = map4.get(i);
            Integer v5 = map5.get(i);
            Integer v6 = map6.get(i);
            Integer v7 = map7.get(i);
            assert eq(v0, v1);
            assert eq(v0, v2);
            assert eq(v0, v3);
            assert eq(v0, v4);
            assert eq(v0, v5);
            assert eq(v0, v6);
            assert eq(v0, v7);
        }

        for (int i = 0; i < maxSize; i++) {
            int v = r.nextInt(maxVal / 2);
            map0.remove(v);
            map1.remove(v);
            map2.remove(v);
            map3.remove(v);
            map4.remove(v);
            map5.remove(v);
            map6.remove(v);
            map7.remove(v);
        }

        for (int i = 0; i < maxSize; i++) {
            Integer v0 = map0.get(i);
            Integer v1 = map1.get(i);
            Integer v2 = map2.get(i);
            Integer v3 = map3.get(i);
            Integer v4 = map4.get(i);
            Integer v5 = map5.get(i);
            Integer v6 = map6.get(i);
            Integer v7 = map7.get(i);
            assert eq(v0, v1);
            assert eq(v0, v2);
            assert eq(v0, v3);
            assert eq(v0, v4);
            assert eq(v0, v5);
            assert eq(v0, v6);
            assert eq(v0, v7);
        }
    }

    private static boolean eq(Object o1, Object o2) {
        if (o1 == null  && o2 == null) {
            return true;
        }
        return Objects.equals(o1, o2);
    }
}


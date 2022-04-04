package org.song.algorithm.base._01datatype._02high.hashmap._01base.test;

import org.junit.Test;
import org.song.algorithm.base._01datatype._02high.hashmap._01base.*;
import org.song.algorithm.base._01datatype._02high.hashmap._02redis.Dict_base_01;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class HashMap_test {

    private int maxVal = 50000;
    private int maxSize = 5000;

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
            HashMap_base_05<Integer, Integer> map1 = new HashMap_base_05<>();
//            HashMap_openAddressing_01<Integer, Integer> map1 = new HashMap_openAddressing_01<>();
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
        HashMap_base_05<Integer, Integer> map5 = new HashMap_base_05<>();
        Dict_base_01<Integer, Integer> dictMap1 = new Dict_base_01<>();

        for (int i = 0; i < maxSize; i++) {
            int k = r.nextInt(maxVal);
            int v = r.nextInt(maxVal);

            map0.put(k, v);
            map1.put(k, v);
            map2.put(k, v);
            map3.put(k, v);
            map4.put(k, v);
            map5.put(k, v);
            dictMap1.put(k, v);


            Integer v0 = map0.get(k);
            Integer v1 = map1.get(k);
            Integer v2 = map2.get(k);
            Integer v3 = map3.get(k);
            Integer v4 = map4.get(k);
            Integer v5 = map5.get(k);
            Integer dictV1 = dictMap1.get(k);
            boolean eq = eq(v0, v1);
            if (!eq) {
                map1.get(k);
                map1.put(k, v);
                map1.get(k);
                assert eq;
            }
            eq = eq(v0, v2);
            if (!eq) {
                map2.get(k);
                map2.put(k, v);
                map2.get(k);
                assert eq;
            }
            eq = eq(v0, v3);
            if (!eq) {
                map3.get(k);
                map3.put(k, v);
                map3.get(k);
                assert eq;
            }
            eq = eq(v0, v4);
            if (!eq) {
                map4.get(k);
                map4.put(k, v);
                map4.get(k);
                assert eq;
            }
            eq = eq(v0, v5);
            if (!eq) {
                map5.get(k);
                map5.put(k, v);
                map5.get(k);
                assert eq;
            }
            eq = eq(v0, dictV1);
            if (!eq) {
                dictMap1.get(k);
                dictMap1.put(k, v);
                dictMap1.get(k);
                assert eq;
            }
            
        }

        for (int i = 0; i < maxSize; i++) {
            int k = r.nextInt(maxVal / 2);
            map0.remove(k);
            map1.remove(k);
            map2.remove(k);
            map3.remove(k);
            map4.remove(k);
            map5.remove(k);
            dictMap1.remove(k);

            Integer v0 = map0.get(k);
            Integer v1 = map1.get(k);
            Integer v2 = map2.get(k);
            Integer v3 = map3.get(k);
            Integer v4 = map4.get(k);
            Integer v5 = map5.get(k);
            Integer dictV1 = dictMap1.get(k);
            boolean eq = eq(v0, v1);
            if (!eq) {
                map1.get(k);
                map1.remove(k);
                map1.get(k);
                assert eq;
            }
            eq = eq(v0, v2);
            if (!eq) {
                map2.get(k);
                map2.remove(k);
                map2.get(k);
                assert eq;
            }
            eq = eq(v0, v3);
            if (!eq) {
                map3.get(k);
                map3.remove(k);
                map3.get(k);
                assert eq;
            }
            eq = eq(v0, v4);
            if (!eq) {
                map4.get(k);
                map4.remove(k);
                map4.get(k);
                assert eq;
            }
            eq = eq(v0, v5);
            if (!eq) {
                map5.get(k);
                map5.remove(k);
                map5.get(k);
                assert eq;
            }
            eq = eq(v0, dictV1);
            if (!eq) {
                dictMap1.get(k);
                dictMap1.remove(k);
                dictMap1.get(k);
                assert eq;
            }
        }
    }

    @Test
    public void test_auto02() {
        HashMap<Integer, Integer> map0 = new HashMap<>();
        HashMap_openAddressing_01<Integer, Integer> openMap1 = new HashMap_openAddressing_01<>();
        HashMap_openAddressing_02<Integer, Integer> openMap2 = new HashMap_openAddressing_02<>();

        for (int i = 0; i < maxSize; i++) {
            int k = r.nextInt(maxVal);
            int v = r.nextInt(maxVal);

            map0.put(k, v);
            openMap1.put(k, v);
            openMap2.put(k, v);
            
            Integer v0 = map0.get(k);
            Integer openV1 = openMap1.get(k);
            Integer openV2 = openMap2.get(k);
            boolean eq = eq(v0, openV1);
            if (!eq) {
                openMap1.get(k);
                openMap1.put(k, v);
                openMap1.get(k);
                assert eq;
            }
            eq = eq(v0, openV2);
            if (!eq) {
                openMap2.get(k);
                openMap2.put(k, v);
                openMap2.get(k);
                assert eq;
            }
        }

        for (int i = 0; i < maxSize; i++) {
            int k = r.nextInt(maxVal / 2);
            map0.remove(k);
            openMap1.remove(k);
            openMap2.remove(k);


            Integer v0 = map0.get(k);
            Integer openV1 = openMap1.get(k);
            Integer openV2 = openMap2.get(k);
            boolean eq = eq(v0, openV1);
            if (!eq) {
                openMap1.get(k);
                openMap1.remove(k);
                openMap1.get(k);
                assert eq;
            }
            eq = eq(v0, openV2);
            if (!eq) {
                openMap2.get(k);
                openMap2.remove(k);
                openMap2.get(k);
                assert eq;
            }
        }
    }

    private static boolean eq(Object o1, Object o2) {
        if (o1 == null  && o2 == null) {
            return true;
        }
        return Objects.equals(o1, o2);
    }
}


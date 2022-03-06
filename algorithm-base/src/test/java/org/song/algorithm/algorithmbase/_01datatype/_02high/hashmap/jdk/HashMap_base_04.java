package org.song.algorithm.algorithmbase._01datatype._02high.hashmap.jdk;

/**
 * 实现简单功能的 HashMap, 模仿JDK中的HashMap
 * 增加树化, 这里采用AVL树
 *
 * @param <K>
 * @param <V>
 */
public class HashMap_base_04<K, V> extends HashMap_base_03<K, V> {

    private Entry<K, V>[] datas;

    private double dilatationRatio = 0.75;

    private int initCapacity = 1 << 3;

    private int size;

    public HashMap_base_04() {
        datas = new Entry[initCapacity];
    }

    public HashMap_base_04(int capacity) {
        datas = new Entry[initCapacity = upPower(capacity)];
    }

}

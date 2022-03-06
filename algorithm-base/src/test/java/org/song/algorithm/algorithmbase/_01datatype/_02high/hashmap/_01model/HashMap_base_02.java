package org.song.algorithm.algorithmbase._01datatype._02high.hashmap._01model;

/**
 * 实现简单功能的 HashMap, 模仿JDK中的HashMap
 *
 * 相比较 HashMap_base_01
 * 1. 数组+链表
 * 2. 数组容量随意
 * 3. 链表采用 头插法或尾插法
 * 4. 扩容采用2倍
 * 变化
 * 1. 增加 hash 扰动算法, hash计算将高16位纳入计算范围
 * 2. 采用 & 替换 % 计算下标, 效率较高
 *
 * @param <K>
 * @param <V>
 */
public class HashMap_base_02<K, V> extends HashMap_base_01<K, V> {

    public HashMap_base_02() {
        datas = new Entry[initCapacity];
    }

    public HashMap_base_02(int capacity) {
        datas = new Entry[initCapacity = upPower(capacity)];
    }

    protected int upPower(int n) {
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return n <= 0 ? 16 : n + 1;
    }

    @Override
    protected int hash(K k) {
        if (k == null) {
            return 0;
        }
        // 相同的值 x, 在不同的JVM进程中返回的值可能不同, 但在同一个JVM进程中相同
//        int hash = System.identityHashCode(k); // 注意, 此函数同样的值计算的hash可能不相等(128以内相等), TODO 待研究
        int hash = k.hashCode();
        // 高16位也参与计算, hash扰动算法
        return hash ^ (hash >>> 16);
    }

    @Override
    protected int getIndex(int hash, int length) {
        return hash & (length - 1);
    }
}

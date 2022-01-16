package org.song.algorithm.algorithmbase._01datatype._02high.hashmap.jdk;

/**
 * 实现简单功能的 HashMap,
 * <p>
 * 冲突处理方式: 采用开放定址法, 使用线性探测找到下一个空格
 *
 * @param <K>
 * @param <V>
 */
public class HashMap_clash_01<K, V> {

    private Entry<K, V>[] datas;

    private double dilatationRatio = 0.6;

    private int initCapacity = 1 << 3;

    private int size;

    public HashMap_clash_01() {
        datas = new Entry[initCapacity];
    }

    public HashMap_clash_01(int capacity) {
        datas = new Entry[initCapacity = upPower(capacity)];
    }

    public V get(K k) {
        int hash = hash(k);
        Entry<K, V> head = datas[hash & (datas.length - 1)];
        if (head == null) {
            return null;
        } else if (head.k.equals(k)) {
            return head.val;
        } else {

        }
        return null;
    }

    public V put(K k, V v) {
        int hash = hash(k);
        int len = datas.length;
        int index = hash & (len - 1);

        Entry<K, V> oldEntry = null;
        Entry<K, V> head = datas[index];
        if (head == null) {
            datas[index] = new Entry<>(k, v, hash);
        } else if (head.k.equals(k)) {
            oldEntry = datas[index];
            datas[index] = new Entry<>(k, v, hash);
            return oldEntry.val;
        } else {
            /*
             冲突处理
             采用开放定址法, 使用线性探测找到下一个空格 并放入
             如果到数组末尾, 则从头开始
             */
            for (int i = 0; i < len; i++) {
                int ni = index + i >= len ? i + index - len : i;
                if (datas[ni] != null) {
                    if (datas[ni].k.equals(k)) {
                        oldEntry = datas[ni];
                        datas[ni] = new Entry<>(k, v, hash);
                        return oldEntry.val;
                    }
                    continue;
                }
                datas[ni] = new Entry<>(k, v, hash);
            }
        }
        size++;
        ensureCapacity();
        return null;
    }

    public V remove(K k) {
        int hash = hash(k);
        int len = datas.length;
        int index = hash & (len - 1);

        Entry<K, V> head = datas[index];
        if (head == null) {
            return null;
        } else if (head.k.equals(k)) {
            size--;
            datas[index] = null;
            return head.val;
        } else {
            for (int i = 0; i < len; i++) {
                int ni = index + i >= len ? i + index - len : i;
                if (datas[ni] != null && datas[ni].k.equals(k)) {
                    head = datas[ni];
                    datas[ni] = null;
                    size--;
                    return head.val;
                }
            }

        }
        return null;
    }

    private static int upPower(int n) {
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return n <= 0 ? 16 : n + 1;
    }

    /**
     * 确保容量
     */
    private void ensureCapacity() {
        if ((double) size / (double) datas.length >= dilatationRatio) {
            dilatation();
        }
    }

    /**
     * 扩容
     */
    private void dilatation() {
        Entry<K, V>[] newDatas = new Entry[datas.length << 1];

        for (int i = 0; i < datas.length; i++) {
            if (datas[i] == null) {
                continue;
            }
            int newIndex = datas[i].hash & (newDatas.length - 1);

            Entry<K, V> head = newDatas[newIndex];
            if (head == null) {
                newDatas[newIndex] = datas[i];
            } else {
                for (int j = 0; j < newDatas.length; j++) {
                    int ni = newIndex + i >= newDatas.length ? i + newIndex - newDatas.length : i;
                    if (newDatas[ni] == null) {
                        newDatas[ni] = datas[i];
                        break;
                    }
                }
            }

        }
        datas = newDatas;
    }

    public int hash(K k) {
        if (k == null) {
            return 0;
        }
        // 相同的值 x, 在不同的JVM进程中返回的值可能不同, 但在同一个JVM进程中相同
        int hash = System.identityHashCode(k);
//        int hash = k.hashCode();
        return hash ^ (hash >>> 16);
    }

    class Entry<K, V> {

        K k;
        V val;
        int hash;

        public Entry(K k, V val, int hash) {
            this.k = k;
            this.val = val;
            this.hash = hash;
        }
    }
}

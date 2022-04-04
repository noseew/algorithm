package org.song.algorithm.base._01datatype._02high.hashmap._01base;

/**
 * 实现简单功能的 HashMap,
 * <p>
 * 冲突处理方式: 采用开放定址法, 使用2次探测找到下一个空格
 * 效率上远低于链地址法, 优点在于空间占用较低
 *
 * @param <K>
 * @param <V>
 */
public class HashMap_openAddressing_02<K, V> extends HashMap_openAddressing_01<K, V> {

    protected double dilatationRatio = 0.6;

    public HashMap_openAddressing_02() {
        super();
    }

    public HashMap_openAddressing_02(int capacity) {
        super(capacity);
    }

    @Override
    public V get(K k) {
        int hash = hash(k);
        int index = getIndex(hash, datas.length);
        Entry<K, V> head = datas[index];
        if (head == null) {
            return null; // 不存在
        } else if (hash == hash(head.k) && (k == head.k || head.k.equals(k))) {
            return head.val; // 相等直接返回
        } else {
            int nextIndex = detect(datas, index, k, hash);
            if (nextIndex < 0) {
                return null;
            }
            Entry<K, V> entry = datas[nextIndex];
            if (entry != null && (hash == hash(entry.k) && (k == entry.k || entry.k.equals(k)))) {
                // 找到了, 返回他
                return entry.val;
            }
        }
        return null;
    }

    @Override
    public V put(K k, V v) {
        int hash = hash(k);
        int index = getIndex(hash, datas.length);

        Entry<K, V> head = datas[index];
        boolean added = false;
        if (head == null) {
            datas[index] = new Entry<>(k, v, null, hash);
            added = true;
        } else if (hash == hash(head.k) && (k == head.k || head.k.equals(k))) {
            datas[index] = new Entry<>(k, v, null, hash);
            return head.val;
        } else {
            /*
             冲突处理
             线性探测, 使用线性探测找到下一个空格 并放入
             如果到数组末尾, 则从头开始
             */
            int nextIndex = detect(datas, index, k, hash);
            if (nextIndex < 0) {
                return v;
            }
            Entry<K, V> entry = datas[nextIndex];
            if (entry == null) {
                // 不等 直接放入
                datas[nextIndex] = new Entry<>(k, v, null, hash);
                added = true;
            } else if (hash == hash(entry.k) && (k == entry.k || entry.k.equals(k))) {
                datas[nextIndex] = new Entry<>(k, v, null, hash);
                return entry.val;
            }
        }
        size++;
        ensureCapacity();
        return added ? null : v;
    }

    @Override
    public V remove(K k) {
        int hash = hash(k);
        int index = getIndex(hash, datas.length);

        Entry<K, V> head = datas[index];
        if (head == null) {
            return null;
        } else if (hash == hash(head.k) && (k == head.k || head.k.equals(k))) {
            size--;
            datas[index] = null;
            return head.val;
        } else {
            int nextIndex = detect(datas, index, k, hash);
            if (nextIndex < 0) {
                return null;
            }
            Entry<K, V> entry = datas[nextIndex];
            if (entry != null && entry.k.equals(k)) {
                datas[nextIndex] = null;
                size--;
                return entry.val;
            }
        }
        return null;
    }

    /**
     * 确保容量
     */
    @Override
    protected void ensureCapacity() {
        if ((double) size / (double) datas.length >= dilatationRatio) {
            dilatation();
        }
    }

    /**
     * 扩容
     */
    @Override
    protected void dilatation() {
        Entry<K, V>[] newDatas = new Entry[datas.length << 1];

        for (Entry<K, V> entry : datas) {
            if (entry == null) {
                continue;
            }
            int index = getIndex(entry.hash, newDatas.length);

            Entry<K, V> head = newDatas[index];
            if (head == null) {
                newDatas[index] = entry;
            } else {
                for (int i = 0; i < newDatas.length; i++) {
                    int nextIndex = (index + i) % newDatas.length;
                    if (newDatas[nextIndex] == null) {
                        newDatas[nextIndex] = entry;
                        break;
                    }
                }
            }
        }
        datas = newDatas;
    }

    /**
     * 二次探测
     * 当前位置如果有数据, 就线性往下一个位置堂测, 找的顺序是[1^2,-(1^2),2^2,-(2^2)...], 采用前后2次方数的位置进行探测, 直到找到空位即可
     * 优点, 解决线性探测数据堆积的问题
     * 缺点, 找到下个位置概率降低, 存储数据失败率变高
     */
    @Override
    protected int detect(Entry<K, V>[] datas, int currentIndex, K k, int hash) {
        for (int i = 1; i < datas.length; i *= 2) {
            // 遍历数组, 哪个有空放哪
            int nextIndex = (currentIndex + i) % datas.length;
            Entry<K, V> entry = datas[nextIndex];
            if (entry == null) return nextIndex;
            if (hash == hash(entry.k) && (k == entry.k || entry.k.equals(k))) {
                return nextIndex;
            }
            nextIndex = Math.abs(currentIndex - i) % datas.length;
            entry = datas[nextIndex];
            if (entry == null) return nextIndex;
            if (hash == hash(entry.k) && (k == entry.k || entry.k.equals(k))) {
                return nextIndex;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("size=").append(size);
        sb.append("\r\n");
        int count = 0;
        for (int i = 0; i < datas.length; i++) {
            Entry<K, V> data = datas[i];
            sb.append(count++).append("-").append(i);
            if (data != null) {
                sb.append(": ").append(data.toString());
                sb.append("\r\n");
            } else {
                sb.append("\r\n");
            }
        }
        return sb.toString();
    }
}

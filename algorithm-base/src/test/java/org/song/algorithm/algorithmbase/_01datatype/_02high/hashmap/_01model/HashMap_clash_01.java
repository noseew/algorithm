package org.song.algorithm.algorithmbase._01datatype._02high.hashmap._01model;

/**
 * 实现简单功能的 HashMap,
 * <p>
 * 冲突处理方式: 采用开放定址法, 使用线性探测找到下一个空格
 *
 * @param <K>
 * @param <V>
 */
public class HashMap_clash_01<K, V> extends HashMap_base_03<K, V> {

    protected double dilatationRatio = 0.6;

    public HashMap_clash_01() {
        super();
    }

    public HashMap_clash_01(int capacity) {
        super(capacity);
    }

    @Override
    public V get(K k) {
        int index = getIndex(hash(k), datas.length);
        Entry<K, V> head = (Entry<K, V>) datas[index];
        if (head == null) {
            return null; // 不存在
        } else if (head.k.equals(k)) {
            return head.val; // 相等直接返回
        } else {
            for (int i = 0; i < datas.length; i++) {
                // 遍历数组
                int nextIndex = (index + i) % datas.length;
                AbstractMap.Entry<K, V> entry = datas[nextIndex];
                if (entry != null && entry.k.equals(k)) {
                    // 找到了, 返回他
                    return entry.val;
                }
            }
        }
        return null;
    }

    @Override
    public V put(K k, V v) {
        int hash = hash(k);
        int index = getIndex(hash, datas.length);

        Entry<K, V> head = (Entry<K, V>) datas[index];
        if (head == null) {
            datas[index] = new Entry<>(k, v, null, hash);
        } else if (head.k.equals(k)) {
            datas[index] = new Entry<>(k, v, null, hash);
            return head.val;
        } else {
            /*
             冲突处理
             采用开放定址法, 使用线性探测找到下一个空格 并放入
             如果到数组末尾, 则从头开始
             */
            for (int i = 0; i < datas.length; i++) {
                // 遍历数组, 哪个有空放哪
                int nextIndex = (index + i) % datas.length;
                Entry<K, V> entry = (Entry<K, V>) datas[nextIndex];
                if (entry != null) {
                    // 相等替换
                    if (entry.k.equals(k)) {
                        datas[nextIndex] = new Entry<>(k, v, null, hash);
                        return entry.val;
                    }
                    continue;
                }
                // 不等 直接放入
                datas[nextIndex] = new Entry<>(k, v, null, hash);
                break;
            }
        }
        size++;
        ensureCapacity();
        return null;
    }

    @Override
    public V remove(K k) {

        int index = getIndex(hash(k), datas.length);

        Entry<K, V> head = (Entry<K, V>) datas[index];
        if (head == null) {
            return null;
        } else if (head.k.equals(k)) {
            size--;
            datas[index] = null;
            return head.val;
        } else {
            for (int i = 0; i < datas.length; i++) {
                int nextIndex = (index + i) % datas.length;
                Entry<K, V> entry = (Entry<K, V>) datas[nextIndex];
                if (entry != null && entry.k.equals(k)) {
                    datas[nextIndex] = null;
                    size--;
                    return entry.val;
                }
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

        for (int i = 0; i < datas.length; i++) {
            Entry<K, V> entry = (Entry<K, V>) datas[i];
            if (entry == null) {
                continue;
            }
            int index = getIndex(entry.hash, newDatas.length);

            Entry<K, V> head = newDatas[index];
            if (head == null) {
                newDatas[index] = entry;
            } else {
                for (int j = 0; j < newDatas.length; j++) {
                    int nextIndex = (index + i) % datas.length;
                    if (newDatas[nextIndex] == null) {
                        newDatas[nextIndex] = entry;
                        break;
                    }
                }
            }

        }
        datas = newDatas;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("size=").append(size);
        sb.append("\r\n");
        for (AbstractMap.Entry<K, V> data : datas) {
            if (data != null) {
                sb.append(data.toString());
                sb.append("\r\n");
            }
        }
        return sb.toString();
    }
}

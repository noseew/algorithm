package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.ArrayBase01;

import java.util.Random;

public abstract class AbstractSkipListMap<K extends Comparable<K>, V> {

    protected final int maxLevel = 32;
    protected int size;

    protected final Random r = new Random();

    public abstract V put(K k, V v);

    public abstract V get(K k);

    public abstract V remove(K k);

    public abstract void clean();

    public abstract int size();

    protected boolean less(K k1, K k2) {
        // k==null, 说明是头结点, 头结点永远最小
        if (k1 == null) return true;
        if (k2 == null) return false;
        return k1.compareTo(k2) < 0;
    }

    protected boolean gather(K k1, K k2) {
        // k==null, 说明是头结点, 头结点永远最小
        if (k1 == null) return false;
        if (k2 == null) return true;
        return k1.compareTo(k2) > 0;
    }

    /**
     * 随机获取层数, 从1开始, 最低0层, 表示不构建索引, 最高层数 == headerIndex 的层数
     * 有0.5的概率不会生成索引
     * 从低位开始, 连续1的个数就是索引的层数
     *
     * @return
     */
    protected int buildLevel(int maxLevel) {
        // 随机层高
        int nextInt = r.nextInt(Integer.MAX_VALUE);
        int level = 0;
        // 最高层数 == headerIndex 的层数
        for (int i = 1; i <= maxLevel && i <= this.maxLevel; i++) {
            if ((nextInt & 0B1) != 0B1) break;
            nextInt = nextInt >>> 1;
            level++;
        }
        return level;
    }
}

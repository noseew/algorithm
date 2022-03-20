package org.song.algorithm.algorithmbase._01datatype._01base._04tree.heap;

import java.util.Arrays;

public abstract class AbstractHeap<T> {

    protected T[] datas;
    protected int size;
    protected boolean little;

    public abstract void push(T v);

    public abstract T pop();
    
    public abstract T replace(T v);


    /**
     * 子节点上升 上滤
     * 新元素存储到数组下一位, 新元素在叶子节点判断一次上升, 直到合适的位置
     */
    protected void shiftUp(int child) {
        /*
         子节点依次上升
         子节点上升路线跟子节点在哪个叶子分支有关
         只需要和父节点对比, 只要比父节点小(小堆), 则肯定比兄弟节点小(小堆)
         */
        int childIndex = child;
        int parentIndex;
        while ((parentIndex = (childIndex - 1) >> 1) >= 0) {
            // 父子对比并交换
            if (less(parentIndex, childIndex)) {
                exchange(parentIndex, childIndex);
                // 索引上移
                childIndex = parentIndex;
            } else {
                break;
            }
        }
    }

    /**
     * 父节点下降 下滤
     * 现将尾结点元素移动到根节点上, 然后对根节点进行下降调整
     */
    protected void shiftDown(int parent) {
        /*
         父节点依次下降
         父节点下降路线和对比有关(和 子节点依次上升 不同)
         父节点在(left, right)中选择一个最小(小堆)的子节点, 作为对比分支
         父节点只要比最小子节点大, 则最小子节点一定应当是父节点, 因为: 最小子节点 < 兄弟节点 & 最小子节点 < 父节点
         */
        int parentIndex = parent;
        int leftIndex;
        while ((leftIndex = ((parentIndex << 1) + 1)) < size) {
            int rightIndex = leftIndex + 1;
            // 找出较 小/大 的子节点
            int child = match(leftIndex, rightIndex);
            // 父子对比并交换
            if (less(parentIndex, child)) {
                exchange(parentIndex, child);
                parentIndex = child;
            } else {
                break;
            }
        }
    }

    protected void checkSize() {
        if (size == 0) {
            throw new RuntimeException("堆为空");
        }
    }

    /**
     * 父子交换
     *
     * @param parentIndex
     * @param childIndex
     */
    protected void exchange(int parentIndex, int childIndex) {
        T parent = datas[parentIndex];
        datas[parentIndex] = datas[childIndex];
        datas[childIndex] = parent;
    }

    /**
     * 是否需要交换
     * childIndex 是否 大于/小于 parentIndex
     *
     * @param parentIndex
     * @param childIndex
     * @return
     */
    protected boolean less(int parentIndex, int childIndex) {
        if (parentIndex == childIndex) return false;

        T parent = (T) datas[parentIndex];
        T child = (T) datas[childIndex];
        if (child == null) return false;

        if (!little) {
            // 大堆
            return ((Comparable)parent).compareTo(child) < 0;
        } else {
            // 小堆
            return ((Comparable)parent).compareTo(child) > 0;
        }
    }

    protected boolean less(T parent, int childIndex) {

        T child = (T) datas[childIndex];
        if (child == null) return false;

        if (!little) {
            // 大堆
            return ((Comparable)parent).compareTo(child) < 0;
        } else {
            // 小堆
            return ((Comparable)parent).compareTo(child) > 0;
        }
    }
    
    protected boolean less(int parentIndex, T child) {
        if (child == null) return false;

        T parent = (T) datas[parentIndex];

        if (!little) {
            // 大堆
            return ((Comparable)parent).compareTo(child) < 0;
        } else {
            // 小堆
            return ((Comparable)parent).compareTo(child) > 0;
        }
    }

    /**
     * 返回子节点中符合条件的那个子节点索引
     * 如果是大堆, 则返回更大的那个节点
     * 如果是小堆, 则返回更小的那个节点
     * 
     * @param left
     * @param right
     * @return
     */
    protected int match(int left, int right) {
        if (left == right || right >= size) {
            return left;
        }

        T childLeft = (T) datas[left];
        T childRight = (T) datas[right];
        if (childLeft == null) {
            return right;
        }
        if (childRight == null) {
            return left;
        }

        if (!little) {
            // 大堆
            return ((Comparable)childLeft).compareTo(childRight) < 0 ? right : left;
        } else {
            // 小堆
            return ((Comparable)childLeft).compareTo(childRight) > 0 ? right : left;
        }
    }
    
    public String toString() {
        return Arrays.toString(datas);
    }

    public String toPretty() {
        // 层数, 从1开始
        int totalLevel = (int) Math.floor(log2(size)) + 1;
        if (totalLevel <= 0) {
            return "";
        }
        // 每层元素数
        int[] levelCount = new int[totalLevel];
        for (int i = 0; i < totalLevel; i++) {
            if (i == 0) {
                levelCount[i] = 1;
            } else {
                levelCount[i] = i << 1;
            }
        }
        StringBuilder sb = new StringBuilder();

        int count = 0;
        for (int i = 0; i < levelCount.length; i++) {
            boolean inter = false;
            for (int j = 0; j < levelCount[i]; j++) {
                sb.append(datas[count++]).append(",");
                if (count >= size) {
                    inter = true;
                    break;
                }
            }
            if (inter) {
                break;
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    public static double log2(double x) {
        double n = 2;//底数
        double m = x;//指数
        return Math.log(m) / Math.log(n);
    }
}

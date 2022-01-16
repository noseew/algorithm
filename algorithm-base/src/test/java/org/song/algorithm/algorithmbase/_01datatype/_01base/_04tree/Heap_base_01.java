package org.song.algorithm.algorithmbase._01datatype._01base._04tree;

import java.lang.reflect.Array;
import java.util.Comparator;

/**
 * heap 基于数组存储
 * 逻辑结构 heap, 存储结构 数组
 * 堆节点关系
 * i: 当前节点
 * parent(i) = floor((i - 1)/2) = (i - 1) >> 1
 * child(i)  = 2*i + 1     =   i << 1 + 1
 * left(i)   = 2*i + 1     =   i << 1 + 1
 * right(i)  = 2*i + 2     =   i << 1 + 2
 *
 * 思路: 以小堆为例
 * 1. push: 存入heap尾, 依次上升
 * -    存储在数组最后一个元素(完全二叉树最后一个元素, 叶子节点)
 * -    和父节点比较, 子<父 则向上升
 * 2. pop: 取出heap顶, heap尾移动到heap顶, 选择分支, 依次下降
 * -    取出heap顶, 将heap尾元素移动到heap顶
 * -    heap顶作为父节点 parent, 选一个最小的 child = Min(left, right),
 * -    parent 和 child 对比并互换, 依次下降
 *
 *
 * @param <T>
 */
public class Heap_base_01<T> {

    private T[] datas;
    private int size;
    /**
     * 0: 小堆(默认)
     * 1: 大堆
     */
    private int property;
    private Comparator<T> comparator;

    private double dilatationRatio = 0.8;
    private static int initCapacity = 7;

    public Heap_base_01() {
        this.property = 0;
        datas = (T[]) new Object[initCapacity];
    }

    public Heap_base_01(int property, Comparator<T> comparator) {
        this.property = property;
        this.comparator = comparator;
        datas = (T[]) new Object[initCapacity];
    }

    /*
     堆节点关系
        i: 当前节点
        parent(i) = floor((i - 1)/2) = (i - 1) >> 1
        child(i)  = 2*i + 1     =   i << 1 + 1
        left(i)   = 2*i + 1     =   i << 1 + 1
        right(i)  = 2*i + 2     =   i << 1 + 2
     */

    public void push(T v) {
        // 新元素存储到数组下一位,
        datas[size++] = v;
        if (size == 1) {
            // 只有一个元素
            return;
        }
        shiftUp(size - 1);
        ensureCapacity();
    }

    public T pop() {
        if (size == 0) {
            return null;
        }
        T v = datas[0];
        datas[0] = datas[size - 1];
        datas[size - 1] = null;
        shiftDown(0);
        size--;
        return v;
    }

    /**
     * 子节点上升
     * 新元素存储到数组下一位, 新元素在叶子节点判断一次上升, 直到合适的位置
     */
    private void shiftUp(int child) {
        /*
         子节点依次上升
         子节点上升路线跟子节点在哪个叶子分支有关
         只需要和父节点对比, 只要比父节点小(小堆), 则肯定比兄弟节点小(小堆)
         */
        int childIndex = child;
        int parenIndex;
        while ((parenIndex = (childIndex - 1) >> 1) >= 0) {
            // 父子对比并交换
            if (isExchange(parenIndex, childIndex)) {
                exchange(parenIndex, childIndex);
                // 索引上移
                childIndex = parenIndex;
            } else {
                break;
            }
        }
    }

    /**
     * 父节点下降
     * 现将尾结点元素移动到根节点上, 然后对根节点进行下降调整
     */
    private void shiftDown(int parent) {
        /*
         父节点依次下降
         父节点下降路线和对比有关(和 子节点依次上升 不同)
         父节点在(left, right)中选择一个最小(小堆)的子节点, 作为对比分支
         父节点只要比最小子节点大, 则最小子节点一定应当是父节点, 因为: 最小子节点 < 兄弟节点 & 最小子节点 < 父节点
         */
        int parenIndex = parent;
        int leftIndex;
        while ((leftIndex = ((parenIndex << 1) + 1)) < size) {
            int rightIndex = leftIndex + 1;
            // 找出较 小/大 的子节点
            int child = match(leftIndex, rightIndex);
            // 父子对比并交换
            if (isExchange(parenIndex, child)) {
                exchange(parenIndex, child);
                parenIndex = child;
            } else {
                break;
            }
        }
    }

    /**
     * 父子交换
     *
     * @param parentIndex
     * @param childIndex
     */
    private void exchange(int parentIndex, int childIndex) {
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
    private boolean isExchange(int parentIndex, int childIndex) {
        if (parentIndex == childIndex) {
            return false;
        }

        T parent = (T) datas[parentIndex];
        T child = (T) datas[childIndex];
        if (child == null) {
            return false;
        }

        if (property == 1) {
            // 大堆
            if (comparator != null) {
                return comparator.compare(parent, child) < 0;
            }
            return ((Comparable) parent).compareTo(child) < 0;
        } else {
            // 小堆
            if (comparator != null) {
                return comparator.compare(parent, child) > 0;
            }
            return ((Comparable) parent).compareTo(child) > 0;
        }
    }

    private int match(int left, int right) {
        if (left == right) {
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

        if (property == 1) {
            // 大堆
            if (comparator != null) {
                if (comparator.compare(childLeft, childRight) < 0) {
                    return right;
                }
                return left;
            }
            if (((Comparable) childLeft).compareTo(childRight) < 0) {
                return right;
            }
            return left;
        } else {
            // 小堆
            if (comparator != null) {
                if (comparator.compare(childLeft, childRight) > 0) {
                    return right;
                }
                return left;
            }
            if (((Comparable) childLeft).compareTo(childRight) > 0) {
                return right;
            }
            return left;
        }
    }

    /**
     * 确保容量
     */
    private void ensureCapacity() {
        if ((double) size / (double) datas.length > dilatationRatio) {
            // 扩容 1.5 倍
            T[] newDatas = (T[]) Array.newInstance(Comparable.class, datas.length + (datas.length >> 1));
            System.arraycopy(datas, 0, newDatas, 0, size);
            datas = newDatas;
        }
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
                sb.append(datas[count++]);
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

    public static void appendEnter(StringBuilder sb, String e, int times) {
        for (int i = 0; i < times; i++) {
            sb.append(e);
        }
    }

    public static double log2(double x) {
        double n = 2;//底数
        double m = x;//指数
        return Math.log(m) / Math.log(n);
    }

}

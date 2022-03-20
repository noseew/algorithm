package org.song.algorithm.algorithmbase._01datatype._01base._04tree.heap;

/**
 * heap 基于数组存储
 * <p>
 * 优化上滤/下滤方法, 减少比较次数
 *
 * @param <T>
 */
public class Heap_base_02<T> extends Heap_base_01<T> {

    public Heap_base_02() {
        this(true);
    }

    public Heap_base_02(boolean little) {
        super(little);
    }

    /**
     * 优化上滤
     * 原始方法, 新节点需要层层比较并替换
     * 示例: [(1),4,3,2] => [4,(1),3,2] => [4,3,(1),2] => [4,3,2,(1)]
     * 优化方法, 依然需要层层比较, 但是只需要将新节点直接放入最后比较的位置中即可, 减少了多次交换的方法
     * 示例: (1)[null,4,3,2] => [4,null,3,2] => [4,3,null,2] => [4,3,2,null] => [4,3,2,(1)]
     * 
     * @param child
     */
    protected void shiftUp(int child) {
        
        // 先将子元素取出
        T v = datas[child];
        
        int childIndex = child;
        int parentIndex;
        while ((parentIndex = (childIndex - 1) >> 1) >= 0) {
            if (less(parentIndex, v)) {
                // 直接将父元素放入子元素位置, 子元素取出, 然后等待合适时机再放入
                datas[parentIndex] = datas[childIndex];
                childIndex = parentIndex;
            } else {
                break;
            }
        }
        // 子元素最终放入的位置
        datas[childIndex] = v;
    }

    protected void shiftDown(int parent) {
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
            if (less(parenIndex, child)) {
                exchange(parenIndex, child);
                parenIndex = child;
            } else {
                break;
            }
        }
    }

}

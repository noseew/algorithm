package org.song.algorithm.base._01datatype._01base._04tree.heap;

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

    /**
     * 优化下滤, 思路桶优化上滤
     * 
     * @param parent
     */
    protected void shiftDown(int parent) {

        // 先将父元素取出
        T v = datas[parent];
        
        int parentIndex = parent;
        int leftIndex;
        while ((leftIndex = ((parentIndex << 1) + 1)) < size) {
            int rightIndex = leftIndex + 1;
            int child = match(leftIndex, rightIndex);
            if (less(v, child)) {
                // 直接将子元素放入父元素位置, 父元素取出, 然后等待合适时机再放入
                datas[parentIndex] = datas[child];
                parentIndex = child;
            } else {
                break;
            }
        }
        // 子元素最终放入的位置
        datas[parentIndex] = v;
    }

}

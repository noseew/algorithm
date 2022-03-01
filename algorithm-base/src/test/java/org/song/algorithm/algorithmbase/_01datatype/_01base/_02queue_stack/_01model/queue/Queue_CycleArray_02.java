package org.song.algorithm.algorithmbase._01datatype._01base._02queue_stack._01model.queue;

/**
 * 循环数组实现队列
 * 优化效率
 *
 * @param <T>
 */
public class Queue_CycleArray_02<T> extends Queue_CycleArray_01<T> {

    private final int capacity;

    public Queue_CycleArray_02(int capacity) {
        /*
        循环数组, 
        数组大小必须是2次幂数
         */
        super(upPower(capacity));
        this.capacity = upPower(capacity);
    }

    @Override
    public void rpush(T v) {
        if (isFull()) throw new RuntimeException("队列已满");
        // 直接将元素放入end下标, 防止下标越界需要取余, 然后end后移, 并恢复到可控范围内
        data[end = (end & (capacity - 1))] = v;
        end++;
        size++;
    }

    @Override
    public void lpush(T v) {
        if (isFull()) throw new RuntimeException("队列已满");
        // start 前移, 防止出现负数 所以加上 length, 并恢复到可控范围内
        data[start = ((start - 1 + data.length) & (capacity - 1))] = v;
        size++;
    }

    @Override
    public T rpop() {
        if (isEmpty()) throw new RuntimeException("队列为空");
        // 直接将end元素取出, 防止出现负数 所以加上 length, 并恢复到可控范围内
        T v = data[end = ((end - 1 + data.length) & (capacity - 1))];
        size--;
        return v;
    }

    @Override
    public T lpop() {
        if (isEmpty()) throw new RuntimeException("队列为空");
        // 直接将start元素取出, 防止下标越界需要取余, 然后start后移, 并恢复到可控范围内
        T v = data[start = (start & (capacity - 1))];
        start++;
        size--;
        return v;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = start; i < end; i++) {
            sb.append(data[i & (capacity - 1)]).append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    private static int upPower(int n) {
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return n <= 0 ? 16 : n + 1;
    }
}

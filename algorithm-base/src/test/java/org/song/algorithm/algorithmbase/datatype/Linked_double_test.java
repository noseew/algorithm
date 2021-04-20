package org.song.algorithm.algorithmbase.datatype;

import org.junit.Test;

public class Linked_double_test {

    @Test
    public void test() {
        Linked_double_02<Integer> deque = new Linked_double_02<>();
        deque.rpush(0);
        deque.lpush(-1);
        deque.lpush(-2);
        deque.rpush(1);
        deque.rpush(2);
        System.out.println(deque.toString());
        Integer rpop = deque.rpop();
        Integer lpop = deque.lpop();
        System.out.println();
        deque.remove(-1);
        System.out.println(deque.toString());
        System.out.println(deque.toPrettyString());
    }
}

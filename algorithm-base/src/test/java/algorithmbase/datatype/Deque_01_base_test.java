package algorithmbase.datatype;

import org.junit.Test;

public class Deque_01_base_test {

    @Test
    public void test() {
        Deque_01_base<Integer> deque = new Deque_01_base<>();
        deque.rpush(0);
        deque.lpush(-1);
        deque.rpush(1);
        System.out.println(deque.toString());
        Integer rpop = deque.rpop();
        Integer lpop = deque.lpop();
        System.out.println();
    }
}

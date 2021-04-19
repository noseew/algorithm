package algorithmbase.datatype;

import org.junit.Test;

public class Linked_01_base_test {

    @Test
    public void test() {
        Linked_01_base<Integer> linked = new Linked_01_base<>();
        linked.add(1);
        linked.add(2);
        linked.add(3);
        linked.add(4);
        linked.add(5);
        System.out.println(linked.toString());
        linked.remove(3);
        System.out.println(linked.toString());
    }
}

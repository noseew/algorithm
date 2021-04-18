package algorithmbase.datatype;

import org.junit.Test;

public class Array_01_base_test {

    @Test
    public void test() {
        Array_01_base<Integer> list = new Array_01_base<>(5);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        System.out.println(list.toString());
        list.remove(2);
        System.out.println(list.toString());
        list.clear();
        System.out.println(list.toString());

    }
}

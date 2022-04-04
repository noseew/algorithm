package org.song.algorithm.base._01datatype._01base._03string_array_matrix._02array;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._01datatype._01base._03string_array_matrix._02array._01model.Array2D01;

public class ArrayTest01 {
    
    @Test
    public void test01() {
        Array2D01 array2D01 = new Array2D01(false);
        array2D01.buildASC(10, 10);

        System.out.println(array2D01);

        System.out.println(array2D01.get(2, 3));
    }
}

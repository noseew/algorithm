package org.song.algorithm.algorithmbase._01datatype._01base._03string_array_matrix._02array;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._03string_array_matrix._02array._01model.Array2D01;
import org.song.algorithm.algorithmbase._01datatype._01base._03string_array_matrix._02array._01model.Matrix01;

public class MatrixTest01 {
    
    @Test
    public void test01() {
        Matrix01 matrix01 = new Matrix01();

        matrix01.buildSymmetry(10, 10);
        System.out.println(matrix01);
        System.out.println(matrix01.isSymmetry());

        matrix01.buildASC(10, 10);
        System.out.println(matrix01);
        System.out.println(matrix01.isSymmetry());

    }
}

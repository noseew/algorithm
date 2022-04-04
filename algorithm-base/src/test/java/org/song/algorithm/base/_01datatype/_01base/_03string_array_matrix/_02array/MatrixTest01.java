package org.song.algorithm.base._01datatype._01base._03string_array_matrix._02array;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._01datatype._01base._03string_array_matrix._02array._01model.Matrix01Symmetry;

public class MatrixTest01 {
    
    @Test
    public void test01() {
        Matrix01Symmetry matrix01Symmetry = new Matrix01Symmetry();

        matrix01Symmetry.buildSymmetry(10, 10);
        System.out.println(matrix01Symmetry);
        System.out.println(matrix01Symmetry.isSymmetry());
        assert matrix01Symmetry.isSymmetry();

        matrix01Symmetry.buildASC(10, 10);
        System.out.println(matrix01Symmetry);
        System.out.println(matrix01Symmetry.isSymmetry());
        assert !matrix01Symmetry.isSymmetry();

    }
}

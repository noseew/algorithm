package org.song.algorithm.algorithmbase._01datatype._01base._03string_array_matrix._02array._01model;

/**
 * 矩阵
 */
public class Matrix01 extends Array2D01 {
    

    public Matrix01() {
        super(true);
    }

    /**
     * 构建对称矩阵
     * 
     * @param m
     * @param n
     */
    public void buildSymmetry(int m, int n) {
        int a = 0;
        data = new int[m][n];

        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                if (i < j) {
                    data[j][i] = data[i][j];
                } else {
                    data[j][i] = a;
                    a++;
                }
            }
        }
    }

    /**
     * 判断是否是对称矩阵
     * 
     * @return
     */
    public boolean isSymmetry() {
        boolean flag = true;
        int m = data.length;
        for (int j = 0; j < m; j++) {
            int n = data[j].length;
            for (int i = j; i < n; i++) {
                flag &= data[j][i] == data[i][j];
            }
        }
        return flag;
    }
    
}

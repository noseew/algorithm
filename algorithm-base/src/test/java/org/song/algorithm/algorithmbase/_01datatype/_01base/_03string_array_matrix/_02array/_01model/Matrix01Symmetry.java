package org.song.algorithm.algorithmbase._01datatype._01base._03string_array_matrix._02array._01model;

/**
 * 矩阵
 * 对称矩阵
 */
public class Matrix01Symmetry extends Array2D01 {
    
    protected int[] compressData;
    
    public Matrix01Symmetry() {
        super(true);
    }

    /**
     * 构建对称矩阵
     * 
     * @param m
     * @param n
     */
    public void buildSymmetry(int m, int n) {
        this.m = m;
        this.n = n;
        
        int a = 0;
        data = new int[m][n];

        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                if (i < j) {
                    // 对称矩阵的 a_ij = a_ji
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
        for (int j = 0; j < m; j++) {
            for (int i = j; i < n; i++) {
                // 对称矩阵的 a_ij = a_ji
                flag &= data[j][i] == data[i][j];
            }
        }
        return flag;
    }

    /**
     * TODO 
     */
    public void compress() {
        /*
        对称矩阵, 将二维矩阵压缩存储在一维数组中
        数组长度s = n(n+1)/2 - 1
        
        a_ij = i*(i-1) / 2 + j - 1
         */
        compressData = new int[n * (n + 1) / 2 - 1];
        for (int k = 0; k < compressData.length; k++) {
            int i = 0, j = 0;
            compressData[k] = data[j][i];
        }
    }
    
}

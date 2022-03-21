package org.song.algorithm.algorithmbase._01datatype._01base._03string_array_matrix._02array._01model;

import java.util.Arrays;
import java.util.Random;

public class Array2D01 {
    /*
    二维数组
    n=总共行, 其中某个 i=x轴, 行坐标
    m=总共列, 其中某个 j=y轴, 列坐标
    某个元素 a_ij
     */
    protected int[][] data;

    // 排列存储方式, true: 行, false: 列
    protected boolean arrangementRow = true;

    protected Random r = new Random();

    public Array2D01(boolean arrangementRow) {
        this.arrangementRow = arrangementRow;
    }

    public void buildASC(int m, int n) {
        int a = 0;
        data = new int[m][n];

        if (arrangementRow) {
            for (int j = 0; j < m; j++) {
                for (int i = 0; i < n; i++) {
                    data[j][i] = a;
                    a++;
                }
            }
        } else {
            for (int j = 0; j < m; j++) {
                for (int i = 0; i < n; i++) {
                    data[i][j] = a;
                    a++;
                }
            }
        }
    }

    public void buildRandom(int m, int n) {
        int max = m * n;
        data = new int[m][n];

        if (arrangementRow) {
            for (int j = 0; j < m; j++) {
                for (int i = 0; i < n; i++) {
                    data[j][i] = r.nextInt(max);
                }
            }
        } else {
            for (int j = 0; j < m; j++) {
                for (int i = 0; i < n; i++) {
                    data[i][j] = r.nextInt(max);
                }
            }
        }
    }

    public int get(int j, int i) {
        if (arrangementRow) {
            return data[j][i];
        } else {
            return data[i][j];
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("按").append(arrangementRow ? "行" : "列").append("存储").append("\r\n");
        for (int j = 0; j < data.length; j++) {
            sb.append(Arrays.toString(data[j])).append("\r\n");
        }
        return sb.toString();
    }
}

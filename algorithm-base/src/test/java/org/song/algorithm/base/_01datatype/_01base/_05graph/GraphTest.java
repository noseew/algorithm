package org.song.algorithm.base._01datatype._01base._05graph;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._01datatype._01base._05graph._01model.base.AdJacentMatrix;

public class GraphTest {
    
    /*
    图:
    1 -- 2
    |
    3 -- 4
    顶点:
    [1,2,3,4]
    邻接矩阵:
    [0,1,1,0]
    [1,0,0,0]
    [1,0,0,1]
    [0,0,1,0]
     */
    @Test
    public void test() {
        AdJacentMatrix adJacentMatrix = new AdJacentMatrix(4, 3);

        // 构建顶点
        adJacentMatrix.buildVertexes(new int[]{1, 2, 3, 4});

        // 构建3条边
        adJacentMatrix.buildEdge(1, 2, 1);
        adJacentMatrix.buildEdge(1, 3, 1);
        adJacentMatrix.buildEdge(4, 3, 1);
        // 无向图要构建对称的边
        adJacentMatrix.buildEdge(2, 1, 1);
        adJacentMatrix.buildEdge(3, 1, 1);
        adJacentMatrix.buildEdge(3, 4, 1);

        System.out.println(adJacentMatrix.toMatrixString());

        adJacentMatrix.bfs(e -> {
            System.out.println(e);
            return true;
        });
    }
}

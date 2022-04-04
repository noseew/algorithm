package org.song.algorithm.base._01datatype._03app.geo.hexagon;

import java.util.Random;

/**
 * @description: 六边形算法
 * @author: jiali.song@song.com
 * @date: 2019年02月01日 19:03:46
 **/
public class HexagonTest {

    public static void main(String[] args) {
        int[] ints = GetHexGridIndex(20, 20, 2, 1, 1, 3, 3, (byte) 1);
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }

    /**
     * 根据参数获得六边形索引
     *
     * @param width       页面宽度
     * @param height      页面长度
     * @param r           六边形边长
     * @param thickness   六边形上下拓扑半间距
     * @param flat        扁平化程度（由于计算过程中，thickness在侧向拓扑中作用减小，故增加了这个系数）
     * @param x           横坐标
     * @param y           纵坐标
     * @param transparent 透明度设置，一般位置为正常值；如果是六边形边界位置的过渡色，减半
     * @return
     */
    public static int[] GetHexGridIndex(int width, int height, int r, int thickness, int flat, int x, int y, byte transparent) {
        //中心点索引（长方形中的六边形的索引）
        int indexX = (x / 3) / r * 2;
        int indexY = (int) ((int) (y / Math.sqrt(3)) / r + indexX / 2);

        //中心点坐标
        int centerX = r + (int) (1.5 * r * indexX);
        int centerY = (int) (Math.sqrt(3) * 0.5 * r * (1 + 2 * indexY - indexX));

        //位于六边外框之外
        if (x > (centerX + r)) {
            if (y > centerY + thickness)//右下
            {
                indexX++;
                indexY++;
            } else if (y < centerY - thickness)//右上
            {
                indexX++;
            } else//中间部分以及外延部分
            {
                indexX = -1;
                indexY = -1;
            }
        } else//六边外框之内
        {
            if (x > centerX + r / 2)//右半区
            {
                if (y > centerY)//右下
                {
                    double M = Math.sqrt(3) / 2 * r + centerY;//上边界点
                    double N = (centerX + r - x) * Math.sqrt(3) + centerY;//六边形边界点
                    int MP = (int) (M - y);
                    int MN = (int) (M - N);
                    if (MP > (MN + thickness * flat))//六边形内部
                    {
                        //索引不变
                    } else if (MP < (MN - thickness * flat))//六边形外部
                    {
                        indexX++;
                        indexY++;
                    } else//交界区
                    {
                        if (MP == MN + thickness * flat)//紧靠内部
                        {
                            transparent = (byte) (transparent / 2);
                            ////去除右侧尖尖
                            if (x == centerX + r - thickness) {
                                indexX = -1;
                                indexY = -1;
                            }
                        } else if (MP == MN - thickness * flat)//紧靠外部
                        {
                            indexX++;
                            indexY++;
                            transparent = (byte) (transparent / 2);
                        } else//中间区
                        {
                            indexX = -1;
                            indexY = -1;
                        }
                    }
                } else//右上
                {
                    double M = centerY - Math.sqrt(3) / 2 * r;
                    double N = centerY - (centerX + r - x) * Math.sqrt(3);
                    int MP = (int) (y - M);
                    int MN = (int) (N - M);
                    if (MP > (MN + thickness * flat))//内部
                    {
                        ////索引不变，但是要去除右侧尖尖
                        if (x == centerX + r - thickness) {
                            indexX = -1;
                            indexY = -1;
                        }
                    } else if (MP < (MN - thickness * flat))//外部,索引单变
                    {
                        indexX++;
                    } else {
                        if (MP == MN + thickness * flat)//里侧
                        {
                            transparent = (byte) (transparent / 2);
                            if (x == centerX + r - thickness - 1)////去除右侧尖尖
                            {
                                indexX = -1;
                                indexY = -1;
                            }
                        } else if (MP == MN - thickness * flat)//外侧
                        {
                            indexX++;
                            transparent = (byte) (transparent / 2);
                        } else {
                            indexX = -1;
                            indexY = -1;
                        }
                    }
                }
            } else if (x < centerX - r / 2)//左半区
            {
                if (y < centerY)//左上
                {
                    double M = centerY - Math.sqrt(3) / 2 * r;
                    double N = centerY + (centerX - r - x) * Math.sqrt(3);
                    int MP = (int) (y - M);
                    int MN = (int) (N - M);
                    if (MP > (MN + thickness * flat)) {
                        //索引不变
                    } else if (MP < (MN - thickness * flat))//索引单变
                    {
                        indexX--;
                        indexY--;
                    } else {
                        if (MP == MN + thickness * flat)//里侧
                        {
                            transparent = (byte) (transparent / 2);
                            if (x == centerX - r + thickness + 1) {
                                indexX = -1;
                                indexY = -1;
                            }
                        } else if (MP == MN - thickness * flat)//外侧
                        {
                            indexX--;
                            indexY--;
                            transparent = (byte) (transparent / 2);
                        } else {
                            indexX = -1;
                            indexY = -1;
                        }
                    }
                } else//左下
                {
                    double M = centerY + Math.sqrt(3) / 2 * r;
                    double N = centerY - (centerX - r - x) * Math.sqrt(3);
                    int MP = (int) (M - y);
                    int MN = (int) (M - N);
                    if (MP > (MN + thickness * flat))//内部
                    {
                        if (x == centerX - r + thickness + 1)//索引不变，但是要清除突兀部分
                        {
                            indexX = -1;
                            indexY = -1;
                        }
                    } else if (MP < (MN - thickness * flat))//索引单变
                    {
                        indexX--;
                    } else//隔离带
                    {
                        if (MP == MN + thickness * flat)//里侧
                        {
                            transparent = (byte) (transparent / 2);
                            if (x < centerX - r + thickness * flat - 1) {
                                indexX = -1;
                                indexY = -1;
                            }
                        } else if (MP == MN - thickness * flat)//外侧
                        {
                            indexX--;
                            transparent = (byte) (transparent / 2);
                        } else {
                            indexX = -1;
                            indexY = -1;
                        }
                    }
                }
            } else//六边形竖条内部
            {
                Random random = new Random((long) Math.sqrt(3) * r);
                int step = random.nextInt();
//                int step = (int) Math.random(Double.valueOf(Math.sqrt(3) * r));
                double remainder = y % step;

                if (remainder <= thickness)//上缓冲
                {
                    indexX = -1;
                    indexY = -1;
                } else if (step - remainder - 1 <= thickness)//下缓冲
                {
                    indexX = -1;
                    indexY = -1;
                }

                if ((remainder - 1 == thickness) || (step - remainder - 2 == thickness))//六边形上下四圆角控制（分为上下半身）
                {
                    //去除四点
                    double first = (int) (centerX - r / 2);
                    double second = (int) Math.ceil((double) centerX - r / 2);
                    int third = (int) (centerX + r / 2);
                    int fourth = (int) Math.ceil((double) centerX + r / 2);

                    if (x == first || x == second || x == third || x == fourth) {
                        indexX = -1;
                        indexY = -1;
                    }
                }
            }
        }

        //right超出界面的设置
        double rightIndex = Math.floor(Double.valueOf(width - r - r) / (3 * Double.valueOf(r) / 2));
        boolean xx = indexX > rightIndex;
        if (indexX > rightIndex) {
            indexX = -1;
            indexY = -1;
        }

        //bottom超出界面的设置
        double bottomIndex = Math.floor(height / (r * Math.sqrt(3))) - 1;
        //基础位置为（0，bottomIndex+1）
        if (indexY > bottomIndex)//&&((indexX-0)%(indexY-(bottomIndex+1))==0))
        {
            //下分三种情况，第一种是首索引
            boolean isOrNotFirstIndex = indexX == 0 && (indexY == bottomIndex + 1);
            //与首索引同行位置的索引
            boolean isOrNotFirstLineIndex = indexX == (indexY - (bottomIndex + 1)) * 2;
            //侧移索引
            boolean isOrNotSideSwayIndex = (indexX + 1) == (indexY - (bottomIndex + 1)) * 2;

            if (isOrNotFirstIndex || isOrNotFirstLineIndex || isOrNotSideSwayIndex) {
                indexX = -1;
                indexY = -1;
            }
        }

        return new int[]{indexX, indexY};
    }


}
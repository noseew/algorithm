/*
 * Copyright: 2016 www.noseew.com Inc. All rights reserved.
 * 注意：本内容仅限于公司内部传阅，禁止外泄以及用于其他的商业目的
 *
 * @Project: labortory
 * @File: BiasHex
 * @Package: org.song.algorithm.hexagon
 * @Date: 2019年02月01日
 * @Author:jiali.song@song.com
 *
 */
package org.song.algorithm.algorithmbase.datatype.geo.hexagon.demo1;

/**
 * @description:
 * @author: jiali.song@song.com
 * @date: 2019年02月01日 18:55:16
 **/
public class BiasHex {

    /* 倾斜坐标轴，采用  lefttop -> rightbottom为x,  top->bottom 为y;
     * 仔细体会下;
     * 						top(1:[0,-1]);
     * lefttop(0:[-1,0])						righttop(2:[1,-1]);
     *						self(-1:[0,0]);
     * leftbottom(5:[-1,1])					rightbottom(3:[1,0]);
     *						bottom(4:[0,1]);
     */
    public static  Position Top = new Position(0, -1);
    public static  Position Bottom = new Position(0, 1);//-Top;
    public static  Position LeftTop = new Position(-1, 0);
    public static  Position LeftBottom = new Position(-1, 1);// + LeftTop + Bottom;
    public static  Position RightTop = new Position(1, -1); //- LeftBottom;
    public static  Position RightBottom = new Position(1, 0);// - LeftTop;
}
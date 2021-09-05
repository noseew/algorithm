/*
 * Copyright: 2016 www.noseew.com Inc. All rights reserved.
 * 注意：本内容仅限于公司内部传阅，禁止外泄以及用于其他的商业目的
 *
 * @Project: labortory
 * @File: Quad
 * @Package: org.song.algorithm.hexagon
 * @Date: 2019年02月01日
 * @Author:jiali.song@song.com
 *
 */
package org.song.algorithm.algorithmbase.datatype.geo.hexagon.demo1;

/**
 * @description:
 * @author: jiali.song@song.com
 * @date: 2019年02月01日 18:55:45
 **/
public class Quad {

    /*
     * lefttop(0)		top(1) 		righttop(2);
     * left(7)			self(-1)	right(3);
     * leftbottom(6)	bottom(5)	rightbottom(4);
     */
    public static  Position Top = new Position(0, -1);
    public static  Position Left = new Position(-1, 0);
    public static  Position Bottom = new Position(0, 1); //- Top;
    public static  Position Right = new Position(1, 0); //- Left;
    public static  Position LeftTop = new Position(-1, -1);//Left + Top;
    public static  Position LeftBottom = new Position(-1, 1);//Left + Bottom;
    public static  Position RightTop = new Position(1, -1);//Right + Top;
    public static  Position RightBottom = new Position(1, 1);//Right + Bottom;
}
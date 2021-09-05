/*
 * Copyright: 2016 www.noseew.com Inc. All rights reserved.
 * 注意：本内容仅限于公司内部传阅，禁止外泄以及用于其他的商业目的
 *
 * @Project: labortory
 * @File: Position
 * @Package: org.song.algorithm.hexagon
 * @Date: 2019年02月01日
 * @Author:jiali.song@song.com
 *
 */
package org.song.algorithm.algorithmbase.datatype.geo.hexagon.demo1;

/**
 * @description:
 * @author: jiali.song@song.com
 * @date: 2019年02月01日 18:57:30
 **/
public class Position {
    private Integer x;
    private Integer y;

    public Position() {
    }

    public Position(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}
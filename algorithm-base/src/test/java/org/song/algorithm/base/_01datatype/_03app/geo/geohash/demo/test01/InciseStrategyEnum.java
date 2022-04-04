package org.song.algorithm.base._01datatype._03app.geo.geohash.demo.test01;

/**
 * 圆(半径)与矩形(GEO块)切割关系
 */
public enum InciseStrategyEnum {
    // 圆 包含 矩形全部
    circle_contain_rectangle,
    // 圆 矩形 无交集
    no_intersection,
    // 圆 包含 矩形中心点
    circle_contain_center,
}

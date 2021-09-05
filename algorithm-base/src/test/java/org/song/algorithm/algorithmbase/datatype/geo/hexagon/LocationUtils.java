/*
 * Copyright: 2016 www.noseew.com Inc. All rights reserved.
 * 注意：本内容仅限于公司内部传阅，禁止外泄以及用于其他的商业目的
 *
 * @Project: labortory
 * @File: Test
 * @Package: org.song.algorithm.hexagon
 * @Date: 2019年02月02日
 * @Author:jiali.song@song.com
 *
 */
package org.song.algorithm.algorithmbase.datatype.geo.hexagon;

import java.math.BigDecimal;

/**
 * @description:
 * @author: jiali.song@song.com
 * @date: 2019年02月02日 12:39:43
 **/
public class LocationUtils {
    private static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 通过经纬度获取距离(单位：米)
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static double getDistance(double lat1, double lng1, double lat2,
                                     double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s * 1000;
        return s;
    }

    public static void main(String[] args) {
        double distance1 = getDistance(0, 0, 36.250914, 121.120858);
        System.out.println(BigDecimal.valueOf(distance1).toPlainString() + "m");
        double distance2 = getDistance(0, 0, 36.654677 , 121.410676);
        System.out.println(BigDecimal.valueOf(distance2).toPlainString() + "m");

        double v = distance2 - distance1;
        System.out.println(v);

        System.out.println(v / 30000);

    }
}
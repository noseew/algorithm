package org.song.algorithm.base._02alg.classical.geo;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @description:
 * @author: jiali.song@song.com
 * @date: 2019年02月02日 12:39:43
 **/
public class LocationUtils {
    /**
     * 地球半径: 6378137 单位米
     * 地球周长: 40075017 单位米
     */
    private static final double EARTH_RADIUS = 6378137.0;

    /**
     * 计算两经纬度点之间的距离（单位：米）
     *
     * @param lng1 经度
     * @param lat1 纬度
     * @param lng2
     * @param lat2
     * @return
     */
    public static double getDistance(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = Math.toRadians(lat1);
        double radLat2 = Math.toRadians(lat2);
        double a = radLat1 - radLat2;
        double b = Math.toRadians(lng1) - Math.toRadians(lng2);
        double s = 2 * Math.asin(
                Math.sqrt(
                        Math.pow(Math.sin(a / 2), 2)
                                + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        // 取WGS84标准参考椭球中的地球长半径(单位:m)
        s = s * EARTH_RADIUS;
        s = new BigDecimal(s * 10000).divide(new BigDecimal(10000), RoundingMode.HALF_UP).doubleValue();
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
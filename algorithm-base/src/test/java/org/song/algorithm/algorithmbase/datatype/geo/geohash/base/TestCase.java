package org.song.algorithm.algorithmbase.datatype.geo.geohash.base;

import org.junit.jupiter.api.Test;

import java.util.List;

public class TestCase {

    /**
     * 获取当前坐标的 GEO hash
     * 
     * GEO在线展示工具
     * https://ryan-miao.gitee.io/GeoHash_base-visualization/show_all.html
     */
    @Test
    public void getGEOHashCodeTest() {
        double lng = 120.741271;
        double lat = 31.27732;
        int precisionLevel = PrecisionLevel.Level7.getCode();

        GeoHash_base place = new GeoHash_base(lat, lng, precisionLevel);
        System.out.println(place.getGeoCodeObj().getGeoCode());
        System.out.println(GeoHash_base.toGeoCode(place).getGeoCode());
        // wttcgtc
    }

    /**
     * 获取周边临近 9/8个 GEO hash
     */
    @Test
    public void getAdjacentTest() {
        double lng = 120.741271;
        double lat = 31.27732;
        int precisionLevel = PrecisionLevel.Level7.getCode();
        GeoHash_base place = new GeoHash_base(lat, lng, precisionLevel);
        GeoHash_base[] adjacent = place.getAdjacent();
        print(adjacent);
        GeoHash_base[] adjacentAndSelf = place.getAdjacentAndSelf();
        print(adjacentAndSelf);
        // wttcgtc,wttcgw0,wttcgw1,wttcgw4,wttcgtf,wttcgtd,wttcgt9,wttcgt8,wttcgtb
    }


    private static void print(GeoHash_base[] adjacentAndSelf) {
        StringBuilder sb = new StringBuilder();
        for (GeoHash_base GeoHash_base : adjacentAndSelf) {
            sb.append(GeoHash_base.getGeoCodeObj().getGeoCode()).append(",");
        }
        System.out.println(sb.substring(0, sb.length() - 1));
    }

    private static void print(List<GeoHash_base> adjacentAndSelf) {
        if (adjacentAndSelf.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (GeoHash_base GeoHash_base : adjacentAndSelf) {
            sb.append(GeoHash_base.getGeoCodeObj().getGeoCode()).append(",");
        }
        System.out.println(sb.substring(0, sb.length() - 1));
    }
}

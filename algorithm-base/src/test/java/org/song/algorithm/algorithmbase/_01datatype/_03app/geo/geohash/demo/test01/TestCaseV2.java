package org.song.algorithm.algorithmbase._01datatype._03app.geo.geohash.demo.test01;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._03app.geo.geohash.base.PrecisionLevel;

import java.util.List;

public class TestCaseV2 {


    @Test
    public void getGEOHashCodeTest() {
        double lng = 120.741271;
        double lat = 31.27732;
        int precisionLevel = PrecisionLevel.Level7.getCode();

        GeoHash_test01 place = GeoHash_test01.withPrecision(lat, lng, precisionLevel);
        System.out.println(place.getGeoCodeObj().getGeoCode());
        System.out.println(GeoHash_test01.toGeoCode(place).getGeoCode());
        // wttcgtc
    }


    /**
     * 指定半径内的 GEO hash 自动适配 精度等级
     */
    @Test
    public void getAdjacent() {
        // 120.730624,31.262825 创意产业园
        // 120.741271,31.27732 文星广场
        double lng = 120.741271;
        double lat = 31.27732;
        int precisionLevel = PrecisionLevel.Level7.getCode();
        long start = System.currentTimeMillis();
        GeoHash_test01 place = GeoHash_test01.withPrecision(lat, lng, precisionLevel);

        GeoHash_test01[] parentAllGeos = place.getAdjacentAndSelf();
        long end = System.currentTimeMillis();
        System.out.println((end - start) + " ms");
        print(parentAllGeos);
        
    }


    /**
     * 指定半径内的 GEO hash 自动适配 精度等级
     */
    @Test
    public void getAdjacentRadiusAdaptTest() {
        // 120.730624,31.262825 创意产业园
        // 120.741271,31.27732 文星广场
        double lng = 120.741271;
        double lat = 31.27732;
        int precisionLevel = PrecisionLevel.Level7.getCode();
        long start = System.currentTimeMillis();
        GeoHash_test01 place = GeoHash_test01.withPrecision(lat, lng, precisionLevel);

        List<GeoHash_test01> parentAllGeos = place.getAdjacentAdapt(5000);
        long end = System.currentTimeMillis();
        System.out.println((end - start) + " ms");
        print(parentAllGeos);
        
    }


    private static void print(GeoHash_test01[] adjacentAndSelf) {
        StringBuilder sb = new StringBuilder();
        for (GeoHash_test01 geoHash : adjacentAndSelf) {
            sb.append(geoHash.getGeoCodeObj().getGeoCode()).append(",");
        }
        System.out.println(sb.substring(0, sb.length() - 1));
    }

    private static void print(List<GeoHash_test01> adjacentAndSelf) {
        if (adjacentAndSelf.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (GeoHash_test01 geoHash : adjacentAndSelf) {
            sb.append(geoHash.getGeoCodeObj().getGeoCode()).append(",");
        }
        System.out.println(sb.substring(0, sb.length() - 1));
    }
}

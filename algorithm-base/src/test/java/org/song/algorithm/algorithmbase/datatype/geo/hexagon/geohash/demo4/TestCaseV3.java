package org.song.algorithm.algorithmbase.datatype.geo.hexagon.geohash.demo4;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase.datatype.geo.hexagon.geohash.demo4.test.GeoHashV3;
import org.song.algorithm.algorithmbase.datatype.geo.hexagon.geohash.demo4.test.PrecisionLevel;

import java.util.List;

public class TestCaseV3 {


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
        GeoHashV3 place = GeoHashV3.withPrecision(lat, lng, precisionLevel);

        System.out.println(place.getGeoCodeObj().toString());

        List<GeoHashV3> parentAllGeos = place.getAdjacentAdapt(5000);
        long end = System.currentTimeMillis();
        System.out.println((end - start) + " ms");
        print(parentAllGeos);
        
    }

    private static void print(GeoHashV3[] adjacentAndSelf) {
        StringBuilder sb = new StringBuilder();
        for (GeoHashV3 geoHash : adjacentAndSelf) {
            sb.append(geoHash.getGeoCodeObj().getGeoCode()).append(",");
        }
        System.out.println(sb.substring(0, sb.length() - 1));
    }

    private static void print(List<GeoHashV3> adjacentAndSelf) {
        if (adjacentAndSelf.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (GeoHashV3 geoHash : adjacentAndSelf) {
            sb.append(geoHash.getGeoCodeObj().getGeoCode()).append(",");
        }
        System.out.println(sb.substring(0, sb.length() - 1));
    }
}

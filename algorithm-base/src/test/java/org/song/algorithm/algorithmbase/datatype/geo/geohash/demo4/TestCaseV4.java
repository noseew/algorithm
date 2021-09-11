package org.song.algorithm.algorithmbase.datatype.geo.geohash.demo4;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase.datatype.geo.geohash.demo4.test.GeoHashV4;
import org.song.algorithm.algorithmbase.datatype.geo.geohash.base.PrecisionLevel;

import java.util.List;

public class TestCaseV4 {


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
        GeoHashV4 place = GeoHashV4.withPrecision(lat, lng, precisionLevel);

        System.out.println(place.getGeoCodeObj().toString());

        List<GeoHashV4> parentAllGeos = place.getAdjacentAdapt(500);
        long end = System.currentTimeMillis();
        System.out.println((end - start) + " ms");
        print(parentAllGeos);
    }

    @Test
    public void getAdaptTest() {
        // 120.730624,31.262825 创意产业园
        // 120.741271,31.27732 文星广场
        double lng = 120.741271;
        double lat = 31.27732;
        int precisionLevel = PrecisionLevel.Level1.getCode();
        GeoHashV4 place = GeoHashV4.withPrecision(lat, lng, precisionLevel);

        System.out.println(place.toBinaryString());
        System.out.println(place.getNorthernNeighbour().toBinaryString());

        long distance = place.getBits() ^ place.getNorthernNeighbour().getBits();
        System.out.println(Long.toBinaryString(distance));
    }

    @Test
    public void getAdaptTest2() {
        // 120.730624,31.262825 创意产业园
        // 120.741271,31.27732 文星广场
        double lng = 120.741271;
        double lat = 31.27732;

        for (PrecisionLevel value : PrecisionLevel.values()) {
            GeoHashV4 place = GeoHashV4.withPrecision(lat, lng, value.getCode());

            System.out.println(place.toBinaryString());
            System.out.println(place.getNorthernNeighbour().toBinaryString());
            long distance = place.getBits() ^ place.getNorthernNeighbour().getBits();
            System.out.println(Long.toBinaryString(distance));
            System.out.println();
        }

    }


    private static void print(GeoHashV4[] adjacentAndSelf) {
        StringBuilder sb = new StringBuilder();
        for (GeoHashV4 geoHash : adjacentAndSelf) {
            sb.append(geoHash.getGeoCodeObj().getGeoCode()).append(",");
        }
        System.out.println(sb.substring(0, sb.length() - 1));
    }

    private static void print(List<GeoHashV4> adjacentAndSelf) {
        if (adjacentAndSelf.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (GeoHashV4 geoHash : adjacentAndSelf) {
            sb.append(geoHash.getGeoCodeObj().getGeoCode()).append(",");
        }
        System.out.println(sb.substring(0, sb.length() - 1));
    }
}

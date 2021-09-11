package org.song.algorithm.algorithmbase.datatype.geo.geohash.demo4;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase.datatype.geo.geohash.demo4.test.GeoHash;
import org.song.algorithm.algorithmbase.datatype.geo.geohash.demo4.test.PrecisionLevel;

import java.util.List;

public class TestCase {

    /**
     * 获取当前坐标的 GEO hash
     */
    @Test
    public void getGEOHashCodeTest() {
        double lng = 120.741271;
        double lat = 31.27732;
        int precisionLevel = PrecisionLevel.Level7.getCode();

        GeoHash place = GeoHash.withPrecision(lat, lng, precisionLevel);
        System.out.println(place.getGeoCodeObj().getGeoCode());
        System.out.println(GeoHash.toGeoCode(place).getGeoCode());
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
        GeoHash place = GeoHash.withPrecision(lat, lng, precisionLevel);
        GeoHash[] adjacent = place.getAdjacent();
        GeoHash[] adjacentAndSelf = place.getAdjacentAndSelf();
        print(adjacentAndSelf);
        // wttcgtc,wttcgw0,wttcgw1,wttcgw4,wttcgtf,wttcgtd,wttcgt9,wttcgt8,wttcgtb
    }

    /**
     * 指定半径内的 GEO hash
     */
    @Test
    public void getAdjacentRadiusTest() {
        double lng = 120.741271;
        double lat = 31.27732;
        int precisionLevel = PrecisionLevel.Level7.getCode();
        long start = System.currentTimeMillis();
        GeoHash place = GeoHash.withPrecision(lat, lng, precisionLevel);
        GeoHash[] adjacentAndSelf = place.getAdjacentAndSelf(500, true, false);

        long end = System.currentTimeMillis();
        System.out.println((end - start) + " ms");
        print(adjacentAndSelf);
        // wttcgqf,wttcgqd,wttcgq6,wttcgq4,wttcgmf,wttcgmd,wttcgm6,wttcgm4,wttcgkf,wttcgke,wttcgkg,wttcgm5,wttcgm7,wttcgme,wttcgmg,wttcgq5,wttcgq7,wttcgqe,wttcgqg,wttcgr5,wttcgrh,wttcgqu,wttcgqs,wttcgqk,wttcgqh,wttcgmu,wttcgms,wttcgmk,wttcgmh,wttcgku,wttcgks,wttcgkk,wttcgkm,wttcgkt,wttcgkv,wttcgmj,wttcgmm,wttcgmt,wttcgmv,wttcgqj,wttcgqm,wttcgqt,wttcgqv,wttcgrj,wttcgrm,wttcgrq,wttcgrn,wttcgqy,wttcgqw,wttcgqq,wttcgqn,wttcgmy,wttcgmw,wttcgmq,wttcgmn,wttcgky,wttcgkw,wttcgkq,wttcgkp,wttcgkr,wttcgkx,wttcgkz,wttcgmp,wttcgmr,wttcgmx,wttcgmz,wttcgqp,wttcgqr,wttcgqx,wttcgqz,wttcgrp,wttcgrr,wttcgx2,wttcgx0,wttcgwb,wttcgw8,wttcgw2,wttcgw0,wttcgtb,wttcgt8,wttcgt2,wttcgt0,wttcgsb,wttcgs8,wttcgs2,wttcgs0,wttcgs1,wttcgs3,wttcgs9,wttcgsc,wttcgt1,wttcgt3,wttcgt9,wttcgtc,wttcgw1,wttcgw3,wttcgw9,wttcgwc,wttcgx1,wttcgx3,wttcgx6,wttcgx4,wttcgwf,wttcgwd,wttcgw6,wttcgw4,wttcgtf,wttcgtd,wttcgt6,wttcgt4,wttcgsf,wttcgsd,wttcgs6,wttcgs4,wttcgs7,wttcgse,wttcgsg,wttcgt5,wttcgt7,wttcgte,wttcgtg,wttcgw5,wttcgw7,wttcgwe,wttcgwg,wttcgx5,wttcgx7,wttcgxk,wttcgxh,wttcgwu,wttcgws,wttcgwk,wttcgwh,wttcgtu,wttcgts,wttcgtk,wttcgth,wttcgsu,wttcgss,wttcgsk,wttcgsm,wttcgst,wttcgsv,wttcgtj,wttcgtm,wttcgtt,wttcgtv,wttcgwj,wttcgwm,wttcgwt,wttcgwv,wttcgxj,wttcgxm,wttcgxn,wttcgwy,wttcgww,wttcgwq,wttcgwn,wttcgty,wttcgtw,wttcgtq,wttcgtn,wttcgsy,wttcgsw,wttcgsz,wttcgtp,wttcgtr,wttcgtx,wttcgtz,wttcgwp,wttcgwr,wttcgwx,wttcgwz,wttcgy8,wttcgy2,wttcgy0,wttcgvb,wttcgv8,wttcgv2,wttcgv0

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
        GeoHash place = GeoHash.withPrecision(lat, lng, precisionLevel);

        List<GeoHash> parentAllGeos = place.getAdjacentAdapt(5000);
        print(parentAllGeos);
        // wttf4e5,wttf4ek,wttf4eh,wttf4ej,wttf4em,wttf4et,wttf4ey,wttf4ew,wttf4eq,wttf4en,wttcdxy,wttcdxx,wttcdxz,wttf4ep,wttf4er,wttf4ex,wttf4ez,wttf4sp,wttcdyu,wttcdyv,wttf4vn,wttcdyy,wttcdyw,wttcdyx,wttcdyz,wttf4vp,wttcejy,wttcejz,wttcemb,wttcemc,wttcemf,wttcemg,wttcemu,wttcems,wttcemt,wttcemv,wttcemy,wttcemw,wttcemx,wttcemz,wttcetb,wttcet8,wttcet9,wttcetc,wttcetf,wttcetd,wttcete,wttcetg,wttcetu,wttcets,wttcett,wttcetv,wttcety,wttcetz,wttcevb,wttcevc,wttcevf,wttcevg,wttfhj2,wttfhj0,wttcsnb,wttcsn8,wttcsn2,wttcsn3,wttcsn9,wttcsnc,wttfhj1,wttfhj3,wttfhj4,wttcsnf,wttcsnd,wttcsne,wttcsng,wttfhj5,wttcsnu,wttcsnv,wttfhk2,wttfhk0,wttcsrb,wttcsr8,wttcsr2,wttcsr3,wttcsr9,wttcsrc,wttfhk1,wttfhk3,wttfhk4,wttcsrf,wttcsrd,wttcsrg,wttfhd8,wttfhd2,wttfhd0,wttfh9b,wttfh98,wttfh92,wttfh90,wttfh91,wttfh93,wttfh99,wttfh9c,wttfhd1,wttfhd3,wttfhd4,wttfh9f,wttfh9d,wttfh96,wttfh94,wttf4d,wttf49,wttf48,wttcdz,wttf4b,wttf4c,wttf4f,wttf4g,wttf4u,wttcep,wttcen,wttceq,wttcer,wttcex,wttcew,wttcey,wttcez,wttfhh,wttfh5,wttfh4,wttfh1,wttfh0,wttcsp,wttfh2,wttfh3,wttfh6,wttfh7,wttfh8,wttcf,wttcg,wttf5,wttcu
    }


    private static void print(GeoHash[] adjacentAndSelf) {
        StringBuilder sb = new StringBuilder();
        for (GeoHash geoHash : adjacentAndSelf) {
            sb.append(geoHash.getGeoCodeObj().getGeoCode()).append(",");
        }
        System.out.println(sb.substring(0, sb.length() - 1));
    }

    private static void print(List<GeoHash> adjacentAndSelf) {
        if (adjacentAndSelf.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (GeoHash geoHash : adjacentAndSelf) {
            sb.append(geoHash.getGeoCodeObj().getGeoCode()).append(",");
        }
        System.out.println(sb.substring(0, sb.length() - 1));
    }
}

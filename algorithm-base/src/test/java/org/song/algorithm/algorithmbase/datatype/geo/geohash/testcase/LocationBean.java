package org.song.algorithm.algorithmbase.datatype.geo.geohash.testcase;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @description: 存储经纬度信息
 * @author: jiali.song@song.com
 * @date: 2019年02月02日 10:45:01
 **/
@Data
@AllArgsConstructor
public class LocationBean {
    public static final double MINLAT = -90;
    public static final double MAXLAT = 90;
    public static final double MINLNG = -180;
    public static final double MAXLNG = 180;
    private double lat;//纬度[-90,90]
    private double lng;//经度[-180,180]

}
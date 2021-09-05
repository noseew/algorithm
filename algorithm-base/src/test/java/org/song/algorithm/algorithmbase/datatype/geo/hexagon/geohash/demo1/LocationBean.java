/*
 * Copyright: 2016 www.noseew.com Inc. All rights reserved.
 * 注意：本内容仅限于公司内部传阅，禁止外泄以及用于其他的商业目的
 *
 * @Project: labortory
 * @File: LocationBean
 * @Package: org.song.algorithm.hexagon.geohash
 * @Date: 2019年02月02日
 * @Author:jiali.song@song.com
 *
 */
package org.song.algorithm.algorithmbase.datatype.geo.hexagon.geohash.demo1;

/**
 * @description: 存储经纬度信息
 * @author: jiali.song@song.com
 * @date: 2019年02月02日 10:45:01
 **/
public class LocationBean {
    public static final double MINLAT = -90;
    public static final double MAXLAT = 90;
    public static final double MINLNG = -180;
    public static final double MAXLNG = 180;
    private double lat;//纬度[-90,90]
    private double lng;//经度[-180,180]

    public LocationBean(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }
    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public double getLng() {
        return lng;
    }
    public void setLng(double lng) {
        this.lng = lng;
    }

}
/*
 * Copyright: 2018 www.noseew.com Inc. All rights reserved.
 * 注意：本内容仅限于公司内部传阅，禁止外泄以及用于其他的商业目的
 *
 * @Project: noseew-custom-carhailing2
 * @File: Point
 * @Package: com.noseew.carhailing.test.geo
 * @Date: 2019年01月29日
 * @Author:gq47193
 *
 */
package org.song.algorithm.algorithmbase.datatype.geo.hexagon.geohash.demo3.geo;

import java.io.Serializable;

/**
 * 坐标点
 *
 * @description:
 * @author: gq47193
 * @date: 2019年01月29日 13:31:32
 **/
public class Coordinate implements Serializable {

    /**
     * 经度
     */
    private double lng;
    /**
     * 纬度
     */
    private double lat;

    public Coordinate(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
        if (Math.abs(lat) > 90 || Math.abs(lng) > 180) {
            throw new IllegalArgumentException(this + "超出了坐标范围：(-90,90)或(-180,180)");
        }
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return String.format("(%s,%s)", lat, lng);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coordinate) {
            Coordinate other = (Coordinate) obj;
            return lat == other.lat && lng == other.lng;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 42;
        long latBits = Double.doubleToLongBits(lat);
        long lonBits = Double.doubleToLongBits(lng);
        result = 31 * result + (int) (latBits ^ (latBits >>> 32));
        result = 31 * result + (int) (lonBits ^ (lonBits >>> 32));
        return result;
    }

}
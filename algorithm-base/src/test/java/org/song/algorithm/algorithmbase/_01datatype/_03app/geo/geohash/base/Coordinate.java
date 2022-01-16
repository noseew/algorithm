package org.song.algorithm.algorithmbase._01datatype._03app.geo.geohash.base;

import java.io.Serializable;

/**
 * 坐标点
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
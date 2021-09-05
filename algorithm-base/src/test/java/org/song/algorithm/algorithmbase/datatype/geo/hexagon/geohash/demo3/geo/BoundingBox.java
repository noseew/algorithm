/*
 * Copyright: 2018 www.noseew.com Inc. All rights reserved.
 * 注意：本内容仅限于公司内部传阅，禁止外泄以及用于其他的商业目的
 *
 * @Project: noseew-custom-carhailing2
 * @File: BoundingBox
 * @Package: com.noseew.carhailing.test.geo
 * @Date: 2019年01月29日
 * @Author:gq47193
 *
 */
package org.song.algorithm.algorithmbase.datatype.geo.hexagon.geohash.demo3.geo;

import java.io.Serializable;

/**
 * （矩形）有界区域
 *
 * @description:
 * @author: gq47193
 * @date: 2019年01月29日 13:17:14
 **/
public class BoundingBox implements Serializable {
    private double minLat;
    private double maxLat;
    private double minLng;
    private double maxLng;

    public BoundingBox(Coordinate p1, Coordinate p2) {
        this(p1.getLat(), p2.getLat(), p1.getLng(), p2.getLng());
    }

    public BoundingBox(double minLat, double maxLat, double minLng, double maxLng) {
        this.minLat = Math.min(minLat, maxLat);
        this.maxLat = Math.max(minLat, maxLat);
        this.minLng = Math.min(minLng, maxLng);
        this.maxLng = Math.max(minLng, maxLng);
    }

    public BoundingBox(BoundingBox that) {
        this(that.minLat, that.maxLat, that.minLng, that.maxLng);
    }

    public Coordinate getUpperLeft() {
        return new Coordinate(maxLat, minLng);
    }

    public Coordinate getLowerRight() {
        return new Coordinate(minLat, maxLng);
    }

    public double getLatitudeSize() {
        return maxLat - minLat;
    }

    public double getLongitudeSize() {
        return maxLng - minLng;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof BoundingBox) {
            BoundingBox that = (BoundingBox) obj;
            return minLat == that.minLat && minLng == that.minLng && maxLat == that.maxLat && maxLng == that.maxLng;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + hashCode(minLat);
        result = 37 * result + hashCode(maxLat);
        result = 37 * result + hashCode(minLng);
        result = 37 * result + hashCode(maxLng);
        return result;
    }

    private static int hashCode(double x) {
        long f = Double.doubleToLongBits(x);
        return (int) (f ^ (f >>> 32));
    }

    public boolean contains(Coordinate point) {
        return (point.getLat() >= minLat) && (point.getLng() >= minLng)
                && (point.getLat() <= maxLat) && (point.getLng() <= maxLng);
    }

    public boolean intersects(BoundingBox other) {
        return !(other.minLng > maxLng || other.maxLng < minLng || other.minLat > maxLat || other.maxLat < minLat);
    }

    @Override
    public String toString() {
        return getUpperLeft() + " -> " + getLowerRight();
    }

    public Coordinate getCenterPoint() {
        double centerLatitude = (minLat + maxLat) / 2;
        double centerLongitude = (minLng + maxLng) / 2;
        return new Coordinate(centerLatitude, centerLongitude);
    }

    public void expandToInclude(BoundingBox other) {
        if (other.minLng < minLng) {
            minLng = other.minLng;
        }
        if (other.maxLng > maxLng) {
            maxLng = other.maxLng;
        }
        if (other.minLat < minLat) {
            minLat = other.minLat;
        }
        if (other.maxLat > maxLat) {
            maxLat = other.maxLat;
        }
    }


    public double getMinLat() {
        return minLat;
    }

    public void setMinLat(double minLat) {
        this.minLat = minLat;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public void setMaxLat(double maxLat) {
        this.maxLat = maxLat;
    }

    public double getMinLng() {
        return minLng;
    }

    public void setMinLng(double minLng) {
        this.minLng = minLng;
    }

    public double getMaxLng() {
        return maxLng;
    }

    public void setMaxLng(double maxLng) {
        this.maxLng = maxLng;
    }
}
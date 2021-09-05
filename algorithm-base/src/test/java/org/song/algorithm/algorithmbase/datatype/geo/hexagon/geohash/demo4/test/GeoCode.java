package org.song.algorithm.algorithmbase.datatype.geo.hexagon.geohash.demo4.test;

import java.io.Serializable;

public class GeoCode implements Serializable {

    /**
     * geo编码
     */
    private String geoCode;
    /**
     * base32格式的编码
     * 转换时，不足两位的字符会前补0
     */
    private String base32Code;

    public GeoCode() {
    }

    public GeoCode(String geoCode, String base32Code) {
        this.geoCode = geoCode;
        this.base32Code = base32Code;
    }

    public String getGeoCode() {
        return geoCode;
    }

    public void setGeoCode(String geoCode) {
        this.geoCode = geoCode;
    }

    public String getBase32Code() {
        return base32Code;
    }

    public void setBase32Code(String base32Code) {
        this.base32Code = base32Code;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", geoCode, base32Code);
    }
}
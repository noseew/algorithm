/*
 * Copyright: 2018 www.noseew.com Inc. All rights reserved.
 * 注意：本内容仅限于公司内部传阅，禁止外泄以及用于其他的商业目的
 *
 * @Project: noseew-custom-carhailing2
 * @File: GeoCode
 * @Package: com.noseew.carhailing.test.geo
 * @Date: 2019年02月01日
 * @Author:gq47193
 *
 */
package org.song.algorithm.algorithmbase.datatype.geo.hexagon.geohash.demo3.geo;

import java.io.Serializable;

/**
 * @description:
 * @author: gq47193
 * @date: 2019年02月01日 10:13:42
 **/
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
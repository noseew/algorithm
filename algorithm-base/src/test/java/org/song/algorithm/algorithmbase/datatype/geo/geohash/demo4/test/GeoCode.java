package org.song.algorithm.algorithmbase.datatype.geo.geohash.demo4.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @Override
    public String toString() {
        return String.format("%s(%s)", geoCode, base32Code);
    }
}
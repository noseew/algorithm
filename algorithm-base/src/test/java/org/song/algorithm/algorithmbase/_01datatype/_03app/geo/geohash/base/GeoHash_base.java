package org.song.algorithm.algorithmbase._01datatype._03app.geo.geohash.base;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Objects;

/**
 * 基础版 Geohash 算法, 采用 base 32 编码, 总共采用64bit存储 geohashcode
 */
public class GeoHash_base implements Serializable {

    private static Logger logger = LoggerFactory.getLogger(GeoHash_base.class);

    private static final long serialVersionUID = 5993704309814832030L;

    /**
     * geohash字符编码数组, base32
     */
    private final static char[] digits = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r',
            's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };
    /**
     * GeoHash算法支持的最长数组长度
     */
    private static final int MAX_BIT_PRECISION = 64;
    /**
     * GeoHash算法支持的最大精度, 即等价于decode之后的区域编码的字符长度
     */
    private static final int MAX_CHARACTER_PRECISION = 12;
    /**
     * 对于base32, 5个长度字节, 对应一个base32字符
     */
    private static final int BASE32_BITS = 5;

    private static final long FIRST_BIT_FLAGGED = 0x8000000000000000L;

    /**
     * 坐标所对应的 GEO Hash Code
     */
    private long bits = 0;

    private byte significantBits = 0;
    /**
     * geo区域边界
     */
    private BoundingBox boundingBox;

    private GeoHash_base() {
    }

    /**
     * 将给定的坐标, 按照给定的精度分装成 GEO 信息
     * 
     * @param latitude         经度
     * @param longitude        纬度
     * @param desiredPrecision 预期精度
     * @return
     */
    private GeoHash_base(double latitude, double longitude, int desiredPrecision) {
        desiredPrecision = Math.min(desiredPrecision, MAX_BIT_PRECISION);

        // 维度范围
        double[] latitudeRange = {-90, 90};
        // 经度范围
        double[] longitudeRange = {-180, 180};

        /*
        经过一系列计算
        1. 最终将经纬度, 按照二分法定位到指定的精度, 同时包含用户传入的坐标
            也就是按照指定的分级标准, 将用户坐标所在的格子坐标范围找出来, 赋值给 latitudeRange, longitudeRange
        2. 将每一级的精度的计算结果(0/1)记录下来, 也就是最终的GEO_HASH_CODE
         */
        boolean isEvenBit = true;
        while (significantBits < desiredPrecision) {
            // 经纬度, 交替计算
            if (isEvenBit) {
                // 计算经度
                divideRangeEncode(longitude, longitudeRange);
            } else {
                // 计算纬度
                divideRangeEncode(latitude, latitudeRange);
            }
            isEvenBit = !isEvenBit;
        }

        // 将定位好的4个坐标点封装成对象
        setBoundingBox(this, latitudeRange, longitudeRange);
        // 将坐标左移位取整, 右边补0
        this.bits <<= (MAX_BIT_PRECISION - desiredPrecision);
    }

    /**
     * (按指定精度)对坐标计算geohash
     *
     * @param latitude       纬度
     * @param longitude      经度
     * @param precisionLevel 级数(或精度，取值范围：1~12)
     * @return
     */
    public static GeoHash_base withPrecision(double latitude, double longitude, int precisionLevel) {
        if (precisionLevel > MAX_CHARACTER_PRECISION) {
            throw new IllegalArgumentException("A geohash can only be " + MAX_CHARACTER_PRECISION + " character long.");
        }
        if (Math.abs(latitude) > 90.0 || Math.abs(longitude) > 180.0) {
            throw new IllegalArgumentException("Can't have lat/lon values out of (-90,90)/(-180/180)");
        }
        int desiredPrecision = Math.min(precisionLevel * BASE32_BITS, 60);
        return new GeoHash_base(latitude, longitude, desiredPrecision);
    }

    /**
     * 给定4个坐标点, 将其转成方格对象
     * 
     * @param hash
     * @param latitudeRange
     * @param longitudeRange
     */
    private void setBoundingBox(GeoHash_base hash, double[] latitudeRange, double[] longitudeRange) {
        hash.boundingBox = new BoundingBox(
                new Coordinate(latitudeRange[0], longitudeRange[0]),
                new Coordinate(latitudeRange[1], longitudeRange[1]));
    }

    private BoundingBox getBoundingBox() {
        return boundingBox;
    }

    /**
     * 计算坐标点的GEO范围, 并保存GEO信息
     * 
     * @param value
     * @param range
     */
    private void divideRangeEncode(double value, double[] range) {
        // 二分法, 将经纬度 / 2, 取到中间分割线, 并判断用户传入的经纬度属于分割线的哪边, 并替换传入的数据中两个值
        double mid = (range[0] + range[1]) / 2;
        if (value >= mid) {
            // 当前为设置为1, 并记录下来
            addOnBitToEnd();
            range[0] = mid;
        } else {
            // 当前为设置为0, 并记录下来
            addOffBitToEnd();
            range[1] = mid;
        }
    }

    private static void divideRangeDecode(GeoHash_base hash, double[] range, boolean b) {
        double mid = (range[0] + range[1]) / 2;
        if (b) {
            hash.addOnBitToEnd();
            range[0] = mid;
        } else {
            hash.addOffBitToEnd();
            range[1] = mid;
        }
    }

    /**
     * returns the 8 adjacent hashes for this one. They are in the following
     * order:<br>
     * SELF, NW, N, NE, E, SE, S, SW, W
     */
    public GeoHash_base[] getAdjacentAndSelf() {
        GeoHash_base northern = getNorthernNeighbour();
        GeoHash_base eastern = getEasternNeighbour();
        GeoHash_base southern = getSouthernNeighbour();
        GeoHash_base western = getWesternNeighbour();
        return new GeoHash_base[]{
                this,
                northern.getWesternNeighbour(),
                northern,
                northern.getEasternNeighbour(),
                eastern,
                southern.getEasternNeighbour(),
                southern,
                southern.getWesternNeighbour(),
                western};
    }

    /**
     * returns the 8 adjacent hashes for this one. They are in the following
     * order:<br>
     * NW, N, NE, E, SE, S, SW, W
     */
    public GeoHash_base[] getAdjacent() {
        GeoHash_base northern = getNorthernNeighbour();
        GeoHash_base eastern = getEasternNeighbour();
        GeoHash_base southern = getSouthernNeighbour();
        GeoHash_base western = getWesternNeighbour();
        return new GeoHash_base[]{
                northern.getWesternNeighbour(),
                northern,
                northern.getEasternNeighbour(),
                eastern,
                southern.getEasternNeighbour(),
                southern,
                southern.getWesternNeighbour(),
                western};
    }

    private GeoHash_base recombineLatLonBitsToHash(long[] latBits, long[] lonBits) {
        GeoHash_base hash = new GeoHash_base();
        boolean isEvenBit = false;
        latBits[0] <<= (MAX_BIT_PRECISION - latBits[1]);
        lonBits[0] <<= (MAX_BIT_PRECISION - lonBits[1]);
        double[] latitudeRange = {-90.0, 90.0};
        double[] longitudeRange = {-180.0, 180.0};

        for (int i = 0; i < latBits[1] + lonBits[1]; i++) {
            if (isEvenBit) {
                divideRangeDecode(hash, latitudeRange, (latBits[0] & FIRST_BIT_FLAGGED) == FIRST_BIT_FLAGGED);
                latBits[0] <<= 1;
            } else {
                divideRangeDecode(hash, longitudeRange, (lonBits[0] & FIRST_BIT_FLAGGED) == FIRST_BIT_FLAGGED);
                lonBits[0] <<= 1;
            }
            isEvenBit = !isEvenBit;
        }
        setBoundingBox(hash, latitudeRange, longitudeRange);
        hash.bits <<= (MAX_BIT_PRECISION - hash.significantBits);
        return hash;
    }

    /**
     * 上(北)
     *
     * @return
     */
    private GeoHash_base getNorthernNeighbour() {
        long[] latitudeBits = getRightAlignedLatitudeBits();
        long[] longitudeBits = getRightAlignedLongitudeBits();
        latitudeBits[0] += 1;
        latitudeBits[0] = maskLastNBits(latitudeBits[0], latitudeBits[1]);
        return recombineLatLonBitsToHash(latitudeBits, longitudeBits);
    }

    /**
     * 下(南)
     *
     * @return
     */
    private GeoHash_base getSouthernNeighbour() {
        long[] latitudeBits = getRightAlignedLatitudeBits();
        long[] longitudeBits = getRightAlignedLongitudeBits();
        latitudeBits[0] -= 1;
        latitudeBits[0] = maskLastNBits(latitudeBits[0], latitudeBits[1]);
        return recombineLatLonBitsToHash(latitudeBits, longitudeBits);
    }

    /**
     * 右(东)
     *
     * @return
     */
    private GeoHash_base getEasternNeighbour() {
        long[] latitudeBits = getRightAlignedLatitudeBits();
        long[] longitudeBits = getRightAlignedLongitudeBits();
        longitudeBits[0] += 1;
        longitudeBits[0] = maskLastNBits(longitudeBits[0], longitudeBits[1]);
        return recombineLatLonBitsToHash(latitudeBits, longitudeBits);
    }

    /**
     * 左(西)
     *
     * @return
     */
    private GeoHash_base getWesternNeighbour() {
        long[] latitudeBits = getRightAlignedLatitudeBits();
        long[] longitudeBits = getRightAlignedLongitudeBits();
        longitudeBits[0] -= 1;
        longitudeBits[0] = maskLastNBits(longitudeBits[0], longitudeBits[1]);
        return recombineLatLonBitsToHash(latitudeBits, longitudeBits);
    }

    private long[] getRightAlignedLatitudeBits() {
        long copyOfBits = bits << 1;
        long value = extractEverySecondBit(copyOfBits, getNumberOfLatLonBits()[0]);
        return new long[]{value, getNumberOfLatLonBits()[0]};
    }

    private long[] getRightAlignedLongitudeBits() {
        long copyOfBits = bits;
        long value = extractEverySecondBit(copyOfBits, getNumberOfLatLonBits()[1]);
        return new long[]{value, getNumberOfLatLonBits()[1]};
    }

    private long extractEverySecondBit(long copyOfBits, int numberOfBits) {
        long value = 0;
        for (int i = 0; i < numberOfBits; i++) {
            if ((copyOfBits & FIRST_BIT_FLAGGED) == FIRST_BIT_FLAGGED) {
                value |= 0x1;
            }
            value <<= 1;
            copyOfBits <<= 2;
        }
        value >>>= 1;
        return value;
    }

    private int[] getNumberOfLatLonBits() {
        if (significantBits % 2 == 0) {
            return new int[]{significantBits / 2, significantBits / 2};
        } else {
            return new int[]{significantBits / 2, significantBits / 2 + 1};
        }
    }

    private void addOnBitToEnd() {
        // 将GEO bit位左移一位, 右边补1(当前位设置成1)
        significantBits++;
        bits <<= 1;
        bits = bits | 0x1;
    }

    private void addOffBitToEnd() {
        // 将GEO bit位左移一位, 右边补0(当前位设置成0)
        significantBits++;
        bits <<= 1;
    }

    private long maskLastNBits(long value, long n) {
        long mask = 0xffffffffffffffffL;
        mask >>>= (MAX_BIT_PRECISION - n);
        return value & mask;
    }

    /**
     * get the base32 string for this {@link GeoHash_base}.<br>
     * this method only makes sense, if this hash has a multiple of 5
     * significant bits.
     *
     * @throws IllegalStateException when the number of significant bits is not a multiple of 5.
     */
    public static GeoCode toGeoCode(GeoHash_base geoHash) {
        if (geoHash.significantBits % 5 != 0) {
            throw new IllegalStateException("Cannot convert a geohash to base32 if the precision is not a multiple of 5.");
        }

        long firstFiveBitsMask = 0xf800000000000000L;
        long bitsCopy = geoHash.bits;
        int partialChunks = (int) Math.ceil(((double) geoHash.significantBits / 5));

        StringBuilder geoCode = new StringBuilder();
        StringBuilder base32Code = new StringBuilder();
        for (int i = 0; i < partialChunks; i++) {
            int pointer = (int) ((bitsCopy & firstFiveBitsMask) >>> 59);
            base32Code.append(String.format("%02d", pointer));
            geoCode.append(GeoHash_base.digits[pointer]);
            bitsCopy <<= 5;
        }
        return new GeoCode(geoCode.toString(), base32Code.toString());
    }

    /**
     * get the base32 string for this {@link GeoHash_base}.<br>
     * this method only makes sense, if this hash has a multiple of 5
     * significant bits.
     *
     * @throws IllegalStateException when the number of significant bits is not a multiple of 5.
     */
    public GeoCode getGeoCodeObj() {
        if (this.significantBits % 5 != 0) {
            throw new IllegalStateException("Cannot convert a geohash to base32 if the precision is not a multiple of 5.");
        }

        long firstFiveBitsMask = 0xf800000000000000L;
        long bitsCopy = this.bits;
        int partialChunks = (int) Math.ceil(((double) this.significantBits / 5));

        StringBuilder geoCode = new StringBuilder();
        StringBuilder base32Code = new StringBuilder();
        for (int i = 0; i < partialChunks; i++) {
            int pointer = (int) ((bitsCopy & firstFiveBitsMask) >>> 59);
            base32Code.append(String.format("%02d", pointer));
            geoCode.append(GeoHash_base.digits[pointer]);
            bitsCopy <<= 5;
        }
        return new GeoCode(geoCode.toString(), base32Code.toString());
    }

    @Override
    public String toString() {
        if (significantBits % BASE32_BITS == 0) {
            return String.format("%s -> %s -> center:%s - > %s", Long.toBinaryString(bits), boundingBox, boundingBox.getCenterPoint(), toGeoCode(this));
        } else {
            return String.format("%s -> %s -> center:%s, bits: %d", Long.toBinaryString(bits), boundingBox, boundingBox.getCenterPoint(), significantBits);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GeoHash_base)) {
            return false;
        }
        GeoHash_base geoHash = (GeoHash_base) o;
        return Objects.equals(geoHash.getGeoCodeObj().getGeoCode(), this.getGeoCodeObj().getGeoCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBoundingBox());
    }
}
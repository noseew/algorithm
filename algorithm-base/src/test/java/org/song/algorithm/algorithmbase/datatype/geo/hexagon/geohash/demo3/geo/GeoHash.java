/*
 * Copyright: 2018 www.noseew.com Inc. All rights reserved.
 * 注意：本内容仅限于公司内部传阅，禁止外泄以及用于其他的商业目的
 *
 * @Project: noseew-custom-carhailing2
 * @File: GeoHash
 * @Package: com.noseew.carhailing.test.geo
 * @Date: 2019年01月29日
 * @Author:gq47193
 *
 */
package org.song.algorithm.algorithmbase.datatype.geo.hexagon.geohash.demo3.geo;

/**
 * @description:
 * @author: gq47193
 * @date: 2019年01月29日 13:14:30
 **/
public class GeoHash {

    /**
     * geohash字符编码数组
     */
    public final static char[] digits = {
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
     * GeoHash算法支持的最大精度，即等价于decode之后的区域编码的字符长度
     */
    private static final int MAX_CHARACTER_PRECISION = 12;
    /**
     * 对于base32，5个长度字节，对应一个base32字符
     */
    private static final int BASE32_BITS = 5;
    public static final long FIRST_BIT_FLAGGED = 0x8000000000000000L;

    protected long bits = 0;
    protected byte significantBits = 0;
    /**
     * geo区域边界
     */
    private BoundingBox boundingBox;

    protected GeoHash() {
    }

    /**
     * @param latitude         经度
     * @param longitude        纬度
     * @param desiredPrecision 预期精度
     * @return
     */
    private GeoHash(double latitude, double longitude, int desiredPrecision) {
        desiredPrecision = Math.min(desiredPrecision, MAX_BIT_PRECISION);

        boolean isEvenBit = true;
        double[] latitudeRange = {-90, 90};
        double[] longitudeRange = {-180, 180};

        while (significantBits < desiredPrecision) {
            if (isEvenBit) {
                divideRangeEncode(longitude, longitudeRange);
            } else {
                divideRangeEncode(latitude, latitudeRange);
            }
            isEvenBit = !isEvenBit;
        }

        setBoundingBox(this, latitudeRange, longitudeRange);
        this.bits <<= (MAX_BIT_PRECISION - desiredPrecision);
    }


    /**
     * （按指定精度）对坐标计算geohash
     *
     * @param latitude       纬度
     * @param longitude      经度
     * @param precisionLevel 级数（或精度，取值范围：1~12）
     * @return
     */
    public static GeoHash withPrecision(double latitude, double longitude, int precisionLevel) {
        if (precisionLevel > MAX_CHARACTER_PRECISION) {
            throw new IllegalArgumentException("A geohash can only be " + MAX_CHARACTER_PRECISION + " character long.");
        }
        if (Math.abs(latitude) > 90.0 || Math.abs(longitude) > 180.0) {
            throw new IllegalArgumentException("Can't have lat/lon values out of (-90,90)/(-180/180)");
        }
        int desiredPrecision = (precisionLevel * BASE32_BITS <= 60) ? precisionLevel * BASE32_BITS : 60;
        return new GeoHash(latitude, longitude, desiredPrecision);
    }

    private static void setBoundingBox(GeoHash hash, double[] latitudeRange, double[] longitudeRange) {
        hash.boundingBox = new BoundingBox(
                new Coordinate(latitudeRange[0], longitudeRange[0]),
                new Coordinate(latitudeRange[1], longitudeRange[1]));
    }

    private void divideRangeEncode(double value, double[] range) {
        double mid = (range[0] + range[1]) / 2;
        if (value >= mid) {
            addOnBitToEnd();
            range[0] = mid;
        } else {
            addOffBitToEnd();
            range[1] = mid;
        }
    }

    private static void divideRangeDecode(GeoHash hash, double[] range, boolean b) {
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
     * N, NE, E, SE, S, SW, W, NW
     */
    public GeoHash[] getAdjacent() {
        GeoHash northern = getNorthernNeighbour();
        GeoHash eastern = getEasternNeighbour();
        GeoHash southern = getSouthernNeighbour();
        GeoHash western = getWesternNeighbour();
        return new GeoHash[]{
                northern.getWesternNeighbour(),
                northern,
                northern.getEasternNeighbour(),
                eastern,
                southern.getEasternNeighbour(),
                southern,
                southern.getWesternNeighbour(),
                western};
    }

    protected GeoHash recombineLatLonBitsToHash(long[] latBits, long[] lonBits) {
        GeoHash hash = new GeoHash();
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

    public GeoHash getNorthernNeighbour() {
        long[] latitudeBits = getRightAlignedLatitudeBits();
        long[] longitudeBits = getRightAlignedLongitudeBits();
        latitudeBits[0] += 1;
        latitudeBits[0] = maskLastNBits(latitudeBits[0], latitudeBits[1]);
        return recombineLatLonBitsToHash(latitudeBits, longitudeBits);
    }

    public GeoHash getSouthernNeighbour() {
        long[] latitudeBits = getRightAlignedLatitudeBits();
        long[] longitudeBits = getRightAlignedLongitudeBits();
        latitudeBits[0] -= 1;
        latitudeBits[0] = maskLastNBits(latitudeBits[0], latitudeBits[1]);
        return recombineLatLonBitsToHash(latitudeBits, longitudeBits);
    }

    public GeoHash getEasternNeighbour() {
        long[] latitudeBits = getRightAlignedLatitudeBits();
        long[] longitudeBits = getRightAlignedLongitudeBits();
        longitudeBits[0] += 1;
        longitudeBits[0] = maskLastNBits(longitudeBits[0], longitudeBits[1]);
        return recombineLatLonBitsToHash(latitudeBits, longitudeBits);
    }

    public GeoHash getWesternNeighbour() {
        long[] latitudeBits = getRightAlignedLatitudeBits();
        long[] longitudeBits = getRightAlignedLongitudeBits();
        longitudeBits[0] -= 1;
        longitudeBits[0] = maskLastNBits(longitudeBits[0], longitudeBits[1]);
        return recombineLatLonBitsToHash(latitudeBits, longitudeBits);
    }

    protected long[] getRightAlignedLatitudeBits() {
        long copyOfBits = bits << 1;
        long value = extractEverySecondBit(copyOfBits, getNumberOfLatLonBits()[0]);
        return new long[]{value, getNumberOfLatLonBits()[0]};
    }

    protected long[] getRightAlignedLongitudeBits() {
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

    protected int[] getNumberOfLatLonBits() {
        if (significantBits % 2 == 0) {
            return new int[]{significantBits / 2, significantBits / 2};
        } else {
            return new int[]{significantBits / 2, significantBits / 2 + 1};
        }
    }

    protected final void addOnBitToEnd() {
        significantBits++;
        bits <<= 1;
        bits = bits | 0x1;
    }

    protected final void addOffBitToEnd() {
        significantBits++;
        bits <<= 1;
    }

    private long maskLastNBits(long value, long n) {
        long mask = 0xffffffffffffffffL;
        mask >>>= (MAX_BIT_PRECISION - n);
        return value & mask;
    }

    /**
     * get the base32 string for this {@link GeoHash}.<br>
     * this method only makes sense, if this hash has a multiple of 5
     * significant bits.
     *
     * @throws IllegalStateException when the number of significant bits is not a multiple of 5.
     */
    public static GeoCode toGeoCode(GeoHash geoHash) {
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
            geoCode.append(GeoHash.digits[pointer]);
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
}
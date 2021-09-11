package org.song.algorithm.algorithmbase.datatype.geo.geohash.demo4.test;


import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.song.algorithm.algorithmbase.datatype.geo.LocationUtils;
import org.song.algorithm.algorithmbase.datatype.geo.geohash.base.BoundingBox;
import org.song.algorithm.algorithmbase.datatype.geo.geohash.base.Coordinate;
import org.song.algorithm.algorithmbase.datatype.geo.geohash.base.GeoCode;
import org.song.algorithm.algorithmbase.datatype.geo.geohash.base.PrecisionLevel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GeoHash implements Serializable {

    private static Logger logger = LoggerFactory.getLogger(GeoHash.class);

    private static final long serialVersionUID = 5993704309814832030L;

    /**
     * geohash字符编码数组
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
     * GeoHash算法支持的最大精度，即等价于decode之后的区域编码的字符长度
     */
    private static final int MAX_CHARACTER_PRECISION = 12;
    /**
     * 对于base32，5个长度字节，对应一个base32字符
     */
    private static final int BASE32_BITS = 5;

    private static final long FIRST_BIT_FLAGGED = 0x8000000000000000L;

    private long bits = 0;

    private byte significantBits = 0;
    /**
     * geo区域边界
     */
    private BoundingBox boundingBox;
    /**
     * 初始精度
     */
    private PrecisionLevel initLevel;
    /**
     * 当前精度
     */
    private PrecisionLevel level;
    /**
     * 定位精度
     */
    private double longitude;
    /**
     * 定位纬度
     */
    private double latitude;
    /**
     * 上级
     */
    private List<GeoHash> parents;

    private GeoHash() {
    }

    /**
     * @param latitude         经度
     * @param longitude        纬度
     * @param desiredPrecision 预期精度
     * @return
     */
    private GeoHash(double latitude, double longitude, int desiredPrecision) {
        desiredPrecision = Math.min(desiredPrecision, MAX_BIT_PRECISION);
        this.latitude = latitude;
        this.longitude = longitude;

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
        int desiredPrecision = Math.min(precisionLevel * BASE32_BITS, 60);
        GeoHash geoHash = new GeoHash(latitude, longitude, desiredPrecision);
        geoHash.level = PrecisionLevel.getByLevelCode(precisionLevel);
        if (geoHash.initLevel == null) {
            geoHash.initLevel = PrecisionLevel.getByLevelCode(precisionLevel);
        }
        withParent(geoHash);
        return geoHash;
    }

    private static void withParent(GeoHash geoHash) {

        geoHash.parents = Lists.newArrayList(geoHash);
        for (int i = geoHash.level.getCode(); i > PrecisionLevel.Level1.getCode(); i--) {
            int parentLevelCode = i - 1;
            int desiredPrecision = Math.min(parentLevelCode * BASE32_BITS, 60);

            GeoHash parentGeoHash = new GeoHash(geoHash.latitude, geoHash.longitude, desiredPrecision);
            parentGeoHash.initLevel = geoHash.initLevel;
            parentGeoHash.level = PrecisionLevel.getByLevelCode(parentLevelCode);
            parentGeoHash.parents = geoHash.parents;

            geoHash.parents.add(parentGeoHash);
        }
    }

    private void setBoundingBox(GeoHash hash, double[] latitudeRange, double[] longitudeRange) {
        hash.boundingBox = new BoundingBox(
                new Coordinate(latitudeRange[0], longitudeRange[0]),
                new Coordinate(latitudeRange[1], longitudeRange[1]));
    }

    private BoundingBox getBoundingBox() {
        return boundingBox;
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


    private GeoHash recombineLatLonBitsToHash(long[] latBits, long[] lonBits) {
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

    /**
     * 上(北)
     *
     * @return
     */
    private GeoHash getNorthernNeighbour() {
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
    private GeoHash getSouthernNeighbour() {
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
    private GeoHash getEasternNeighbour() {
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
    private GeoHash getWesternNeighbour() {
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

    private final void addOnBitToEnd() {
        significantBits++;
        bits <<= 1;
        bits = bits | 0x1;
    }

    private final void addOffBitToEnd() {
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

    /**
     * get the base32 string for this {@link GeoHash}.<br>
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

    public String toBinaryString() {
        return Long.toBinaryString(bits);
    }

    public long getBits() {
        return bits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GeoHash)) {
            return false;
        }
        GeoHash geoHash = (GeoHash) o;
        return Objects.equals(geoHash.getGeoCodeObj().getGeoCode(), this.getGeoCodeObj().getGeoCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBoundingBox());
    }

    public List<GeoHash> getParents() {
        return parents;
    }

    /**
     * 生成矩阵, 以左上角为原点
     * 过滤已存在 GEO 块
     *
     * @param side
     * @param originalPoint
     * @param parentFilterList
     * @return
     */
    private List<GeoHash> generateMatrix(int side, GeoHash originalPoint, List<GeoHash> parentFilterList) {
        List<GeoHash> allGeo = new ArrayList<>(side * side);
        GeoHash current = originalPoint;
        for (int x = 0; x < side; x++) {
            allGeo.add(current);
            for (int y = 0; y < side - 1; y++) {
                if (x % 2 == 0) {
                    // 向下走
                    current = current.getSouthernNeighbour();
                } else {
                    // 向上走
                    current = current.getNorthernNeighbour();
                }

                final GeoHash temp = current;
                boolean contain = parentFilterList.parallelStream().anyMatch(e -> contain(e.boundingBox, temp.boundingBox));
                if (!contain) {
                    allGeo.add(current);
                }
            }
            // 向右走
            current = current.getEasternNeighbour();
        }
        return allGeo;
    }

    /**
     * 获取坐标原点(左上角)
     *
     * @param side
     * @return
     */
    private GeoHash getOriginalPoint(int side) {
        GeoHash originalPoint = this;
        for (int step = 0; step < side - 1; step++) {
            if (step % 2 == 0) {
                // 向上走
                originalPoint = originalPoint.getNorthernNeighbour();
            } else {
                // 向左走
                originalPoint = originalPoint.getWesternNeighbour();
            }
        }
        return originalPoint;
    }

    /**
     * 获取层数(覆盖半径的最小层数), 从1开始
     *
     * @param minUnitSide    单元格最小边长
     * @param distanceRadius 半径距离范围
     * @return
     */
    private static int getCircle(int minUnitSide, int distanceRadius) {
        return 1 + (int) Math.ceil((distanceRadius - (minUnitSide >> 1)) / (double) minUnitSide);
    }

    /**
     * 获取指定距离内 自适应的 GEO
     * 由大向小遍历, 对象数量较少, 自动限制最小级数
     *
     * @param distanceRadius
     * @return
     */
    public List<GeoHash> getAdjacentAdapt(int distanceRadius) {
        InciseStrategyEnum inciseStrategyEnum;

        // 限制跨级最大3, 超过3数量会指数增长, 效率指数降低
        int limitLevel = 3;

        List<GeoHash> parentList = Lists.newArrayList();
        for (int i = this.parents.size() - 1; i >= 0 && limitLevel >= 0; i--) {
            GeoHash current = this.parents.get(i);
            if (distanceRadius > Math.min(current.level.getHeight(), current.level.getWidth())) {
                limitLevel--;
                if (initLevel.equals(current.level)) {
                    // 初始外层使用 no_intersection
                    inciseStrategyEnum = InciseStrategyEnum.no_intersection;
                } else {
                    // 上级使用 circle_contain_center
                    inciseStrategyEnum = InciseStrategyEnum.circle_contain_center;
                }
                List<GeoHash> geoHashes = current.getRadiusAround(distanceRadius, inciseStrategyEnum, parentList);
                logger.info("level {}, 数量 {}", current.level, geoHashes.size());
                parentList.addAll(geoHashes);
            }
        }
        return parentList;
    }

    /**
     * 获取指定半径内的 GEO
     *
     * @param distanceRadius
     * @param inciseStrategyEnum
     * @return
     */
    private List<GeoHash> getRadiusAround(int distanceRadius, InciseStrategyEnum inciseStrategyEnum, List<GeoHash> parentFilterList) {

        int circle = getCircle((int) Math.min(this.level.getWidth(), this.level.getHeight()), distanceRadius);
        int side = (circle << 1) - 1;
        logger.debug("层数circle:{}, 边长side:{}, 半径Radius:{}", circle, side, distanceRadius);

        GeoHash originalPoint = getOriginalPoint(side);
        logger.debug("中心点: {} 坐标原点: {}", this.getGeoCodeObj().getGeoCode(), originalPoint.getGeoCodeObj().getGeoCode());

        List<GeoHash> allGeo = generateMatrix(side, originalPoint, parentFilterList);
        logger.debug("矩形矩阵数量 = {}", allGeo.size());
        allGeo.removeIf(e -> isCircleBoxStrategy(e.getBoundingBox(), distanceRadius, inciseStrategyEnum));
        return allGeo;
    }

    private boolean isCircleBoxStrategy(BoundingBox box, int distanceRadius, InciseStrategyEnum strategyEnum) {
        switch (strategyEnum) {
            case circle_contain_rectangle:
                return isExcircleCircle(box, distanceRadius);
            case circle_contain_center:
                return isContainCenter(box, distanceRadius);
            case no_intersection:
                return isInscribe(box, distanceRadius);
            default:
                return isInscribe(box, distanceRadius);

        }
    }

    private boolean isExcircleCircle(BoundingBox box, int distanceRadius) {
        Coordinate upperLeft = box.getUpperLeft();
        if (LocationUtils.getDistance(upperLeft.getLng(), upperLeft.getLat(), this.longitude, this.latitude) > distanceRadius) {
            return false;
        }
        Coordinate upperRight = box.getUpperRight();
        if (LocationUtils.getDistance(upperRight.getLng(), upperRight.getLat(), this.longitude, this.latitude) > distanceRadius) {
            return false;
        }
        Coordinate lowerLeft = box.getLowerLeft();
        if (LocationUtils.getDistance(lowerLeft.getLng(), lowerLeft.getLat(), this.longitude, this.latitude) > distanceRadius) {
            return false;
        }
        Coordinate lowerRight = box.getLowerRight();
        if (LocationUtils.getDistance(lowerRight.getLng(), lowerRight.getLat(), this.longitude, this.latitude) > distanceRadius) {
            return false;
        }
        return true;
    }

    private boolean isContainCenter(BoundingBox box, int distanceRadius) {
        Coordinate centerPoint = box.getCenterPoint();
        if (LocationUtils.getDistance(centerPoint.getLng(), centerPoint.getLat(), this.longitude, this.latitude) > distanceRadius) {
            return true;
        }
        return false;
    }

    private boolean isInscribe(BoundingBox box, int distanceRadius) {
        Coordinate upperLeft = box.getUpperLeft();
        if (LocationUtils.getDistance(upperLeft.getLng(), upperLeft.getLat(), this.longitude, this.latitude) < distanceRadius) {
            return false;
        }
        Coordinate upperRight = box.getUpperRight();
        if (LocationUtils.getDistance(upperRight.getLng(), upperRight.getLat(), this.longitude, this.latitude) < distanceRadius) {
            return false;
        }
        Coordinate lowerLeft = box.getLowerLeft();
        if (LocationUtils.getDistance(lowerLeft.getLng(), lowerLeft.getLat(), this.longitude, this.latitude) < distanceRadius) {
            return false;
        }
        Coordinate lowerRight = box.getLowerRight();
        if (LocationUtils.getDistance(lowerRight.getLng(), lowerRight.getLat(), this.longitude, this.latitude) < distanceRadius) {
            return false;
        }
        return true;
    }

    private static boolean contain(BoundingBox box1, BoundingBox box2) {
        List<Coordinate> box2Coordinates = Arrays.asList(box2.getLowerRight(), box2.getLowerLeft(), box2.getUpperLeft(), box2.getUpperRight());
        for (Coordinate c2 : box2Coordinates) {
            boolean inside = isInside(box1.getMinLng(), box1.getMaxLat(), box1.getMaxLng(), box1.getMinLat(),
                    c2.getLat(), c2.getLng());
            if (!inside) {
                return false;
            }
        }
        return true;
    }

    private static boolean isInside(double left, double upper, double right, double below,
                                    double pointY, double pointX) {
        // x 经度, y 纬度
        if (pointX < left) {
            //在矩形左侧
            return false;
        }
        if (pointX > right) {
            //在矩形右侧
            return false;
        }
        if (pointY > upper) {
            //在矩形上侧
            return false;
        }
        if (pointY < below) {
            //在矩形下侧
            return false;
        }
        return true;
    }

}
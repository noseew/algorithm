package org.song.algorithm.algorithmbase.datatype.geo.geohash.base.part;

import org.junit.jupiter.api.Test;

/**
 * 组成 GEO hash 的具体部分算法
 * 摘自: https://github.com/yinqiwen/geohash-int
 * 源码是C语言, 这里转成java
 * 
 * 主要是GEO 普通编码和快速编码的区别
 */
public class PartAlg {

    /**
     * 速度对比, java中速度相差10倍
     * 采用二分法的 GEO hash 编解码
     * 采用优化后快速的 GEO hash 编解码
     */
    @Test
    public void test1() {
        GeoHashBits hash = new GeoHashBits(), fast_hash = new GeoHashBits();

        GeoHashRange lat_range = new GeoHashRange(), lon_range = new GeoHashRange();
        // 地球 半周长 常量
        lat_range.max = 20037726.37;
        lat_range.min = -20037726.37;
        lon_range.max = 20037726.37;
        lon_range.min = -20037726.37;

        // 具体坐标点(以米为单位)
        double latitude = 9741705.20;
        double longitude = 5417390.90;

        int loop = 10000000;
        int i = 0;
        long start = System.currentTimeMillis();
        for (i = 0; i < loop; i++) {
            geohashEncode(lat_range, lon_range, latitude, longitude, 24, hash);
        }

        long end = System.currentTimeMillis();
        System.out.println(String.format("Cost %sms to encode\n", end - start));

        start = System.currentTimeMillis();
        for (i = 0; i < loop; i++) {
            geohashFastEncode(lat_range, lon_range, latitude, longitude, 24, fast_hash);
        }
        end = System.currentTimeMillis();
        System.out.println(String.format("Cost %sms to fast encode\n", end - start));

        GeoHashArea area = new GeoHashArea(), area1 = new GeoHashArea();
        start = System.currentTimeMillis();
        for (i = 0; i < loop; i++) {
            geohashDecode(lat_range, lon_range, hash, area);
        }
        end = System.currentTimeMillis();
        System.out.println(String.format("Cost %sms to  decode\n", end - start));

        start = System.currentTimeMillis();
        for (i = 0; i < loop; i++) {
            geohashFastDecode(lat_range, lon_range, hash, area1);
        }
        end = System.currentTimeMillis();
        System.out.println(String.format("Cost %sms to fast decode\n", end - start));

    }

    @Test
    public void test2() {
        GeoHashBits hash = new GeoHashBits(), fast_hash = new GeoHashBits();
        GeoHashNeighbors neighbors = new GeoHashNeighbors();

        GeoHashRange lat_range = new GeoHashRange(), lon_range = new GeoHashRange();
        lat_range.max = 20037726.37;
        lat_range.min = -20037726.37;
        lon_range.max = 20037726.37;
        lon_range.min = -20037726.37;
        double radius = 5000;
        double latitude = 9741705.20;
        double longitude = 5417390.90;
        GeoHashBits hash_min = new GeoHashBits(), 
                hash_max = new GeoHashBits(), 
                hash_lt = new GeoHashBits(), 
                hash_gr = new GeoHashBits();
        
        geohashEncode(lat_range, lon_range, latitude, longitude, 24, hash);
        geohashFastEncode(lat_range, lon_range, latitude, longitude, 24, fast_hash);
        geohashEncode(lat_range, lon_range, latitude - radius, longitude - radius, 13, hash_min);
        geohashEncode(lat_range, lon_range, latitude + radius, longitude + radius, 13, hash_max);
        geohashEncode(lat_range, lon_range, latitude + radius, longitude - radius, 13, hash_lt);
        geohashEncode(lat_range, lon_range, latitude - radius, longitude + radius, 13, hash_gr);

        System.out.println(String.format("## %s\n", hash.bits));
        System.out.println(String.format("## %s\n", fast_hash.bits));
        geohashGetNeighbors(hash, neighbors);
        System.out.println(String.format("%s\n", hash.bits));
        System.out.println(String.format("%s\n", neighbors.east.bits));
        System.out.println(String.format("%s\n", neighbors.west.bits));
        System.out.println(String.format("%s\n", neighbors.south.bits));
        System.out.println(String.format("%s\n", neighbors.north.bits));
        System.out.println(String.format("%s\n", neighbors.north_west.bits));
        System.out.println(String.format("%s\n", neighbors.north_east.bits));
        System.out.println(String.format("%s\n", neighbors.south_east.bits));
        System.out.println(String.format("%s\n", neighbors.south_west.bits));

        System.out.println(String.format("##%s\n", hash_min.bits));
        System.out.println(String.format("##%s\n", hash_max.bits));
        System.out.println(String.format("##%s\n", hash_lt.bits));
        System.out.println(String.format("##%s\n", hash_gr.bits));

        //    geohash_encode(&lat_range, &lon_range, 9741705.20, 5417390.90, 13, &hash);
        //    System.out.println(String.format("from %s to %s \n", hash.bits << 2, (hash.bits+1) << 2);

        GeoHashArea area = new GeoHashArea(), area1 = new GeoHashArea();
        geohashDecode(lat_range, lon_range, hash, area);
        geohashFastDecode(lat_range, lon_range, hash, area1);
        System.out.println(String.format("%.10f %.10f\n", area.latitude.min, area.latitude.max));
        System.out.println(String.format("%.10f %.10f\n", area.longitude.min, area.longitude.max));
        System.out.println(String.format("%.10f %.10f\n", area1.latitude.min, area1.latitude.max));
        System.out.println(String.format("%.10f %.10f\n", area1.longitude.min, area1.longitude.max));

    }

    @Test
    public void test3() {
        GeoHashBits hash = new GeoHashBits(), fast_hash = new GeoHashBits();

        GeoHashRange lat_range = new GeoHashRange(), lon_range = new GeoHashRange();
        lat_range.max = 90.0;
        lat_range.min = -90.0;
        lon_range.max = 180.0;
        lon_range.min = -180.0;

        double latitude = 12.34;
        double longitude = 56.78;

        for (int step = 1; step <= 32; step++) {
            geohashEncode(lat_range, lon_range, latitude, longitude, step, hash);
            geohashFastEncode(lat_range, lon_range, latitude, longitude, step, fast_hash);

            System.out.println(String.format("normal encode (%d): %s\n", step, hash.bits));
            System.out.println(String.format("fast encode   (%d): %s\n", step, fast_hash.bits));
            System.out.println(String.format("\n"));
        }
    }

    /**
     * 将经纬度位置, 进行二分编码
     * 注意经纬度并不是按照角度传入, 而是按照距离, 单位米
     * <p>
     * 0:success
     * -1:failed
     *
     * @param lat_range 纬度距离范围, 单位米
     * @param lon_range 精度距离范围, 单位米
     * @param latitude  坐标纬度, 单位米
     * @param longitude 坐标纬度, 单位米
     * @param step      计算的步进, 精度
     * @param hash      计算的结果记录
     * @return
     */
    public int geohashEncode(GeoHashRange lat_range, GeoHashRange lon_range,
                             double latitude, double longitude, int step, GeoHashBits hash) {
        if (null == hash || step > 32 || step == 0) {
            return -1;
        }
        hash.bits = 0;
        hash.step = step;
        int i = 0;
        if (latitude < lat_range.min || latitude > lat_range.max
                || longitude < lon_range.min || longitude > lon_range.max) {
            return -1;
        }

        // 循环步进/2, 计算次数
        for (; i < step; i++) {
            int lat_bit, lon_bit;
            // 如果落入左边, 则标记0, 右边则标记1
            if (lat_range.max - latitude >= latitude - lat_range.min) {
                lat_bit = 0;
                lat_range.max = (lat_range.max + lat_range.min) / 2;
            } else {
                lat_bit = 1;
                lat_range.min = (lat_range.max + lat_range.min) / 2;
            }
            // 如果落入下边, 则标记0, 上边则标记1
            if (lon_range.max - longitude >= longitude - lon_range.min) {
                lon_bit = 0;
                lon_range.max = (lon_range.max + lon_range.min) / 2;
            } else {
                lon_bit = 1;
                lon_range.min = (lon_range.max + lon_range.min) / 2;
            }
            // bit编码交替记录经纬度的编码, 并向左移动
            hash.bits <<= 1;
            hash.bits += lon_bit;
            hash.bits <<= 1;
            hash.bits += lat_bit;
        }
        return 0;
    }

    /**
     * 将二分编码进行二分解码成经纬度位置
     * 注意经纬度并不是按照角度, 而是按照距离, 单位米
     *
     * 0:success
     * -1:failed
     *
     * @param lat_range 纬度距离范围, 单位米
     * @param lon_range 精度距离范围, 单位米
     * @param hash 已经计算的结果记录
     * @param area 将结果解码成区域
     * @return
     */
    public int geohashDecode(GeoHashRange lat_range, GeoHashRange lon_range,
                             GeoHashBits hash, GeoHashArea area) {
        if (null == area) {
            return -1;
        }
        // 编码结果
        area.hash = hash;
        int i = 0;
        area.latitude.min = lat_range.min;
        area.latitude.max = lat_range.max;
        area.longitude.min = lon_range.min;
        area.longitude.max = lon_range.max;

        // 按照步进, 循环*2
        for (; i < hash.step; i++) {
            // 获取指定位置的编码, 0/1
            int lon_bit = getBit(hash.bits, (hash.step - i) * 2 - 1);
            int lat_bit = getBit(hash.bits, (hash.step - i) * 2 - 2);

            // 纬度: 不断地获取二分结果, 并判断属于哪个区域, 最终将区域赋值给 area
            if (lat_bit == 0) {
                area.latitude.max = (area.latitude.max + area.latitude.min) / 2;
            } else {
                area.latitude.min = (area.latitude.max + area.latitude.min) / 2;
            }
            // 精度: 不断地获取二分结果, 并判断属于哪个区域, 最终将区域赋值给 area
            if (lon_bit == 0) {
                area.longitude.max = (area.longitude.max + area.longitude.min) / 2;
            } else {
                area.longitude.min = (area.longitude.max + area.longitude.min) / 2;
            }
        }
        return 0;
    }

    /**
     * 将经纬度位置, 进行二分编码
     * 注意经纬度并不是按照角度传入, 而是按照距离, 单位米
     *
     * 快速版本
     *
     * Fast encode/decode version, more magic in implementation.
     *
     * @param lat_range 纬度距离范围, 单位米
     * @param lon_range 精度距离范围, 单位米
     * @param latitude 坐标纬度, 单位米
     * @param longitude 坐标纬度, 单位米
     * @param step 计算的步进, 精度
     * @param hash 计算的结果记录
     * @return
     */
    public int geohashFastEncode(GeoHashRange lat_range, GeoHashRange lon_range, double latitude,
                                 double longitude, int step, GeoHashBits hash) {
        if (null == hash || step > 32 || step == 0) {
            return -1;
        }
        hash.bits = 0;
        hash.step = step;
        if (latitude < lat_range.min || latitude > lat_range.max
                || longitude < lon_range.min || longitude > lon_range.max) {
            return -1;
        }

        /*
        纬度为例
            1. (当前坐标 - 最小值)/(最大值-最小值) 得到 当前坐标在范围中的具体比例, 经度偏移量 lon_offset
            2. 1左移步进 得到 步进的2次幂数, 
            3. 经度偏移量*次幂数 得到 将比例偏移量放大具体倍数
            4. 经度偏移量取整 得到 最终的经度结果
        
        快速版公式
            lonCode = (cLon-lonMin)/(lonMax-lonMin)
            lonCode = lonCode*2^step
            
        普通版采用循环step计算, 效率O(step), 快速版本直接采用浮点数相乘, 直接得到指定位置, 效率O(1)
        
        该算法计算范围内的geohash位置的morton代码, 使用以下代码可以更有效地完成
         */
        // The algorithm computes the morton code for the geohash location within
        // the range this can be done MUCH more efficiently using the following code

        // 计算范围为0-1的坐标
        // compute the coordinate in the range 0-1
        double lat_offset = (latitude - lat_range.min) / (lat_range.max - lat_range.min);
        double lon_offset = (longitude - lon_range.min) / (lon_range.max - lon_range.min);

        // 根据步长将其转换为定点
        // convert it to fixed point based on the step size
        lat_offset *= (1L << step);
        lon_offset *= (1L << step);

        int ilato = (int) lat_offset;
        int ilono = (int) lon_offset;

        // 交错位创建莫顿代码. 没有分支, 没有边界
        // interleave the bits to create the morton code.  No branching and no bounding
        hash.bits = interleave(ilato, ilono);
        return 0;
    }

    /**
     * 将二分编码进行二分解码成经纬度位置
     * 注意经纬度并不是按照角度, 而是按照距离, 单位米
     * 
     * 快速版本
     * 
     * Fast encode/decode version, more magic in implementation.
     *
     * @param lat_range 纬度距离范围, 单位米
     * @param lon_range 精度距离范围, 单位米
     * @param hash 已经计算的结果记录
     * @param area 将结果解码成区域
     * @return
     */
    public int geohashFastDecode(GeoHashRange lat_range, GeoHashRange lon_range, GeoHashBits hash, GeoHashArea area) {
        if (null == area) {
            return -1;
        }
        area.hash = hash;
        int step = hash.step;
        long xyhilo = deinterleave(hash.bits); //decode morton code

        double lat_scale = lat_range.max - lat_range.min;
        double lon_scale = lon_range.max - lon_range.min;

        int ilato = (int) xyhilo;        //get back the original integer coordinates
        int ilono = (int) (xyhilo >> 32);

        // double lat_offset=ilato;
        // double lon_offset=ilono;
        // lat_offset /= (1<<step);
        // lon_offset /= (1<<step);

        /*
        ldexp调用将整数转换为双精度数, 然后除以2**步得到0-1坐标, 然后将其乘以scale并加到最小值得到绝对坐标
         */
        // the ldexp call converts the integer to a double,then divides by 2**step to get the 0-1 coordinate, which is then multiplied times scale and added to the min to get the absolute coordinate
//    area.latitude.min = lat_range.min + ldexp(ilato, -step) * lat_scale;
//    area.latitude.max = lat_range.min + ldexp(ilato + 1, -step) * lat_scale;
//    area.longitude.min = lon_range.min + ldexp(ilono, -step) * lon_scale;
//    area.longitude.max = lon_range.min + ldexp(ilono + 1, -step) * lon_scale;

        /*
        比ldexp快得多
        much faster than 'ldexp'
         */
        area.latitude.min = lat_range.min + (ilato * 1.0 / (1 << step)) * lat_scale;
        area.latitude.max = lat_range.min + ((ilato + 1) * 1.0 / (1 << step)) * lat_scale;
        area.longitude.min = lon_range.min + (ilono * 1.0 / (1 << step)) * lon_scale;
        area.longitude.max = lon_range.min + ((ilono + 1) * 1.0 / (1 << step)) * lon_scale;

        return 0;
    }

    /**
     * 
     * @param hash
     * @param d
     * @return
     */
    public int geohashMoveX(GeoHashBits hash, int d) {
        if (d == 0) {
            return 0;
        }
        long x = hash.bits & 0xaaaaaaaaaaaaaaaaL; // 10101010_10101010
        long y = hash.bits & 0x5555555555555555L; // 01010101_01010101

        long zz = 0x5555555555555555L >> (64 - hash.step * 2);
        if (d > 0) {
            x = x + (zz + 1);
        } else {
            x = x | zz;
            x = x - (zz + 1);
        }
        x &= (0xaaaaaaaaaaaaaaaaL >> (64 - hash.step * 2));
        hash.bits = (x | y);
        return 0;
    }

    public int geohashMoveY(GeoHashBits hash, int d) {
        if (d == 0) {
            return 0;
        }
        long x = hash.bits & 0xaaaaaaaaaaaaaaaaL;
        long y = hash.bits & 0x5555555555555555L;

        long zz = 0xaaaaaaaaaaaaaaaaL >> (64 - hash.step * 2);
        if (d > 0) {
            y = y + (zz + 1);
        } else {
            y = y | zz;
            y = y - (zz + 1);
        }
        y &= (0x5555555555555555L >> (64 - hash.step * 2));
        hash.bits = (x | y);
        return 0;
    }

    /**
     * 将两个32位的数交错成64位的数
     *
     * @param xlo
     * @param ylo
     * @return
     */
    private long interleave(int xlo, int ylo) {
        long B[] = {0x5555555555555555L,
                0x3333333333333333L,
                0x0F0F0F0F0F0F0F0FL,
                0x00FF00FF00FF00FFL,
                0x0000FFFF0000FFFFL};
        int S[] = {1, 2, 4, 8, 16};

        long x = xlo; // Interleave lower  bits of x and y, so the bits of x
        long y = ylo; // are in the even positions and bits from y in the odd; //https://graphics.stanford.edu/~seander/bithacks.html#InterleaveBMN

        // x and y must initially be less than 2**32.

        x = (x | (x << S[4])) & B[4];
        y = (y | (y << S[4])) & B[4];

        x = (x | (x << S[3])) & B[3];
        y = (y | (y << S[3])) & B[3];

        x = (x | (x << S[2])) & B[2];
        y = (y | (y << S[2])) & B[2];

        x = (x | (x << S[1])) & B[1];
        y = (y | (y << S[1])) & B[1];

        x = (x | (x << S[0])) & B[0];
        y = (y | (y << S[0])) & B[0];

        return x | (y << 1);
    }

    /**
     * 将一个已经交错的数拆开, 并拼装返回
     * 拼成 x|y 返回
     *
     * @param interleaved
     * @return
     */
    private long deinterleave(long interleaved) {
        long B[] = {0x5555555555555555L,
                0x3333333333333333L,
                0x0F0F0F0F0F0F0F0FL,
                0x00FF00FF00FF00FFL,
                0x0000FFFF0000FFFFL,
                0x00000000FFFFFFFFL};
        int S[] = {0, 1, 2, 4, 8, 16};

        long x = interleaved; ///reverse the interleave process (http://stackoverflow.com/questions/4909263/how-to-efficiently-de-interleave-bits-inverse-morton)
        long y = interleaved >> 1;

        x = (x | (x >>> S[0])) & B[0];
        y = (y | (y >>> S[0])) & B[0];

        x = (x | (x >>> S[1])) & B[1];
        y = (y | (y >>> S[1])) & B[1];

        x = (x | (x >>> S[2])) & B[2];
        y = (y | (y >>> S[2])) & B[2];

        x = (x | (x >>> S[3])) & B[3];
        y = (y | (y >>> S[3])) & B[3];

        x = (x | (x >>> S[4])) & B[4];
        y = (y | (y >>> S[4])) & B[4];

        x = (x | (x >>> S[5])) & B[5];
        y = (y | (y >>> S[5])) & B[5];

        return x | (y << 32);
    }

    public int geohashGetNeighbors(GeoHashBits hash, GeoHashNeighbors neighbors) {
        geohashGetNeighbor(hash, GeoDirection.GEOHASH_NORTH, neighbors.north);
        geohashGetNeighbor(hash, GeoDirection.GEOHASH_EAST, neighbors.east);
        geohashGetNeighbor(hash, GeoDirection.GEOHASH_WEST, neighbors.west);
        geohashGetNeighbor(hash, GeoDirection.GEOHASH_SOUTH, neighbors.south);
        geohashGetNeighbor(hash, GeoDirection.GEOHASH_SOUTH_WEST, neighbors.south_west);
        geohashGetNeighbor(hash, GeoDirection.GEOHASH_SOUTH_EAST, neighbors.south_east);
        geohashGetNeighbor(hash, GeoDirection.GEOHASH_NORT_WEST, neighbors.north_west);
        geohashGetNeighbor(hash, GeoDirection.GEOHASH_NORT_EAST, neighbors.north_east);
        return 0;
    }

    public int geohashGetNeighbor(GeoHashBits hash, GeoDirection direction, GeoHashBits neighbor) {
        if (null == neighbor) {
            return -1;
        }
        neighbor = hash;
        switch (direction) {
            case GEOHASH_NORTH: {
                // 上北
                geohashMoveX(neighbor, 0);
                geohashMoveY(neighbor, 1);
                break;
            }
            case GEOHASH_SOUTH: {
                // 下南
                geohashMoveX(neighbor, 0);
                geohashMoveY(neighbor, -1);
                break;
            }
            case GEOHASH_EAST: {
                // 左西
                geohashMoveX(neighbor, 1);
                geohashMoveY(neighbor, 0);
                break;
            }
            case GEOHASH_WEST: {
                // 右东
                geohashMoveX(neighbor, -1);
                geohashMoveY(neighbor, 0);
                break;
            }
            case GEOHASH_SOUTH_WEST: {
                // 东南
                geohashMoveX(neighbor, -1);
                geohashMoveY(neighbor, -1);
                break;
            }
            case GEOHASH_SOUTH_EAST: {
                // 西南
                geohashMoveX(neighbor, 1);
                geohashMoveY(neighbor, -1);
                break;
            }
            case GEOHASH_NORT_WEST: {
                // 西北
                geohashMoveX(neighbor, -1);
                geohashMoveY(neighbor, 1);
                break;
            }
            case GEOHASH_NORT_EAST: {
                // 东北
                geohashMoveX(neighbor, 1);
                geohashMoveY(neighbor, 1);
                break;
            }
            default: {
                return -1;
            }
        }
        return 0;
    }

    public GeoHashBits geohashNextLeftbottom(GeoHashBits bits) {
        GeoHashBits newbits = GeoHashBits.copy(bits);
        newbits.step++;
        newbits.bits <<= 2;
        return newbits;
    }

    public GeoHashBits geohashNextRightbottom(GeoHashBits bits) {
        GeoHashBits newbits = GeoHashBits.copy(bits);
        newbits.step++;
        newbits.bits <<= 2;
        newbits.bits += 2;
        return newbits;
    }

    public GeoHashBits geohashNextLefttop(GeoHashBits bits) {
        GeoHashBits newbits = GeoHashBits.copy(bits);
        newbits.step++;
        newbits.bits <<= 2;
        newbits.bits += 1;
        return newbits;
    }

    public GeoHashBits geohashNextRighttop(GeoHashBits bits) {
        GeoHashBits newbits = GeoHashBits.copy(bits);
        newbits.step++;
        newbits.bits <<= 2;
        newbits.bits += 3;
        return newbits;
    }

    public static int getBit(long bits, int pos) {
        return (int) ((bits >>> pos) & 0x01);
    }

    enum GeoDirection {
        GEOHASH_NORTH, // = 0,
        GEOHASH_EAST,
        GEOHASH_WEST,
        GEOHASH_SOUTH,
        GEOHASH_SOUTH_WEST,
        GEOHASH_SOUTH_EAST,
        GEOHASH_NORT_WEST,
        GEOHASH_NORT_EAST,
        ;
    }

    static class GeoHashBits {
        // 编码的bit
        long bits;
        // 计算步进(精度/次数)
        int step;

        public static GeoHashBits copy(GeoHashBits bits) {
            GeoHashBits newBits = new GeoHashBits();
            newBits.bits = bits.bits;
            newBits.step = bits.step;
            return newBits;
        }
    }

    // 经纬度距离的范围, 单位米
    static class GeoHashRange {
        double max;
        double min;
    }

    static class GeoHashArea {
        GeoHashBits hash = new GeoHashBits();
        GeoHashRange latitude = new GeoHashRange();
        GeoHashRange longitude = new GeoHashRange();
    }

    static class GeoHashNeighbors {
        GeoHashBits north = new GeoHashBits();
        GeoHashBits east = new GeoHashBits();
        GeoHashBits west = new GeoHashBits();
        GeoHashBits south = new GeoHashBits();
        GeoHashBits north_east = new GeoHashBits();
        GeoHashBits south_east = new GeoHashBits();
        GeoHashBits north_west = new GeoHashBits();
        GeoHashBits south_west = new GeoHashBits();
    }
}

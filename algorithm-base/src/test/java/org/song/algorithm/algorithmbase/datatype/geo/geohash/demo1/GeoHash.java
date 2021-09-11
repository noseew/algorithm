package org.song.algorithm.algorithmbase.datatype.geo.geohash.demo1;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @description:
 * @author: jiali.song@song.com
 * @date: 2019年02月02日 10:45:45
 **/
public class GeoHash {
    private LocationBean location;
    /**
     * 1 2500km;
     * 2 630km;
     * 3 78km;
     * 4 30km
     * 5 2.4km; 
     * 6 610m; 
     * 7 76m; 
     * 8 19m
     */
    private int hashLength = 8; //经纬度转化为geohash长度
    private int latLength = 20; //纬度转化为二进制长度
    private int lngLength = 20; //经度转化为二进制长度

    private double minLat;//每格纬度的单位大小
    private double minLng;//每个经度的倒下
    private static final char[] CHARS = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n',
            'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    /**
     * 在GeoHash实例化时，我们需要对一些属性进行赋值：
     *
     * @param lat
     * @param lng
     */
    public GeoHash(double lat, double lng) {
        location = new LocationBean(lat, lng);
        setMinLatLng();
    }

    public int gethashLength() {
        return hashLength;
    }

    /**
     * @Author:lulei
     * @Description: 设置经纬度的最小单位
     */
    private void setMinLatLng() {
        minLat = LocationBean.MAXLAT - LocationBean.MINLAT;
        for (int i = 0; i < latLength; i++) {
            minLat /= 2.0;
        }
        minLng = LocationBean.MAXLNG - LocationBean.MINLNG;
        for (int i = 0; i < lngLength; i++) {
            minLng /= 2.0;
        }
    }

    /**
     * 我们在使用GeoHash的时候，需要设置最终编码的长度，因此编写一个方法实现对GeoHash长度的设置
     *
     * @param length
     * @return
     */
    public boolean sethashLength(int length) {
        if (length < 1) {
            return false;
        }
        hashLength = length;
        latLength = (length * 5) / 2;
        if (length % 2 == 0) {
            lngLength = latLength;
        } else {
            lngLength = latLength + 1;
        }
        setMinLatLng();
        return true;
    }

    /**
     * 有了这些设置之后，我们需要将经度、纬度转化为对应的二进制编码
     *
     * @param value
     * @param min
     * @param max
     * @param length
     * @return
     */
    private boolean[] getHashArray(double value, double min, double max, int length) {
        if (value < min || value > max) {
            return null;
        }
        if (length < 1) {
            return null;
        }
        boolean[] result = new boolean[length];
        for (int i = 0; i < length; i++) {
            double mid = (min + max) / 2.0;
            if (value > mid) {
                result[i] = true;
                min = mid;
            } else {
                result[i] = false;
                max = mid;
            }
        }
        return result;
    }

    /**
     * 分别获取经纬度的二进制编码后，我们需要将两个二进制字符串合并成一个, 合并经纬度二进制
     *
     * @param latArray
     * @param lngArray
     * @return
     */
    private boolean[] merge(boolean[] latArray, boolean[] lngArray) {
        if (latArray == null || lngArray == null) {
            return null;
        }
        boolean[] result = new boolean[lngArray.length + latArray.length];
        Arrays.fill(result, false);
        for (int i = 0; i < lngArray.length; i++) {
            result[2 * i] = lngArray[i];
        }
        for (int i = 0; i < latArray.length; i++) {
            result[2 * i + 1] = latArray[i];
        }
        return result;
    }

    private String getGeoHashBase32(double lat, double lng) {
        boolean[] bools = getGeoBinary(lat, lng);
        if (bools == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bools.length; i = i + 5) {
            boolean[] base32 = new boolean[5];
            System.arraycopy(bools, i + 0, base32, 0, 5);
            char cha = getBase32Char(base32);
            if (' ' == cha) {
                return null;
            }
            sb.append(cha);
        }
        return sb.toString();
    }

    /**
     * @param base32
     * @return
     * @Author:lulei
     * @Description: 将五位二进制转化为base32
     */
    private char getBase32Char(boolean[] base32) {
        if (base32 == null || base32.length != 5) {
            return ' ';
        }
        int num = 0;
        for (boolean bool : base32) {
            num <<= 1;
            if (bool) {
                num += 1;
            }
        }
        return CHARS[num % CHARS.length];
    }

    /**
     * 对于如何获取周围八个区域的GeoHash值这个问题我们可以做如下转化，我们已经知道当前点的经纬度值，
     * 我们也知道每一个区域内的经度、纬度的宽度，如果经度加上或减去这个宽度，我们就可以位于该区域左侧和右侧区域的经度，
     * 如果纬度加上或减去这个宽度，我们就可以获取该区域上部和下部的纬度，这样我们就可以分别获取到该区域周围八个区域内的一个点的坐标，
     * 我们分别计算这八个点的坐标，也就是八个区域对应的GeoHash编码。
     * <p>
     * 求所在坐标点及周围点组成的九个
     *
     * @return
     */
    public List<String> getGeoHashBase32For9() {
        double leftLat = location.getLat() - minLat;
        double rightLat = location.getLat() + minLat;
        double upLng = location.getLng() - minLng;
        double downLng = location.getLng() + minLng;
        List<String> base32For9 = new ArrayList<String>();
        //左侧从上到下 3个
        String leftUp = getGeoHashBase32(leftLat, upLng);
        if (!(leftUp == null || "".equals(leftUp))) {
            base32For9.add(leftUp);
        }
        String leftMid = getGeoHashBase32(leftLat, location.getLng());
        if (!(leftMid == null || "".equals(leftMid))) {
            base32For9.add(leftMid);
        }
        String leftDown = getGeoHashBase32(leftLat, downLng);
        if (!(leftDown == null || "".equals(leftDown))) {
            base32For9.add(leftDown);
        }
        //中间从上到下 3个
        String midUp = getGeoHashBase32(location.getLat(), upLng);
        if (!(midUp == null || "".equals(midUp))) {
            base32For9.add(midUp);
        }
        String midMid = getGeoHashBase32(location.getLat(), location.getLng());
        if (!(midMid == null || "".equals(midMid))) {
            base32For9.add(midMid);
        }
        String midDown = getGeoHashBase32(location.getLat(), downLng);
        if (!(midDown == null || "".equals(midDown))) {
            base32For9.add(midDown);
        }
        //右侧从上到下 3个
        String rightUp = getGeoHashBase32(rightLat, upLng);
        if (!(rightUp == null || "".equals(rightUp))) {
            base32For9.add(rightUp);
        }
        String rightMid = getGeoHashBase32(rightLat, location.getLng());
        if (!(rightMid == null || "".equals(rightMid))) {
            base32For9.add(rightMid);
        }
        String rightDown = getGeoHashBase32(rightLat, downLng);
        if (!(rightDown == null || "".equals(rightDown))) {
            base32For9.add(rightDown);
        }
        return base32For9;
    }

    /**
     * @return
     * @Author:lulei
     * @Description: 获取经纬度的base32字符串
     */
    public String getGeoHashBase32() {
        return getGeoHashBase32(location.getLat(), location.getLng());
    }


    /**
     * @param lat
     * @param lng
     * @return
     * @Author:lulei
     * @Description: 获取坐标的geo二进制字符串
     */
    private boolean[] getGeoBinary(double lat, double lng) {
        boolean[] latArray = getHashArray(lat, LocationBean.MINLAT, LocationBean.MAXLAT, latLength);
        boolean[] lngArray = getHashArray(lng, LocationBean.MINLNG, LocationBean.MAXLNG, lngLength);
        return merge(latArray, lngArray);
    }


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        long time = System.currentTimeMillis();
        GeoHash g = new GeoHash(40.222012, 116.248283);
        System.out.println(BigDecimal.valueOf(g.minLat).toPlainString());
        System.out.println(g.hashLength);
        System.out.println(g.getGeoHashBase32());
        System.out.println(JSONObject.toJSONString(g.getGeoHashBase32For9()));
        System.out.println(System.currentTimeMillis() - time);
    }
}
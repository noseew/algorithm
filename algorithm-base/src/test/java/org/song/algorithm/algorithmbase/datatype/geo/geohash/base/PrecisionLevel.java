package org.song.algorithm.algorithmbase.datatype.geo.geohash.base;

public enum PrecisionLevel {
    Level1(1, 5009400, 4992600),
    Level2(2, 1252300, 624100),
    Level3(3, 156500, 156500),
    Level4(4, 39100, 19500),
    Level5(5, 4900, 4900),
    Level6(6, 1200, 609.4),
    Level7(7, 152.9, 152.4),
    Level8(8, 38.2, 19),
    Level9(9, 4.8, 4.8),
    Level10(10, 1.2, 0.60),
    Level11(11, 0.15, 0.15),
    Level12(12, 0.037, 0.019),
    ;

    PrecisionLevel(int code, double width, double height) {
        this.code = code;
        this.width = width;
        this.height = height;
    }

    /**
     * 等级
     */
    private int code;
    /**
     * 宽度, 经度间隔米
     */
    private double width;
    /**
     * 高度, 纬度间隔米
     */
    private double height;

    public static PrecisionLevel getByLevelCode(int code) {
        for (PrecisionLevel value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
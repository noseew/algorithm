package org.song.encription.basedemo.utils.aes;

/**
 * 加解密工具类
 */
public class Encryption {

    /**
     * 应用于 消息体
     */
    private static final String KEY_HTTP = "EF290D911DD34E8E";
    private static final byte[] IV_HTTP = new byte[]{0x13, 0x33, 0x5D, 0x7F, 0x52, 0x29, 0x2C, 0x15, 0x3B, 0x51, 0x55, 0x23, 0x4F, 0x19, 0x36, 0x3D};

    /**
     * 应用于 密码
     */
    private static final String KEY_PASSWORD = "j7Eb526F0^_^0g4F";
    private static final byte[] IV_PASSWORD = new byte[]{0x13, 0x33, 0x5D, 0x7F, 0x52, 0x29, 0x2C, 0x15, 0x3B, 0x51, 0x55, 0x23, 0x4F, 0x19, 0x36, 0x3D};

    /**
     * 应用于 url
     */
    private static final String KEY_TRACK = "sgt$%@CVBGgdt12q";
    private static final byte[] IV_TRACK = new byte[]{0x12, 0x34, 0x56, 0x78, 0x90 - 0x100, 0xAB - 0x100, 0xCD - 0x100, 0xEF - 0x100, 0x12, 0x34, 0x56, 0x78, 0x90 - 0x100, 0xAB - 0x100, 0xCD - 0x100, 0xEF - 0x100};

    /**
     * 加密 - 消息体
     *
     * @param content
     * @return
     */
    public static String encode4http(String content) {
        try {
            return AESUtil.encrypt(content, KEY_HTTP, IV_HTTP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 解密 - 消息体
     *
     * @param content
     * @return
     */
    public static String decode4http(String content) {
        try {
            return AESUtil.decrypt(content, KEY_HTTP, IV_HTTP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 解密 - 密码
     *
     * @param content
     * @return
     */
    public static String encode4password(String content) {
        try {
            return AESUtil.encrypt(content, KEY_PASSWORD, IV_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 解密 - 密码
     *
     * @param content
     * @return
     */
    public static String decode4password(String content) {
        try {
            return AESUtil.decrypt(content, KEY_PASSWORD, IV_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * url - 加密
     *
     * @param content
     * @return
     */
    public static String encode4track(String content) {
        try {
            return AESUtil.encrypt(content, KEY_TRACK, IV_TRACK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * url - 解密
     *
     * @param content
     * @return
     */
    public static String decode4track(String content) {
        try {
            return AESUtil.decrypt(content, KEY_TRACK, IV_TRACK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {

//        // 加密
//        String content2 = "songjiali";
//        String s2 = encode4http(content2);
//        System.out.println("加密结果: " + s2);
//
//        // 解密
//        String co2 = "VpfLlRiC1M5y1IZvGGC6FA==";
//        String s12 = decode4http(co2);
//        System.out.println("解密结果: " + s12);
        // 解密
        String co21 = "d3FQOGNWZU5NbHRVVXM2di82UHV4VTJjaHZNWk0vVGY4ZFh4Q3BFMjczaE9NRDNvS3dRMDV4UitoZ3RoWjJvS1REYlVybG9lRWNzWEdKYThZTitwK0Y3MXpyTHJ2aUFhMFJiVVk0RldTSURvZTJRWHI5WjE0SW56VHRnT0E5b1FUSU1Cekg5eUtaR2JublludUdkNm1TdEtMb2ZXeVdTUHpZV1RxSHNkYUFvPQ==";
        String s121 = decode4track(co21);
        String s122 = decode4password(co21);
        String s123 = decode4http(co21);
        System.out.println("解密结果: " + s121);
        System.out.println("解密结果: " + s122);
        System.out.println("解密结果: " + s123);




    }
}
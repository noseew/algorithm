package org.song.algorithm.algorithmbase.datatype.bit.bitmap;

import org.junit.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 布隆过滤器
 */
public class BitMap_base_03_bloomfilter {

    @Test
    public void test_01_start() {
        BloomFilter bloomFilter = new BloomFilter(1 << 16);
        System.out.println("存放----------------");
        for (int i = 0; i < 5; i++) {
            bloomFilter.add(i + "");
        }

        System.out.println("判断----------------");
        for (int i = 0; i < 10; i++) {
            System.out.println(i + ": " + bloomFilter.contains(i + ""));
        }

    }

    /*
    布隆过滤器
     */

    /**
     * 基于位图实现的布隆过滤器
     */
    public static class BloomFilter {

        private final BitMap_base_01.BitMap bitMap;

        public BloomFilter(int size) {
            size = Math.min(size, (2 << 16));
            bitMap = new BitMap_base_01.BitMap(size);
        }

        public boolean contains(String key) {
            boolean contains = true;
            for (Hash hash : Hash.values()) {
                contains = contains && bitMap.getBit(hash.calculate(key)) == 1;
            }
            return contains;
        }

        public void add(String key) {
            for (Hash hash : Hash.values()) {
                bitMap.setBit(hash.calculate(key));
            }
        }

        enum Hash {

            MD5() {
                MessageDigest messageDigest;
                {
                    try {
                        messageDigest = MessageDigest.getInstance("MD5");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public int calculate(String key) {
                    byte[] md5s = messageDigest.digest(key.getBytes());
//                        String javaMd5Str = DatatypeConverter.printHexBinary(md5s);
                    int code = precisionCompress(bytesToInt(md5s));
                    System.out.println(String.format("MD5:key=%s, code=%s", key, code));
                    return code;
                }
            },
            SHA1() {
                MessageDigest messageDigest;
                {
                    try {
                        messageDigest = MessageDigest.getInstance("SHA");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public int calculate(String key) {
                    byte[] shas = messageDigest.digest(key.getBytes());
                    int code = precisionCompress(bytesToInt(shas));
                    System.out.println(String.format("SHA:key=%s, code=%s", key, code));
                    return code;
                }
            },
            MAC() {
                Mac mac;
                {
                    try {
                        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacMD5");
                        SecretKey secretKey = keyGenerator.generateKey();
                        byte[] defaultKey = secretKey.getEncoded();
                        SecretKey restoreSecretKey = new SecretKeySpec(defaultKey, "HmacMD5");
                        mac = Mac.getInstance(restoreSecretKey.getAlgorithm());
                        mac.init(restoreSecretKey);
                    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public int calculate(String key) {
                    byte[] hmacMD5Bytes = mac.doFinal(key.getBytes());
                    int code = precisionCompress(bytesToInt(hmacMD5Bytes));
                    System.out.println(String.format("MAC:key=%s, code=%s", key, code));
                    return code;
                }
            },
            ;

            public abstract int calculate(String key);

            public static int toHashCode(String key) {
                // 参考 HashMap8
                int h;
                h = (h = key.hashCode()) ^ (h >>> 16);
                h = h & 0xffff;
                return h;
            }

            public static int bytesToInt(byte[] src) {
                int offset = 0;
                int value;
                value = (int) ((src[offset] & 0xFF)
                        | ((src[offset + 1] & 0xFF) << 8)
                        | ((src[offset + 2] & 0xFF) << 16)
                        | ((src[offset + 3] & 0xFF) << 24));
                return value;
            }

            public static int precisionCompress(int val) {
                int h;
                h = (h = val) ^ (h >>> 16);
                h = h & 0xffff;
                return h;
            }
        }

    }


}

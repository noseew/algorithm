package org.song.algorithm.base._01datatype._02high.bloom;

import org.song.algorithm.base._01datatype._01base._01linear.bit.bitmap.BitMap01;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

    /*
    布隆过滤器
        bitmap采用bit位来存储数据, 对于计算机来说bit是存储数据的最小单位, 所以bitmap是计算机存储数据的"极限"
        布隆过滤器并没有突破这个极限, 他也并没有存储数据, 仅仅是实现了数据有没有存在布隆过滤器中, 而无法将数据从布隆过滤器中取出
        但从存储限制来说, 布隆过滤器是突破了上面的"限制", 所以存储的精度并不是百分百的, 而是有一定的概率
        
        如何突破存储的限制呢?
            bitmap采用一个bit位来存储一个数据, 那么布隆过滤器用一个bit位来存储多个数据, 实际上是采用n个bit位来存储n+k个数据, 
            由于是1对多, 所以无法做到精确性
            
        布隆过滤器如何存储数据?
            存储一个数据k
                使用 hash1 => code1 然后存入 BitMapBase
                使用 hash2 => code2 然后存入 BitMapBase
                使用 hash_n => code_n 然后存入 BitMapBase
                这样这个数k就存储在bitmap中最多n个bit位, 因为不同的hash算法计算的结构可能会相同, 所以这个数可能会小于n
            判断数据k是否存在
                判断一个数k是否存在布隆过滤器中, 同样采用k经过n个hash算法后, 再从bitmap中获取, 如果全部命中, 则说明k存在bitmap中, 
                反之如果存在一个hash算法没有命中, 则说明k不存在bitmap中
                
        布隆过滤器的存储精度
            1. bitmap的容量
            2. hash算法组的数量
            3. hash算法组的分散差异度
     */

/**
 * 基于位图实现的布隆过滤器
 */
public class BloomFilter {

    protected final BitMap01 bitmap;
    protected int initSize;
    protected int maxSize;
    protected int maxMask;

    public BloomFilter(int initSize, int maxSize) {
        this.initSize = Math.min(initSize, (2 << 16));
        this.maxSize = maxSize;
        this.maxMask = (Integer.highestOneBit(maxSize) << 1) - 1;
        bitmap = new BitMap01(this.initSize);
    }

    public boolean contains(String key) {
        boolean contains = true;
        for (Hash hash : Hash.values()) {
            // 经过n个hash算法, 是否全部命中
            int index = hash.calculate(key) & this.maxMask;
            contains = contains && bitmap.getBit(index) == 1;
        }
        return contains;
    }

    public void add(String key) {
        for (Hash hash : Hash.values()) {
            // 经过n个hash算法, 存入bitmap
            int index = hash.calculate(key) & this.maxMask;
            bitmap.setBit(index);
            postAdd(key, index);
        }
    }
    
    protected void postAdd(String key, int index) {
        
    }

    protected enum Hash {

        /**
         * 采用MD5, 计算出Hash值
         * 不限平台, 不限次数, 同样的key总能得到同样的val, 可以实现布隆过滤器存储, 比如存储在redis中, 进行重用
         */
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
//                System.out.println(String.format("MD5:key=%s, code=%s", key, code));
                return code;
            }
        },
        /**
         * 采用SHA1, 计算出Hash值
         * 不限平台, 不限次数, 同样的key总能得到同样的val, 可以实现布隆过滤器存储, 比如存储在redis中, 进行重用
         */
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
//                System.out.println(String.format("SHA:key=%s, code=%s", key, code));
                return code;
            }
        },
        /**
         * 采用MAC, 计算出Hash值
         * 由于同样的key在不同的进程中得到的val不一致, 所以使用该算法的布隆过滤器不能够实现存储, 仅限于单次单进程的计算
         */
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
//                System.out.println(String.format("MAC:key=%s, code=%s", key, code));
                return code;
            }
        },
        ;

        public abstract int calculate(String key);

        /**
         * JDK的hash算法和MAC一样, 不同的进程计算的结果不一致, 所以不能用于存储
         *
         * @param key
         * @return
         */
        public static int toHashCode(String key) {
            // 参考 HashMap8
            int h;
            h = (h = key.hashCode()) ^ (h >>> 16);
            h = h & 0xffff;
            return h;
        }

        /**
         * 字节数组转成 int
         *
         * @param src
         * @return
         */
        public static int bytesToInt(byte[] src) {
            int offset = 0;
            int value;
            value = (int) ((src[offset] & 0xFF)
                    | ((src[offset + 1] & 0xFF) << 8)
                    | ((src[offset + 2] & 0xFF) << 16)
                    | ((src[offset + 3] & 0xFF) << 24));
            return value;
        }

        /**
         * int精度压缩
         * 将32位的int, 有损压缩成16位, 用于减少bitmap存储空间
         *
         * @param val
         * @return
         */
        public static int precisionCompress(int val) {
            int h;
            h = (h = val) ^ (h >>> 16);
            h = h & 0xffff;
            return h;
        }
    }

}

/*
 * Copyright: 2016 www.noseew.org Inc. All rights reserved.
 * 注意：本内容仅限于公司内部传阅，禁止外泄以及用于其他的商业目的
 *
 * @Project: labortory
 * @File: DESHelper
 * @Package: org.song.encription.basedemo.utils.des
 * @Date: 2018年07月18日
 * @Author:jiali.song@song.com
 *
 */
package org.song.encription.basedemo.utils.des;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @desc DES加解密
 * @description:
 * @author: jiali.song@song.com
 * @date: 2018年07月18日 17:39:59
 * <p>
 * DES加密介绍
 * DES是一种对称加密算法，所谓对称加密算法即：加密和解密使用相同密钥的算法。DES加密算法出自IBM的研究，
 * 后来被美国政府正式采用，之后开始广泛流传，但是近些年使用越来越少，因为DES使用56位密钥，以现代计算能力，
 * 24小时内即可被破解。虽然如此，在某些简单应用中，我们还是可以使用DES加密算法，本文简单讲解DES的JAVA实现。
 * 注意：DES加密和解密过程中，密钥长度都必须是8的倍数
 */
public class DESHelper {


    static final Base64.Decoder decoder = Base64.getDecoder();
    static final Base64.Encoder encoder = Base64.getEncoder();

    private final static String DES = "DES";
    private final static String KEY = "www.com.cn";

    public DESHelper() {
    }

    public static String encrypt(String pliantext) throws Exception {
        return encodeBase64(encryptDES(pliantext, KEY));
    }

    public static String encrypt(String pliantext, String key) throws Exception {
        return encodeBase64(encryptDES(pliantext, key));
    }

    public static String decrypt(String ciphertext) throws Exception {
        return decryptDES(decodeBase64(ciphertext.getBytes()), KEY);
    }

    public static String decrypt(String ciphertext, String key) throws Exception {
        return decryptDES(decodeBase64(ciphertext.getBytes()), key);
    }

    /**
     * base64编码
     *
     * @param binaryData
     * @return
     * @throws Exception
     */
    private static String encodeBase64(byte[] binaryData) throws Exception {
        try {
            return encoder.encodeToString(binaryData);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("BASE64编码失败!");
        }
    }

    /**
     * Base64解码
     *
     * @param binaryData
     * @return
     */
    private static byte[] decodeBase64(byte[] binaryData) {
        try {
            return decoder.decode(binaryData);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("BASE64解码失败！");
        }
    }

    public static byte[] encryptDES(String data, String key) {

        try {
            // 生成一个可信任的随机数源 ,  SHA1PRNG: 仅指定算法名称
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            // 从原始密钥数据创建DESKeySpec对象
            DESKeySpec deskey = new DESKeySpec(key.getBytes("UTF-8"));

            //创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey secretKey = keyFactory.generateSecret(deskey);
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance(DES);
            //用密匙初始化Cipher对象,
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
            //现在，获取数据并加密
            //正式执行加密操作
            return cipher.doFinal(data.getBytes("UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptDES(byte[] data, String key) {

        try {
            // 算法要求有一个可信任的随机数源,  SHA1PRNG: 仅指定算法名称
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            // 创建一个DESKeySpec对象
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
            // 创建一个密匙工厂
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            // 将DESKeySpec对象转换成SecretKey对象
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance(DES);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, secretKey, random);
            // 真正开始解密操作
            return new String(cipher.doFinal(data), "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        String enString = encrypt("{\"pageEntity\":{\"pageSize\":\"20\",\"pageIndex\":\"0\"},\"instance\":{\"status\":\"7\"}}");
        System.out.println("加密后的字串是：" + enString);
        String deString = decrypt(enString);
        System.out.println("解密后的字串是：" + deString);

    }


}
package org.song.encription.basedemo.utils.aes;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

/**
 * 通过AES算法对文本进行加密解密
 *  AES 工具类
 */
public class AESUtil {


    static final Base64.Decoder decoder = Base64.getDecoder();
    static final Base64.Encoder encoder = Base64.getEncoder();

    /**
     * 生成密钥
     */
    public static String initkey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM); // 实例化密钥生成器
        kg.init(128); // 初始化密钥生成器:AES要求密钥长度为128,192,256位
        SecretKey secretKey = kg.generateKey(); // 生成密钥
        return encoder.encodeToString(secretKey.getEncoded()); // 获取二进制密钥编码形式
    }

    /**
     * 转换密钥
     */
    public static Key toKey(byte[] key) throws Exception {
        return new SecretKeySpec(key, KEY_ALGORITHM);
    }

    /**
     * 密钥算法
     */
    public static final String KEY_ALGORITHM = "AES";

    /**
     * 加解密算法/工作模式/填充方式,Java6.0支持PKCS5Padding填充方式,BouncyCastle支持PKCS7Padding填充方式
     */
    public static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";

    /**
     * 加解密算法/工作模式/填充方式,Java6.0支持PKCS5Padding填充方式,BouncyCastle支持PKCS7Padding填充方式
     */
    public static final String CIPHER_ALGORITHM_ECB = "AES/ECB/PKCS5Padding";

    /**
     * 加密数据
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        Key k = toKey(decoder.decode(key)); // 还原密钥
        // 使用PKCS7Padding填充方式,这里就得这么写了(即调用BouncyCastle组件实现)
        // Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB, "BC");
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB); // 实例化Cipher对象，它用于完成实际的加密操作
        cipher.init(Cipher.ENCRYPT_MODE, k); // 初始化Cipher对象，设置为加密模式
        return encoder.encodeToString(cipher.doFinal(data.getBytes())); // 执行加密操作。加密后的结果通常都会用Base64编码进行传输
    }

    /**
     * 解密数据
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws Exception {
        Key k = toKey(decoder.decode(key));
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
        cipher.init(Cipher.DECRYPT_MODE, k); // 初始化Cipher对象，设置为解密模式
        return new String(cipher.doFinal(decoder.decode(data))); // 执行解密操作
    }

    /**
     * 加密，使用指定数据源生成密钥，使用用户数据作为算法参数进行AES加密
     *
     * @param data
     * @param KEY
     * @param IV
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String KEY, byte[] IV) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC); // 实例化Cipher对象，它用于完成实际的加密操作

        Key k = toKey(KEY.getBytes()); // 还原密钥

        AlgorithmParameterSpec paramSpec = new IvParameterSpec(IV); // 算法参数
        cipher.init(Cipher.ENCRYPT_MODE, k, paramSpec); // 初始化Cipher对象，设置为加密模式

        return encoder.encodeToString(cipher.doFinal(data.getBytes())); // 执行加密操作。加密后的结果通常都会用Base64编码进行传输
    }

    /**
     * 解密，对生成的16进制的字符串进行解密
     *
     * @param data
     * @param KEY
     * @param IV
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, String KEY, byte[] IV) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);

        Key k = toKey(KEY.getBytes());
        // 算法参数
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(IV);
        // 初始化Cipher对象，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, k, paramSpec);
        // 执行解密操作
        return new String(cipher.doFinal(decoder.decode(data)));
    }
}

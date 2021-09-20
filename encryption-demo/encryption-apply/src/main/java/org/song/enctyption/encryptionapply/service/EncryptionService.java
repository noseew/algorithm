package org.song.enctyption.encryptionapply.service;

import org.song.encription.basedemo.utils.aes.Encryption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 加解密的应用
 * 配置文件用户名密码加密, 读取的时候解密后使用
 */
@Service
public class EncryptionService {

    @Value("${db.user.name}")
    private String userName;

    @Value("${db.user.password}")
    private String password;

    public String getUserName() {
        return Encryption.decode4http(userName);
    }

    public String getPassword() {
        return Encryption.decode4http(password);
    }
}

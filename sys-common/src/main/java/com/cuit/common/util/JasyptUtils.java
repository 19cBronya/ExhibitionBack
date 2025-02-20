package com.cuit.common.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;

import javax.swing.*;


/**
 * alibaba druid加解密规则：
 * 明文密码+私钥(privateKey)加密=加密密码
 * 加密密码+公钥(publicKey)解密=明文密码
 */
public final class JasyptUtils {
    /**
     * 加密算法
     */
    private static final String PBEWITHMD5ANDDES = "PBEWithMD5AndDES";
    /**
     * @param text  待加密原文
     * @param crack 盐值（密钥）
     * @return 加密后的字符串
     * @Description: Jasypt加密（PBEWithMD5AndDES）
     */
    public static String encryptWithMD5(String text, String crack) {
        //1.创建加解密工具实例
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        //2.加解密配置
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
        config.setAlgorithm(PBEWITHMD5ANDDES);
        config.setPassword(crack);
        encryptor.setConfig(config);
        //3.加密
        return encryptor.encrypt(text);
    }

    /**
     * @param text  待解密原文
     * @param crack 盐值（密钥）
     * @return 解密后的字符串
     * @Description: Jasypt解密（PBEWithMD5AndDES）
     */
    public static String decryptWithMD5(String text, String crack) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
        config.setAlgorithm(PBEWITHMD5ANDDES);
        config.setPassword(crack);
        encryptor.setConfig(config);
        return encryptor.decrypt(text);
    }
    public static void main(String[] args) {
        String crack = "XYM@2001";
        System.out.println(encryptWithMD5("zmedhbhpmdpjdaaa",crack));
        System.out.println(decryptWithMD5("iW6Or+ILskeF4qV45PTNxGFz8PFOkbqsQAFKL6iGHow=",crack));
    }
}

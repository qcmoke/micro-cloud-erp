package com.qcmoke.common.utils.security;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by HUFENG on 2016/5/25 0025.
 * AES加密，+BASE64（通过key，生成128位秘钥） key填充方式
 * 128位
 */
public class AESUtil {

    //密钥算法
    public static final String KEY_ALGORITHM = "AES";

    //加解密算法/工作模式/填充方式,Java6.0支持PKCS5Padding填充方式,BouncyCastle支持PKCS7Padding填充方式
    public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * 生成密钥
     */
    public static String initkey() throws Exception{
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM); //实例化密钥生成器
        kg.init(128);                                              //初始化密钥生成器:AES要求密钥长度为128,192,256位
        SecretKey secretKey = kg.generateKey();                    //生成密钥
        return Base64.encodeBase64String(secretKey.getEncoded());  //获取二进制密钥编码形式
    }


    /**
     * 转换密钥
     */
    public static Key toKey(byte[] key) throws Exception{
        return new SecretKeySpec(key, KEY_ALGORITHM);
    }


    /**
     * 加密数据
     * @param data 待加密数据
     * @param password  密钥
     * @return 加密后的数据
     * */
    public static String encrypt(String data, String password) throws Exception{
        Key key = getSecretKeySpec(password);
        //使用PKCS7Padding填充方式,这里就得这么写了(即调用BouncyCastle组件实现)
        //Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);              //实例化Cipher对象，它用于完成实际的加密操作
        cipher.init(Cipher.ENCRYPT_MODE, key);                               //初始化Cipher对象，设置为加密模式
        return Base64.encodeBase64String(cipher.doFinal(data.getBytes())); //执行加密操作。加密后的结果通常都会用Base64编码进行传输
    }

    private static SecretKeySpec getSecretKeySpec(String password) throws NoSuchAlgorithmException {
        KeyGenerator kgen = KeyGenerator.getInstance(KEY_ALGORITHM);
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(password.getBytes());
        kgen.init(128, secureRandom);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, KEY_ALGORITHM);
        return key;
    }


    /**
     * 解密数据
     * @param data 待解密数据
     * @param password  密钥
     * @return 解密后的数据
     * */
    public static String decrypt(String data, String password) throws Exception{
        Key key = getSecretKeySpec(password);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);                          //初始化Cipher对象，设置为解密模式
        return new String(cipher.doFinal(Base64.decodeBase64(data))); //执行解密操作
    }


    public static void main(String[] args) throws Exception {
        String source = "[{\'clientId\':\'18\',\'exchangeType\':\'1\',\'happenGoldCount\':\'100\',\'businessType\':\'204\',\'context\':\'123123\'}]";
        System.out.println("原文：" + source);

//        String key = initkey();
        String key = "12345678123";
        System.out.println("密钥：" + key+",length="+key.length());

        String encryptData = encrypt(source, key);
        System.out.println("加密：" + encryptData);

        String decryptData = decrypt(encryptData, key);
        System.out.println("解密: " + decryptData);
    }
}

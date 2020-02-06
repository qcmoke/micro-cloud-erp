package com.qcmoke.auth;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.crypto.Cipher;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public class KeyTest {
    @Test
    public void testKey() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {

        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("key/qcmoke.key"), "123456password".toCharArray());
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("qcmoke", "123456secret".toCharArray());
        System.out.println(keyPair.getPrivate());
        System.out.println(keyPair.getPublic());

    }

    /**
     * 非对称加密（加密和解密不是使用同一个秘钥）
     * （1）私钥加密-公钥解密
     * （2）公钥加密-私钥解密
     *
     * @throws Exception
     */
    @Test
    public void testKey2() throws Exception {
        //1.从密钥库中获取私钥
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new BufferedInputStream(new ClassPathResource("key/qcmoke.keystore").getInputStream()), "123456password".toCharArray());//使用“秘钥库密码”加载得到密钥对文件
        PrivateKey prikey = (PrivateKey) ks.getKey("qcmoke", "123456secret".toCharArray());//使用“秘密保护密钥”获取私钥

        //2.从证书中获取公钥
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate cert = cf.generateCertificate(new ClassPathResource("key/qcmoke.crt").getInputStream());
        PublicKey pubKey = cert.getPublicKey();




        //3、构建加密解密类
        Cipher cipher = Cipher.getInstance("RSA");
        //测试数据
        String data = "测试数据";

        //4.使用公钥进行加密
        //设置为加密模式进行加密
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        //得到加密后数据
        byte[] encryptData = cipher.doFinal(data.getBytes());
        System.out.println(bytesToHexString(encryptData));
        System.out.println(new String(encryptData));

        //5、使用私钥进行解密
        //设置为解密模式进行解密
        cipher.init(Cipher.DECRYPT_MODE, prikey);
        //得到解密后的数据
        byte[] decryptData = cipher.doFinal(encryptData);
        System.out.println(new String(decryptData));

    }


    //把二进制转换成ASCII字符串。
    public static String bytesToHexString(byte[] bytes) {
        if (bytes == null)
            return "null!";
        int len = bytes.length;
        StringBuilder ret = new StringBuilder(2 * len);

        for (byte aByte : bytes) {
            int b = 0xF & aByte >> 4;
            ret.append("0123456789abcdef".charAt(b));
            b = 0xF & aByte;
            ret.append("0123456789abcdef".charAt(b));
        }

        return ret.toString();
    }
}

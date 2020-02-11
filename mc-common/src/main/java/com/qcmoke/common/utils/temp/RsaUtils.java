package com.qcmoke.common.utils.temp;

import com.qcmoke.common.utils.KeyStoreUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.Cipher;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author qcmoke
 */
public class RsaUtils {
    private static final Log logger = LogFactory.getLog(KeyStoreUtils.class);

    /**
     * 从文件中读取公钥
     *
     * @param filename 公钥保存路径，相对于classpath
     * @return 公钥对象
     * @throws Exception
     */
    public static PublicKey getPublicKey(String filename) throws Exception {
        byte[] bytes = readFile(filename);
        return getPublicKey(bytes);
    }

    /**
     * 从文件中读取密钥
     *
     * @param filename 私钥保存路径，相对于classpath
     * @return 私钥对象
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String filename) throws Exception {
        byte[] bytes = readFile(filename);
        return getPrivateKey(bytes);
    }

    /**
     * 获取公钥
     *
     * @param bytes 公钥的字节形式
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(byte[] bytes) throws Exception {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }

    /**
     * 获取密钥
     *
     * @param bytes 私钥的字节形式
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(byte[] bytes) throws Exception {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePrivate(spec);
    }

    /**
     * 根据密文，生存rsa公钥和私钥,并写出到指定文件
     *
     * @param publicKeyFilename  公钥文件路径
     * @param privateKeyFilename 私钥文件路径
     * @param secret             生成密钥的密文
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void generateKey(String publicKeyFilename, String privateKeyFilename, String secret) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRandom = new SecureRandom(secret.getBytes());
        keyPairGenerator.initialize(2048, secureRandom);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        // 获取公钥并写出
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        writeFile(publicKeyFilename, publicKeyBytes);
        // 获取私钥并写出
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        writeFile(privateKeyFilename, privateKeyBytes);
    }


    /**
     * 通过私钥和公钥获取密钥对
     *
     * @param privateKey PrivateKey
     * @param publicKey  PublicKey
     * @return KeyPair
     */
    public static KeyPair getKeyPair(PrivateKey privateKey, PublicKey publicKey) {
        return new KeyPair(publicKey, privateKey);
    }

    /**
     * 使用私钥进行加密
     *
     * @param privateKey
     * @param data
     * @return
     */
    public static byte[] encrypt(PrivateKey privateKey, byte[] data) {
        try {
            if (privateKey == null || data == null || data.length == 0) {
                return null;
            }
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            logger.error("e={}", e);
            return null;
        }
    }

    /**
     * 使用公钥进行解密
     *
     * @param publicKey
     * @param data
     * @return
     */
    public static byte[] decrypt(PublicKey publicKey, byte[] data) {
        try {
            if (publicKey == null || data == null || data.length == 0) {
                return null;
            }
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            logger.error("e={}", e);
            return null;
        }
    }


    private static byte[] readFile(String fileName) throws Exception {
        return Files.readAllBytes(new File(fileName).toPath());
    }

    private static void writeFile(String destPath, byte[] bytes) throws IOException {
        File dest = new File(destPath);
        if (!dest.exists()) {
            boolean newFile = dest.createNewFile();
            if (!newFile) {
                throw new RuntimeException("创建文件失败");
            }
        }
        Files.write(dest.toPath(), bytes);
    }
}
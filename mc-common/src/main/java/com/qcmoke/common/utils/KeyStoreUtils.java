package com.qcmoke.common.utils;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.Cipher;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

/**
 * @author qcmoke
 */
public class KeyStoreUtils {

    private static final Log logger = LogFactory.getLog(KeyStoreUtils.class);

    public static byte[] encrypt(InputStream keyStoreInputStream, String keyStorePassword, String alias, String keypass, byte[] data) {
        try {
            if (keyStoreInputStream == null || keyStorePassword == null || keyStorePassword.trim().length() == 0 || data == null || data.length == 0) {
                return null;
            }
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(keyStoreInputStream, keyStorePassword.toCharArray());
            PrivateKey privateKey = (PrivateKey) ks.getKey(alias, keypass.toCharArray());
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            logger.error("e={}", e);
            return null;
        }
    }


    public static byte[] decrypt(InputStream publicCertInputStream, byte[] data) {
        try {
            if (publicCertInputStream == null || data == null || data.length == 0) {
                return null;
            }
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate cert = cf.generateCertificate(publicCertInputStream);
            PublicKey pubKey = cert.getPublicKey();
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            logger.error("e={}", e);
            return null;
        }
    }
}

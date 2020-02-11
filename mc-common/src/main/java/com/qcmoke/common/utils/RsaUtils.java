package com.qcmoke.common.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author qcmoke
 */
public class RsaUtils {
    private static final Log logger = LogFactory.getLog(RsaUtils.class);
    /**
     * 秘钥算法
     */
    public static final String ALGORITHM = "RSA";
    /**
     * 证书类型
     */
    public static final String CERTIFICATE_TYPE = "X.509";

    /**
     * 通过pem格式公钥字符串获取公钥对象
     *
     * @param pem pem格式公钥字符串
     * @return PublicKey
     */
    public static PublicKey getPublicKeyFromPemEncoded(String pem) {
        if (pem == null || pem.trim().length() == 0) {
            return null;
        }
        int beginIndex = pem.indexOf("-----BEGIN PUBLIC KEY-----") + "-----BEGIN PUBLIC KEY-----".length();
        int endIndex = pem.indexOf("-----END PUBLIC KEY-----");
        String base64 = pem.substring(beginIndex, endIndex).trim();
        PublicKey publicKey;
        try {
            byte[] decode = new BASE64Decoder().decodeBuffer(base64);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decode);
            KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
            publicKey = kf.generatePublic(spec);
        } catch (Exception e) {
            logger.error("解析pem字符串获取公钥对象异常,e=" + e.getMessage());
            return null;
        }
        return publicKey;
    }

    /**
     * 通过cert格式公钥输入流获取公钥对象
     *
     * @param certKeyInputStream certKeyInputStream
     * @return PublicKey
     */
    public static PublicKey getPublicKeyFromCertKeyInputStream(InputStream certKeyInputStream) {
        if (certKeyInputStream == null) {
            return null;
        }
        Certificate cert;
        try {
            CertificateFactory cf = CertificateFactory.getInstance(CERTIFICATE_TYPE);
            cert = cf.generateCertificate(certKeyInputStream);
        } catch (CertificateException e) {
            return null;
        }
        if (cert == null) {
            return null;
        }
        return cert.getPublicKey();
    }


    private static void test1() {
        String pemKey = "-----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwlJSUTtDeUML9LYp6X/r\n" +
                "5/3LxdKaLLwpTPr+OHPYjRnYCdYXpG/CN13A+fubmX0Hs8zLfSN7Y+Ao93g1VDou\n" +
                "Y6FPcrDqoQ71AuH7lZG2eqHyOaOYKgfUSAG8N6I2OWZ7z1t1dRSBJbioP0xAic+O\n" +
                "8O31Mej23l64yuV9GogWRu0XjKam5fLumWLMmGGlMVYYZHUgX1AFbZQzgrophmug\n" +
                "dm5EOQWtok6Y3tS2/vbjVh/x/MNFMWsUVFjMi9t8UUU1v2S7twL/oPDXIc+lqoPX\n" +
                "UqqGXX5EZ0zn+yzwx2Ge53vTTut64wgrZCYuothNU0ULkfSVoW8SjJUjSwPJAKDI\n" +
                "0wIDAQAB\n" +
                "-----END PUBLIC KEY-----";
        PublicKey publicKey = RsaUtils.getPublicKeyFromPemEncoded(pemKey);
        System.out.println(publicKey);
    }

    private static void test2() {
        String certKey = "-----BEGIN CERTIFICATE-----\n" +
                "MIIDhTCCAm2gAwIBAgIEFdcLvTANBgkqhkiG9w0BAQsFADBzMQ4wDAYDVQQGEwVD\n" +
                "aGluYTEQMA4GA1UECBMHQmVpamluZzEQMA4GA1UEBxMHQmVpamluZzEYMBYGA1UE\n" +
                "ChMPd3d3LnFjbW9rZS5zaXRlMQ4wDAYDVQQLEwVDaGluYTETMBEGA1UEAxMKV2Vi\n" +
                "IFNlcnZlcjAeFw0yMDAxMjkyMzM1NDNaFw0yMDA0MjgyMzM1NDNaMHMxDjAMBgNV\n" +
                "BAYTBUNoaW5hMRAwDgYDVQQIEwdCZWlqaW5nMRAwDgYDVQQHEwdCZWlqaW5nMRgw\n" +
                "FgYDVQQKEw93d3cucWNtb2tlLnNpdGUxDjAMBgNVBAsTBUNoaW5hMRMwEQYDVQQD\n" +
                "EwpXZWIgU2VydmVyMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwlJS\n" +
                "UTtDeUML9LYp6X/r5/3LxdKaLLwpTPr+OHPYjRnYCdYXpG/CN13A+fubmX0Hs8zL\n" +
                "fSN7Y+Ao93g1VDouY6FPcrDqoQ71AuH7lZG2eqHyOaOYKgfUSAG8N6I2OWZ7z1t1\n" +
                "dRSBJbioP0xAic+O8O31Mej23l64yuV9GogWRu0XjKam5fLumWLMmGGlMVYYZHUg\n" +
                "X1AFbZQzgrophmugdm5EOQWtok6Y3tS2/vbjVh/x/MNFMWsUVFjMi9t8UUU1v2S7\n" +
                "twL/oPDXIc+lqoPXUqqGXX5EZ0zn+yzwx2Ge53vTTut64wgrZCYuothNU0ULkfSV\n" +
                "oW8SjJUjSwPJAKDI0wIDAQABoyEwHzAdBgNVHQ4EFgQU3tsqo1qzYiSqJwaq4b52\n" +
                "zl7oOagwDQYJKoZIhvcNAQELBQADggEBAG+W9zqrymWwnCRjtzrobIpn7+5M/GfI\n" +
                "b7ih0wAspaxkTmm6p6oRsPBHcd3BswxeH9XOU6V73rLuzj0nv3svFJdlKAhC8n1Q\n" +
                "HEgjLIofNZi5LeuT5ERyz6c6ypJEwN00R3FPWykO3Kj8shKfzktaZiOGIpy9tA69\n" +
                "+1cyuu5JgwBw8zlhCZTap8bbFc/BpiW9Ip7rHbCBnZUkG6kexzrlBCPRdy/OMYWM\n" +
                "qjIbaSLPZ82piD4SLl6XQ9r0EE4YQWCroJkI3pFo8ixN/+EGR+74U9vPeFczDdWf\n" +
                "pc/WOUK7ccMDtnEnH4/gRMzbGh1+ZDCQkPyXrWP6JsqqYZ0rH1D0Cpw=\n" +
                "-----END CERTIFICATE-----\n";
        PublicKey publicKey = RsaUtils.getPublicKeyFromCertKeyInputStream(new ByteArrayInputStream(certKey.getBytes()));
        System.out.println(publicKey);
    }

    public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException {
        test1();
//        test2();
    }
}

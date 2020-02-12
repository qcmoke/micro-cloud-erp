package com.qcmoke.common.utils.security;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qcmoke
 */
public class RSAUtils {
    private static final Log logger = LogFactory.getLog(RSAUtils.class);

    public static final String CHARSET = "UTF-8";
    /**
     * 秘钥算法
     */
    public static final String ALGORITHM = "RSA";
    /**
     * 证书类型
     */
    public static final String CERTIFICATE_TYPE = "X.509";


    /**
     * 生成经过base64编码的密钥对
     *
     * @param keySize 密钥长度
     * @return Map<String, String>
     */
    public static Map<String, String> createKeys(int keySize) {
        //为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm-->[" + ALGORITHM + "]");
        }

        //初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize);
        //生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        //得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = Base64.encodeBase64String(publicKey.getEncoded());
        //得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.encodeBase64String(privateKey.getEncoded());
        Map<String, String> keyPairMap = new HashMap<String, String>();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);

        return keyPairMap;
    }

    /**
     * 得到公钥
     *
     * @param publicKey 密钥字符串（经过base64编码）
     */
    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        //通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        return (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
    }

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


    /**
     * 得到私钥
     *
     * @param privateKey 密钥字符串（经过base64编码）
     * @return 私钥
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException  InvalidKeySpecException
     * @throws IOException              IOException
     */
    public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        //通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
    }

    /**
     * 公钥加密
     *
     * @param data      需要加密的数据
     * @param publicKey 公钥
     * @return 加密结果
     */
    public static String publicEncrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bytes = rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength());
            return Base64.encodeBase64String(bytes);
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥解密
     *
     * @param data       需要解密的数据
     * @param privateKey 私钥
     * @return 解密结果
     */

    public static String privateDecrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), privateKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥加密
     *
     * @param data       需要加密的数据
     * @param privateKey 私钥
     * @return 加密结果
     */

    public static String privateEncrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return Base64.encodeBase64String(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), privateKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 公钥解密
     * <p>
     * Cipher.getInstance("RSA/ECB/KeyEx")
     *
     * @param data      需要解密的数据
     * @param publicKey 公钥
     * @return 解密结果
     */

    public static String publicDecrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] b = data.getBytes(CHARSET);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int inputLen = b.length;
            int offSet = 0;
            byte[] cache;
            int maxDecryptBlock = 128;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > maxDecryptBlock) {
                    cache = cipher.doFinal(b, offSet, maxDecryptBlock);
                } else {
                    cache = cipher.doFinal(b, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * maxDecryptBlock;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return new String(decryptedData, CHARSET);

            // return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, new BASE64Decoder().decodeBuffer(data),1024), CHARSET);

        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }


    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        int maxBlock = 0;
        if (opmode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try {
            while (datas.length > offSet) {
                if (datas.length - offSet > maxBlock) {
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        } catch (Exception e) {
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
        }
        byte[] resultData = out.toByteArray();
        IOUtils.closeQuietly(out);
        return resultData;
    }


    /**
     * rsa验签
     *
     * @param content   被签名的内容
     * @param sign      签名后的结果
     * @param publicKey rsa公钥
     * @param charset   字符集
     * @return 验签结果
     * @throws SignatureException 验签失败，则抛异常
     */
    public static boolean doCheck(String content, String sign, String publicKey, String charset) throws SignatureException {
        try {
            PublicKey pubKey = getPublicKey(publicKey);
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initVerify(pubKey);
            signature.update(getContentBytes(content, charset));
            return signature.verify(Base64.decodeBase64(sign.getBytes()));
        } catch (Exception e) {
            throw new SignatureException("RSA验证签名[content = " + content + "; charset = " + charset
                    + "; signature = " + sign + "]发生异常!", e);
        }
    }

    public static byte[] getContentBytes(String content, String charset) throws UnsupportedEncodingException {
        if (StringUtils.isEmpty(charset)) {
            return content.getBytes();
        }

        return content.getBytes(charset);
    }

    /**
     * rsa签名
     *
     * @param content    待签名的字符串
     * @param privateKey rsa私钥字符串
     * @param charset    字符编码
     * @return 签名结果
     * @throws SignatureException 签名失败则抛出异常
     */
    public static String rsaSign(String content, String privateKey, String charset) throws SignatureException {
        try {
            PrivateKey priKey = getPrivateKey(privateKey);

            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(priKey);
            if (StringUtils.isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }

            byte[] signed = signature.sign();
            return new String(Base64.encodeBase64(signed));
        } catch (Exception e) {
            throw new SignatureException("RSAcontent = " + content + "; charset = " + charset, e);
        }
    }


    public static void main(String[] args) throws Exception {
        test1();
        test2();
        test3();
    }


    @SuppressWarnings("all")
    private static void test1() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        Map<String, String> keyMap = RSAUtils.createKeys(1024);
        //测试
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCx55x0CG1tpdFewgvuOf4c3DLFCGlCpbyASIJhX5pL1o/cGuRcf3/nT5sEpcGcPN2cUnd57BBw8h5/H4422riF6hE71zerGKnsR2lWAmgZmTcghRGLgs+1xKp6sUFQttJSQcIS928iN6VJmlCkF70HT2Ya/n/rfF8ymx8Kx7UdlQIDAQAB";
        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALHnnHQIbW2l0V7CC+45/hzcMsUIaUKlvIBIgmFfmkvWj9wa5Fx/f+dPmwSlwZw83ZxSd3nsEHDyHn8fjjbauIXqETvXN6sYqexHaVYCaBmZNyCFEYuCz7XEqnqxQVC20lJBwhL3byI3pUmaUKQXvQdPZhr+f+t8XzKbHwrHtR2VAgMBAAECgYAaykMAIijAY0EFIPmE9Uyz8eDfVOXs+GJLex/PJANrOjNNtOsAlt6e6ZjxeTiPm4bPvIdrX8YWDA/Vmt3imstAdsstcFHGGgRryaSVkLtklHoCOirEMJR84SIwx7coro8uRcJVY+lo6r40uCq/4naAa11Wif49DYsMsNmNiCRNgQJBAOYXqrh1V/E4HgTxgkwJeEBbb8CoCSDGxnzxT0wsflqXMJ1VmSVNJ/eV2F7WIg9cK0umJBaFKEvGXVx9faQg6bUCQQDF76be6mq2fTLGViAzX+tODUuLp+/q9B55eVwcvJ21s3HS5GRI0AMeILtvvC+rFrn8LoW/hOqtBxI0sJYl5FBhAkEA2a2QPHv/G90cQU7+FtNqqXAXtGsEX7bN90wP2h/J1ghs3Jwri2eIJSnlDiuFA4UODL58K7YD3lQm5SZvo8PjdQJAadyZyAFh34YoYNFxWWjEpbMQo3nHJEc6AUf6DtiGFMcLanqCdDrkX/mrpb/lUsDN6eVL3TmOdcohX5LOSyfIIQJBANQwNmTer7NnwgQLD1utxgaNHXcrNEdpj3OLUiXcupbLWS1dgCysKmrSJYUsuSFGjnDV5uMYUCU1+uJSuJSkJXQ=";
          /*String content="test";
          String rsaSign=rsaSign(content,privateKey,"UTF-8");
          System.out.println(doCheck(content,rsaSign,publicKey,"UTF-8"));

          System.out.println("公钥: \n\r" + publicKey);
          System.out.println("私钥： \n\r" + privateKey);*/

        System.out.println("公钥加密——私钥解密");
        String str = "hello";
        String encodedData = RSAUtils.publicEncrypt(str, RSAUtils.getPublicKey(publicKey));
        System.out.println("密文：\r\n" + encodedData);

        String decodeData = RSAUtils.privateDecrypt(encodedData, RSAUtils.getPrivateKey(privateKey));
        System.out.println("解密：\r\n" + decodeData);
    }

    private static void test2() {
        String pemKey = "-----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwlJSUTtDeUML9LYp6X/r\n" +
                "5/3LxdKaLLwpTPr+OHPYjRnYCdYXpG/CN13A+fubmX0Hs8zLfSN7Y+Ao93g1VDou\n" +
                "Y6FPcrDqoQ71AuH7lZG2eqHyOaOYKgfUSAG8N6I2OWZ7z1t1dRSBJbioP0xAic+O\n" +
                "8O31Mej23l64yuV9GogWRu0XjKam5fLumWLMmGGlMVYYZHUgX1AFbZQzgrophmug\n" +
                "dm5EOQWtok6Y3tS2/vbjVh/x/MNFMWsUVFjMi9t8UUU1v2S7twL/oPDXIc+lqoPX\n" +
                "UqqGXX5EZ0zn+yzwx2Ge53vTTut64wgrZCYuothNU0ULkfSVoW8SjJUjSwPJAKDI\n" +
                "0wIDAQAB\n" +
                "-----END PUBLIC KEY-----";
        PublicKey publicKey = getPublicKeyFromPemEncoded(pemKey);
        System.out.println(publicKey);
    }

    private static void test3() {
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
        PublicKey publicKey = getPublicKeyFromCertKeyInputStream(new ByteArrayInputStream(certKey.getBytes()));
        System.out.println(publicKey);
    }

}
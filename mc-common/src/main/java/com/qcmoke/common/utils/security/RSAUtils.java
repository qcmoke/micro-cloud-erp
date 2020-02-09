package com.qcmoke.common.utils.security;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
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

    public static final String CHARSET = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA";


    public static Map<String, String> createKeys(int keySize) {
        //为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
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
     * @throws Exception
     */
    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        //通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
        return key;
    }

    /**
     * 得到私钥
     *
     * @param privateKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        //通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
        return key;
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     */
    public static String publicEncrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
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
     * @param data
     * @param privateKey
     * @return
     */

    public static String privateDecrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), privateKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥加密
     *
     * @param data
     * @param privateKey
     * @return
     */

    public static String privateEncrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return Base64.encodeBase64String(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), privateKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 公钥解密
     *
     * @param data
     * @param publicKey
     * @return
     */

    public static String publicDecrypt(String data, RSAPublicKey publicKey) {
        try {
            // Cipher cipher = Cipher.getInstance("RSA/ECB/KeyEx");
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] b = data.getBytes(CHARSET);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int inputLen = b.length;
            int offSet = 0;
            byte[] cache;
            int MAX_DECRYPT_BLOCK = 128;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(b, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(b, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
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
        byte[] resultDatas = out.toByteArray();
        IOUtils.closeQuietly(out);
        return resultDatas;
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

     public static  byte[] getContentBytes(String content, String charset) throws UnsupportedEncodingException {
        if (StringUtils.isEmpty(charset)) {
            return content.getBytes();
        }

        return content.getBytes(charset);
    }

    /**
     * rsa签名
     *
     * @param content
     *            待签名的字符串
     * @param privateKey
     *            rsa私钥字符串
     * @param charset
     *            字符编码
     * @return 签名结果
     * @throws Exception
     *             签名失败则抛出异常
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
        Map<String, String> keyMap = RSAUtils.createKeys(1024);
//        测试
          String  publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCx55x0CG1tpdFewgvuOf4c3DLFCGlCpbyASIJhX5pL1o/cGuRcf3/nT5sEpcGcPN2cUnd57BBw8h5/H4422riF6hE71zerGKnsR2lWAmgZmTcghRGLgs+1xKp6sUFQttJSQcIS928iN6VJmlCkF70HT2Ya/n/rfF8ymx8Kx7UdlQIDAQAB";
          String  privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALHnnHQIbW2l0V7CC+45/hzcMsUIaUKlvIBIgmFfmkvWj9wa5Fx/f+dPmwSlwZw83ZxSd3nsEHDyHn8fjjbauIXqETvXN6sYqexHaVYCaBmZNyCFEYuCz7XEqnqxQVC20lJBwhL3byI3pUmaUKQXvQdPZhr+f+t8XzKbHwrHtR2VAgMBAAECgYAaykMAIijAY0EFIPmE9Uyz8eDfVOXs+GJLex/PJANrOjNNtOsAlt6e6ZjxeTiPm4bPvIdrX8YWDA/Vmt3imstAdsstcFHGGgRryaSVkLtklHoCOirEMJR84SIwx7coro8uRcJVY+lo6r40uCq/4naAa11Wif49DYsMsNmNiCRNgQJBAOYXqrh1V/E4HgTxgkwJeEBbb8CoCSDGxnzxT0wsflqXMJ1VmSVNJ/eV2F7WIg9cK0umJBaFKEvGXVx9faQg6bUCQQDF76be6mq2fTLGViAzX+tODUuLp+/q9B55eVwcvJ21s3HS5GRI0AMeILtvvC+rFrn8LoW/hOqtBxI0sJYl5FBhAkEA2a2QPHv/G90cQU7+FtNqqXAXtGsEX7bN90wP2h/J1ghs3Jwri2eIJSnlDiuFA4UODL58K7YD3lQm5SZvo8PjdQJAadyZyAFh34YoYNFxWWjEpbMQo3nHJEc6AUf6DtiGFMcLanqCdDrkX/mrpb/lUsDN6eVL3TmOdcohX5LOSyfIIQJBANQwNmTer7NnwgQLD1utxgaNHXcrNEdpj3OLUiXcupbLWS1dgCysKmrSJYUsuSFGjnDV5uMYUCU1+uJSuJSkJXQ=";
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

}
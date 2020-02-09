package com.qcmoke.auth;

import com.qcmoke.common.utils.KeyStoreUtils;
import com.qcmoke.common.utils.RsaUtils;
import com.qcmoke.common.utils.security.RSAUtils;
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
import java.util.Map;

public class KeyTest {
    @Test
    public void testKey() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {

        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("key/qcmoke.keystore"), "123456password".toCharArray());
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


    @Test
    public void test3() throws IOException {
        String data = "qqq";
        byte[] encrypt = KeyStoreUtils.encrypt(
                new ClassPathResource("key/qcmoke.keystore").getInputStream(),
                "123456password",
                "qcmoke",
                "123456secret", data.getBytes());
        System.out.println(new String(encrypt));


        byte[] decrypt = KeyStoreUtils.decrypt(new ClassPathResource("key/qcmoke.crt").getInputStream(), encrypt);
        System.out.println(new String(decrypt));
    }


    @Test
    public void test4() throws Exception {
        RsaUtils.generateKey("f:/public.key", "f:/private.key", "123456");
        KeyPair keyPair = RsaUtils.getKeyPair(RsaUtils.getPrivateKey("f:/private.key"), RsaUtils.getPublicKey("f:/public.key"));
    }

    @Test
    public void test5() throws Exception {
        PrivateKey privateKey = RsaUtils.getPrivateKey(new ClassPathResource("key/private.key").getFile().getAbsolutePath());
        PublicKey publicKey = RsaUtils.getPublicKey(new ClassPathResource("key/public.key").getFile().getAbsolutePath());
        byte[] encrypt = RsaUtils.encrypt(privateKey, "hah".getBytes());
        System.out.println(new String(encrypt));

        byte[] decrypt = RsaUtils.decrypt(publicKey, encrypt);
        System.out.println(new String(decrypt));
    }

    @Test
    public void test6() throws SignatureException {
        String data = "hello";
//        Map<String, String> keys = RSAUtils.createKeys(2048);
        String  publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCx55x0CG1tpdFewgvuOf4c3DLFCGlCpbyASIJhX5pL1o/cGuRcf3/nT5sEpcGcPN2cUnd57BBw8h5/H4422riF6hE71zerGKnsR2lWAmgZmTcghRGLgs+1xKp6sUFQttJSQcIS928iN6VJmlCkF70HT2Ya/n/rfF8ymx8Kx7UdlQIDAQAB";
        String  privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALHnnHQIbW2l0V7CC+45/hzcMsUIaUKlvIBIgmFfmkvWj9wa5Fx/f+dPmwSlwZw83ZxSd3nsEHDyHn8fjjbauIXqETvXN6sYqexHaVYCaBmZNyCFEYuCz7XEqnqxQVC20lJBwhL3byI3pUmaUKQXvQdPZhr+f+t8XzKbHwrHtR2VAgMBAAECgYAaykMAIijAY0EFIPmE9Uyz8eDfVOXs+GJLex/PJANrOjNNtOsAlt6e6ZjxeTiPm4bPvIdrX8YWDA/Vmt3imstAdsstcFHGGgRryaSVkLtklHoCOirEMJR84SIwx7coro8uRcJVY+lo6r40uCq/4naAa11Wif49DYsMsNmNiCRNgQJBAOYXqrh1V/E4HgTxgkwJeEBbb8CoCSDGxnzxT0wsflqXMJ1VmSVNJ/eV2F7WIg9cK0umJBaFKEvGXVx9faQg6bUCQQDF76be6mq2fTLGViAzX+tODUuLp+/q9B55eVwcvJ21s3HS5GRI0AMeILtvvC+rFrn8LoW/hOqtBxI0sJYl5FBhAkEA2a2QPHv/G90cQU7+FtNqqXAXtGsEX7bN90wP2h/J1ghs3Jwri2eIJSnlDiuFA4UODL58K7YD3lQm5SZvo8PjdQJAadyZyAFh34YoYNFxWWjEpbMQo3nHJEc6AUf6DtiGFMcLanqCdDrkX/mrpb/lUsDN6eVL3TmOdcohX5LOSyfIIQJBANQwNmTer7NnwgQLD1utxgaNHXcrNEdpj3OLUiXcupbLWS1dgCysKmrSJYUsuSFGjnDV5uMYUCU1+uJSuJSkJXQ=";
        String rsaSignContent = RSAUtils.rsaSign(data, privateKey, "utf-8");
        boolean b = RSAUtils.doCheck("hello", rsaSignContent, publicKey, "utf-8");
    }
}

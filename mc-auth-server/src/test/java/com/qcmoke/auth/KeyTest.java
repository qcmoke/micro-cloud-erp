package com.qcmoke.auth;

import com.qcmoke.common.utils.security.RSAUtils;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.RsaKeyUtil;
import org.jose4j.lang.JoseException;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.crypto.Cipher;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;

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
    public void test6() throws SignatureException {
        String data = "hello";
//        Map<String, String> keys = RSAUtils.createKeys(2048);
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCx55x0CG1tpdFewgvuOf4c3DLFCGlCpbyASIJhX5pL1o/cGuRcf3/nT5sEpcGcPN2cUnd57BBw8h5/H4422riF6hE71zerGKnsR2lWAmgZmTcghRGLgs+1xKp6sUFQttJSQcIS928iN6VJmlCkF70HT2Ya/n/rfF8ymx8Kx7UdlQIDAQAB";
        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALHnnHQIbW2l0V7CC+45/hzcMsUIaUKlvIBIgmFfmkvWj9wa5Fx/f+dPmwSlwZw83ZxSd3nsEHDyHn8fjjbauIXqETvXN6sYqexHaVYCaBmZNyCFEYuCz7XEqnqxQVC20lJBwhL3byI3pUmaUKQXvQdPZhr+f+t8XzKbHwrHtR2VAgMBAAECgYAaykMAIijAY0EFIPmE9Uyz8eDfVOXs+GJLex/PJANrOjNNtOsAlt6e6ZjxeTiPm4bPvIdrX8YWDA/Vmt3imstAdsstcFHGGgRryaSVkLtklHoCOirEMJR84SIwx7coro8uRcJVY+lo6r40uCq/4naAa11Wif49DYsMsNmNiCRNgQJBAOYXqrh1V/E4HgTxgkwJeEBbb8CoCSDGxnzxT0wsflqXMJ1VmSVNJ/eV2F7WIg9cK0umJBaFKEvGXVx9faQg6bUCQQDF76be6mq2fTLGViAzX+tODUuLp+/q9B55eVwcvJ21s3HS5GRI0AMeILtvvC+rFrn8LoW/hOqtBxI0sJYl5FBhAkEA2a2QPHv/G90cQU7+FtNqqXAXtGsEX7bN90wP2h/J1ghs3Jwri2eIJSnlDiuFA4UODL58K7YD3lQm5SZvo8PjdQJAadyZyAFh34YoYNFxWWjEpbMQo3nHJEc6AUf6DtiGFMcLanqCdDrkX/mrpb/lUsDN6eVL3TmOdcohX5LOSyfIIQJBANQwNmTer7NnwgQLD1utxgaNHXcrNEdpj3OLUiXcupbLWS1dgCysKmrSJYUsuSFGjnDV5uMYUCU1+uJSuJSkJXQ=";
        String rsaSignContent = RSAUtils.rsaSign(data, privateKey, "utf-8");
        boolean b = RSAUtils.doCheck("hello", rsaSignContent, publicKey, "utf-8");
    }

    @Test
    public void test7() throws JoseException, InvalidKeySpecException, InvalidJwtException, MalformedClaimException {
        String publicKeyPEM = "-----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwlJSUTtDeUML9LYp6X/r\n" +
                "5/3LxdKaLLwpTPr+OHPYjRnYCdYXpG/CN13A+fubmX0Hs8zLfSN7Y+Ao93g1VDou\n" +
                "Y6FPcrDqoQ71AuH7lZG2eqHyOaOYKgfUSAG8N6I2OWZ7z1t1dRSBJbioP0xAic+O\n" +
                "8O31Mej23l64yuV9GogWRu0XjKam5fLumWLMmGGlMVYYZHUgX1AFbZQzgrophmug\n" +
                "dm5EOQWtok6Y3tS2/vbjVh/x/MNFMWsUVFjMi9t8UUU1v2S7twL/oPDXIc+lqoPX\n" +
                "UqqGXX5EZ0zn+yzwx2Ge53vTTut64wgrZCYuothNU0ULkfSVoW8SjJUjSwPJAKDI\n" +
                "0wIDAQAB\n" +
                "-----END PUBLIC KEY-----";

        String jwtToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOjE3LCJ1c2VyX25hbWUiOiJxY21va2UiLCJzY29wZSI6WyJhbGwiXSwiZXhwIjoxNTgxMzk4NTY5LCJhdXRob3JpdGllcyI6WyJST0xFX21hbmFnZXIiLCJST0xFX3VzZXIiXSwianRpIjoiOGYzZWE3ZTgtZDczMS00YmNhLTgzOGEtMmY0ZTNmZDk5MzhlIiwiY2xpZW50X2lkIjoic3dhZ2dlciJ9.DsauNSllhsI7YRGcxcUgnzT1dfrRNqZCpOEmcdKcJmUZPt0TRQLyZFc_LHpUxhxHX0EEEOgVhxVBLmOigIuVUI6obg8NdujHUFn1LKgHLrM6bYL8-VqodzvTYknNFImU8sk02C4ErSnqPhHfPuG_jk5EGnjovPjFODrWTDP-wT9z50tn7QfmydjtPeoHflVqVYZSViB9A_irHCJgJ_zejDRnWeasHswEKhFkAK51CT8Rfm3HSomJBqO6susAUVUcOBIhfdl1kzJoZoPaXqfDrGUMhJNTkFXdPLR0epwntPTFVBXKOtaLWcdbSKf7h7PMNpYtcbKgU-xnWmq_7Clq0g";

        RsaKeyUtil rsaKeyUtil = new RsaKeyUtil();
        PublicKey publicKey = rsaKeyUtil.fromPemEncoded(publicKeyPEM);
        // create a JWT consumer
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setVerificationKey(publicKey)
                .build();

        // validate and decode the jwt
        JwtClaims jwtDecoded = jwtConsumer.processToClaims(jwtToken);
        NumericDate expirationTime = jwtDecoded.getExpirationTime();
        boolean isExpired = expirationTime.isBefore(NumericDate.now());
        String username = jwtDecoded.getStringClaimValue("user_name");
    }


    @Test
    public void test9() throws JoseException, InvalidKeySpecException {
        String pemKey = "-----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwlJSUTtDeUML9LYp6X/r\n" +
                "5/3LxdKaLLwpTPr+OHPYjRnYCdYXpG/CN13A+fubmX0Hs8zLfSN7Y+Ao93g1VDou\n" +
                "Y6FPcrDqoQ71AuH7lZG2eqHyOaOYKgfUSAG8N6I2OWZ7z1t1dRSBJbioP0xAic+O\n" +
                "8O31Mej23l64yuV9GogWRu0XjKam5fLumWLMmGGlMVYYZHUgX1AFbZQzgrophmug\n" +
                "dm5EOQWtok6Y3tS2/vbjVh/x/MNFMWsUVFjMi9t8UUU1v2S7twL/oPDXIc+lqoPX\n" +
                "UqqGXX5EZ0zn+yzwx2Ge53vTTut64wgrZCYuothNU0ULkfSVoW8SjJUjSwPJAKDI\n" +
                "0wIDAQAB\n" +
                "-----END PUBLIC KEY-----";

        String jwtToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOjE3LCJ1c2VyX25hbWUiOiJxY21va2UiLCJzY29wZSI6WyJhbGwiXSwiZXhwIjoxNTgxMzg5MDA4LCJhdXRob3JpdGllcyI6WyJST0xFX21hbmFnZXIiLCJST0xFX3VzZXIiXSwianRpIjoiZmVhMzZjYmQtZGJkYy00YWU5LWE3YWUtODdmYTk0MTIxOWI5IiwiY2xpZW50X2lkIjoic3dhZ2dlciJ9.OfPREavzhFY3Q4Nw7w6VcCATK5Nn2MuXw1AvhI22baidRmPirMbQk4ucjWP5mUSrvZgwPcxRrOSaTIVgSWTnZCajJv35SywjjU7sywt1oBV9CShapd1JLBiji3IFocu1gnNzrCUSF14RNKq1S6e44tsLd5YDPF-4xqY2bMmy8MaiRSfG8uTCdN7F0D7qW2e9WZb3fGddixDYqCfNMYsDEryAvApSp3oieWmN-NZ2j80cPauFTnmCubuPEEpiX6yO0JhjgVf8g8gDWZPp5RHGdQ7KN6OvOZvww6OMzGSbd_GOPH1rjnb0hh5q4q93gQRYQjBX7V4ETP3ZtZtJWPk5tg";
        Jwt decode = JwtHelper.decode(jwtToken);
        decode.verifySignature(new RsaVerifier(pemKey));
        String claims = decode.getClaims();
        System.out.println(claims);
    }
}

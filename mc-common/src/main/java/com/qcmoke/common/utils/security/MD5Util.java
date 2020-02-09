package com.qcmoke.common.utils.security;

import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;

/**
 * @author qcmoke
 */
public class MD5Util {

    public static final String KEY_MD5 = "MD5";

    /**
     * 默认编码格式
     */
    public static final String DEFAULTCHARSET = "UTF-8";

    /**
     * 用来将字节转换成 16 进制表示的字符
     */
    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * MD5加密
     *
     * @param source
     * @param charSet 字符集
     * @return
     * @throws Exception
     */
    public static String encryptMD5(String source, String charSet) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        if (StringUtils.isNotBlank(charSet)) {
            md5.update(source.getBytes(charSet));
        } else {
            md5.update(source.getBytes(DEFAULTCHARSET));
        }
        byte[] encryptStr = md5.digest();
        char str[] = new char[16 * 2];
        int k = 0;
        for (int i = 0; i < 16; i++) {
            byte byte0 = encryptStr[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }

    /**
     * 默认字符集
     *
     * @param source
     * @return
     * @throws Exception
     */
    public static String encryptMD5(String source) throws Exception {
        return encryptMD5(source, DEFAULTCHARSET);
    }


    public static void main(String[] args) throws Exception {
        String inputStr = "简单加密";
        System.err.println("原文:" + inputStr);
        String result = encryptMD5(inputStr, null);
        System.err.println("MD5:" + result);
    }
}

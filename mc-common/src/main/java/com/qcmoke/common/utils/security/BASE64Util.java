package com.qcmoke.common.utils.security;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Base64Utils;

/**
 * BASE64加密、解密
 * 使用commons codec包实现，防止换行问题
 * @author qcmoke
 */
public class BASE64Util {

    /**
     * 默认编码格式
     */
    public static final String DEFAULTCHARSET = "UTF-8";

    /**
     * BASE64解密
     *
     * @param source
     * @param charSet
     * @return
     * @throws Exception
     */
    public static String decryptBASE64(String source, String charSet) throws Exception {
        byte[] bytes = null;
        if(StringUtils.isNotBlank(charSet)){
            bytes = source.getBytes(charSet);
        }else {
            bytes = source.getBytes(DEFAULTCHARSET);
        }
        return new String(Base64.decodeBase64(bytes));
    }

    public static String decryptBASE64(String source) throws Exception {
        return decryptBASE64(source,DEFAULTCHARSET);
    }

    /**
     * BASE64加密
     *
     * @param source
     * @param charSet
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(String source, String charSet) throws Exception {
        byte[] bytes = null;
        if(StringUtils.isNotBlank(charSet)){
            bytes = source.getBytes(charSet);
        }else {
            bytes = source.getBytes(DEFAULTCHARSET);
        }
        return new String(Base64.encodeBase64(bytes));
    }

    public static String encryptBASE64(String source) throws Exception {
        return encryptBASE64(source,DEFAULTCHARSET);
    }

    public static void main(String[] args) throws Exception {
        String str = "test111";
        String code = encryptBASE64(str);
        System.out.println("加密后："+code+",length="+code.length());

        String result = Base64Utils.encodeToString(str.getBytes());
        System.out.println("加密后："+result+",length="+result.length());

        result = Base64.encodeBase64String(str.getBytes());
        System.out.println("加密后："+result+",length="+result.length());

        result = decryptBASE64(code);
        System.out.println("解密后："+ new String(result));
    }
}

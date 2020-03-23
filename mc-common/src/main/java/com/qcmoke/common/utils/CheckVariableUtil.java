
package com.qcmoke.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author qcmoke
 */
public class CheckVariableUtil {
    /**
     * 检查分页信息非法
     *
     * @param pageNum  起始页
     * @param pageSize 页面大小
     * @return 如果分页信息非法返回true
     */
    public static boolean pageParamIsIllegal(Integer pageNum, Integer pageSize) {

        return pageNum == null || pageSize == null || pageNum <= 0 || pageSize < 0;
    }

    /**
     * 检测空字符串变量
     *
     * @return 空字符传返回true 非空返回false
     */
    private static boolean stringVariableIsEmpty(String string) {
        return string != null && !"".equals(string.trim());
    }

    /**
     * 检测Integer值为null或小于0
     *
     * @return 如果为null或小于0返回true
     */
    public static boolean integerParamLessZero(Integer value) {
        return null == value || (value < 0);
    }

    /**
     * 检测字符串是Integer格式的
     * 如果字符串 -不是- Integer格式的返回true
     */
    public static boolean variableIsInteger(String value) {
        if (value == null) {
            return true;
        }
        String pattern = "^[1-9]\\d*|0$";
        return !Pattern.matches(pattern, value);
    }


    /**
     * 检测身份证号是非法的
     *
     * @param idCard idCard
     * @return 非法返回true
     */
    public static boolean idCardIsIllegal(String idCard) {
        return idCard == null || "".equals(idCard.trim()) || idCard.trim().length() != 18 || !idCard.matches("\\d{17}[\\d|X]");
    }

    /**
     * 字符串转Double
     *
     * @param defaultNum 默认值
     */
    public static Double parseDouble(String str, double defaultNum) {
        double num = defaultNum;
        if (stringVariableIsEmpty(str)) {
            try {
                num = Double.parseDouble(str.trim());
            } catch (Exception ignored) {
            }
        }
        return num;
    }

    /**
     * 字符串转Integer
     *
     * @param defaultNum 默认值
     */
    public static Integer parseInt(String str, int defaultNum) {
        int num = defaultNum;
        if (stringVariableIsEmpty(str)) {
            try {
                num = Integer.parseInt(str.trim());
            } catch (Exception ignored) {
            }
        }
        return num;
    }

    /**
     * 手机号验证
     *
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$");
        m = p.matcher(str);
        b = m.matches();
        return b;
    }


    /**
     * 邮箱验证
     *
     * @return 验证通过返回true
     */
    public static boolean isEmai(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 判断字符串是否为合法邮箱
     */
    public static boolean isEmail(String email) {
        boolean result = false;
        if (email != null) {
            Pattern pattern = Pattern.compile("^([a-z0-9A-Z._-])+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
            if (pattern.matcher(email).matches()) {
                result = true;
            }
        }
        return result;
    }


    /**
     * 哦按段字符串是否为合法IP
     */
    public static boolean isIp(String ip) {
        if (null == ip || "".equals(ip)) {
            return false;
        }
        String regEx = "^([0-9]|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.([0-9]|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.([0-9]|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.([0-9]|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    /**
     * 判断是否为合法手机号码
     */
    public static boolean isMobileNum(String tel) {
        if (null == tel || "".equals(tel)) {
            return false;
        }

        Matcher matcher = Pattern.compile("(0?[0-9]{11})|((2|6)[0-9]{7})|(886[0-9]{9})").matcher(tel);
        return matcher.matches();
    }


}

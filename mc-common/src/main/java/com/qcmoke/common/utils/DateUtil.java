package com.qcmoke.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 *
 * @author qcmoke
 */
public class DateUtil {

    public static final String FULL_TIME_PATTERN = "yyyyMMddHHmmss";

    public static final String FULL_TIME_SPLIT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String CST_TIME_PATTERN = "EEE MMM dd HH:mm:ss zzz yyyy";

    /**
     * 格式化时间，格式为 yyyyMMddHHmmss
     *
     * @param localDateTime LocalDateTime
     * @return 格式化后的字符串
     */
    public static String formatFullTime(LocalDateTime localDateTime) {
        return formatFullTime(localDateTime, FULL_TIME_PATTERN);
    }

    /**
     * 根据传入的格式，格式化时间
     *
     * @param localDateTime LocalDateTime
     * @param format        格式
     * @return 格式化后的字符串
     */
    public static String formatFullTime(LocalDateTime localDateTime, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(dateTimeFormatter);
    }

    /**
     * 根据传入的格式，格式化时间
     *
     * @param date   Date
     * @param format 格式
     * @return 格式化后的字符串
     */
    public static String getDateFormat(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    /**
     * 格式化 CST类型的时间字符串
     *
     * @param date   CST类型的时间字符串
     * @param format 格式
     * @return 格式化后的字符串
     * @throws ParseException 异常
     */
    public static String formatCstTime(String date, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CST_TIME_PATTERN, Locale.US);
        Date usDate = simpleDateFormat.parse(date);
        return getDateFormat(usDate, format);
    }

    /**
     * 格式化 Instant
     *
     * @param instant Instant
     * @param format  格式
     * @return 格式化后的字符串
     */
    public static String formatInstant(Instant instant, String format) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 判断当前时间是否在指定时间范围
     *
     * @param from 开始时间
     * @param to   结束时间
     * @return 结果
     */
    public static boolean between(LocalTime from, LocalTime to) {
        LocalTime now = LocalTime.now();
        return now.isAfter(from) && now.isBefore(to);
    }


    /**
     * 转换Date类型为字符串类型
     */
    public static String getSimpleDate(Date value) {
        return getSimpleDate(value, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 转换字符串类型为Date类型
     */
    public static Date getDate(String value) throws ParseException {
        if (null == value) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.parse(value);
    }

    /**
     * 转换Date类型为中文字符串类型
     */
    public static String getChinieseDate(Date value) {
        return getSimpleDate(value, "yyyy年MM月dd日HH时mm分");
    }

    /**
     * 转换Date类型为字符串类型
     */
    public static String getSimpleDate(Date value, String pattern) {
        if (null == value) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(value);
    }

    /**
     * 返回日期是否在指定日期之间
     */
    public static Boolean betweenDates(Date date, Date start, Date end) {
        try {
            if (date.before(end) && date.after(start)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * 返回日期是否在指定日期之间(包括相等情况)
     */
    public static boolean betweenDatesWithBound(Date date, Date start, Date end) {
        try {
            if (date.getTime() == start.getTime() || date.getTime() == end.getTime()) {
                return true;
            } else if (date.before(end) && date.after(start)) {
                return true;
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * 获取几月后的时间
     */
    public static Date nextMonth(Date d, int m) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.MONTH, now.get(Calendar.MONTH) + m);
        return now.getTime();
    }

    /**
     * 获取几天后的时间
     */
    public static Date nextDate(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 计算两个时间之间的秒数，注意不能超过50年
     *
     * @param start 开始时间
     * @param end   结束时间
     */
    public static int calSecondsBetween(Date start, Date end) {
        long times = (end.getTime() - start.getTime()) / 1000;
        return (int) times;
    }

    /**
     * 获取几小时后的时间
     */
    public static Date nextHour(Date d, int hour) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY) + hour);
        return now.getTime();
    }

    /**
     * 获取几分钟后的时间
     */
    public static Date nextMinute(Date d, int minute) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + minute);
        return now.getTime();
    }

    /**
     * 获取几秒钟后的时间
     */
    public static Date nextSecond(Date d, int second) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.SECOND, now.get(Calendar.SECOND) + second);
        return now.getTime();
    }

    /**
     * 获取两个日期的间隔天数
     */
    public static int dayInterval(Date startDay, Date endDay) {
        return (int) ((endDay.getTime() - startDay.getTime()) / (24 * 60 * 60 * 1000));
    }

    public static int dayIntervalExact(Date startDay, Date endDay) {
        // 避免死循环
        if (startDay.getTime() - endDay.getTime() > 0) {
            Date tmp = startDay;
            startDay = endDay;
            endDay = tmp;
        }

        return dayInterval(trimDate(startDay), trimDate(endDay));
    }


    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @return String 返回值为：xx天xx小时xx分xx秒
     */
    public static String getDistanceTime(Date startTime, Date endTime) {
        return getString(startTime, endTime);
    }

    static String getString(Date startTime, Date endTime) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long time1 = startTime.getTime();
        long time2 = endTime.getTime();
        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return day + "天" + hour + "小时" + min + "分" + sec + "秒";
    }

    /**
     * 转换String类型为Date类型
     */
    public static Date getSimpleDate(String value) throws ParseException {
        if (null == value || "".equals(value.trim())) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.parse(value);
    }

    /**
     * 转换String类型为Date类型
     */
    public static Date getSimpleDateBy(String value, String pattern) throws ParseException {
        if (null == value || "".equals(value.trim())) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.parse(value);
    }

    /**
     * 转换String类型为Date类型
     */
    public static Date getSimpleDate2(String value) throws ParseException {
        if (null == value || "".equals(value.trim())) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.parse(value);
    }

    /**
     * 把时分秒置为0
     */
    public static Date getDateOnly(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }


    /**
     * 从指定的时间截取年月日。将时分秒毫秒都设置为0
     *
     * @param source 原始时间
     * @return 将时分秒毫秒都设置为0的日期
     */
    public static Date trimDate(Date source) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(source);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 返回指定时间在当天的最后时间，便于查询
     */
    public static Date fillDate(Date source) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(source);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
}

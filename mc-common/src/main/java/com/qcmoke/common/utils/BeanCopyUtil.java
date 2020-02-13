package com.qcmoke.common.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于spring的beanUtils工具
 *
 * @author qcmoke
 */
public class BeanCopyUtil {

    /**
     * 拷贝实体
     *
     * @param source
     * @return
     */
    public static <T, K> K copy(T source, Class<K> clz) {
        if (source == null) {
            return null;
        }
        try {
            K target = clz.newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 拷贝实体
     *
     * @param source
     * @param properties 忽略的字段名称
     * @return
     */
    public static <T, K> K copy(T source, Class<K> clz, String... properties) {
        if (source == null) {
            return null;
        }
        try {
            K target = clz.newInstance();
            BeanUtils.copyProperties(source, target, properties);
            return target;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 拷贝list
     *
     * @param sourceList
     * @param clz
     * @return
     */
    public static <T, K> List<K> copy(List<T> sourceList, Class<K> clz) {
        try {
            if (sourceList == null) {
                return null;
            }
            List<K> list = new ArrayList<K>();
            for (T source : sourceList) {
                K taeget = copy(source, clz);
                if (taeget != null) {
                    list.add(taeget);
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 拷贝list
     *
     * @param sourceList
     * @param clz
     * @param properties 忽略的字段名称
     * @return
     */
    public static <T, K> List<K> copy(List<T> sourceList, Class<K> clz, String... properties) {
        try {
            if (sourceList == null) {
                return null;
            }
            List<K> list = new ArrayList<K>();
            for (T source : sourceList) {
                K taeget = copy(source, clz, properties);
                if (taeget != null) {
                    list.add(taeget);
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

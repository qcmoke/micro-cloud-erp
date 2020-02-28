package com.qcmoke.ums.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.common.dto.PageQuery;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @author qcmoke
 */
public class SqlUtil {


    /**
     * 封装前端分页表格所需数据
     *
     * @param pageInfo pageInfo
     * @return Map<String, Object>
     */
    public static Map<String, Object> getDataTable(IPage<?> pageInfo) {
        Map<String, Object> data = new HashMap<>(2);
        data.put("rows", pageInfo.getRecords());
        data.put("total", pageInfo.getTotal());
        return data;
    }

    /**
     * 处理排序（分页情况下） for mybatis-plus
     *
     * @param pageQuery         PageQuery
     * @param defaultSort       默认排序的字段
     * @param defaultOrder      默认排序规则
     * @param camelToUnderscore 是否开启驼峰转下划线
     */
    public static Page<?> handlePageSort(PageQuery pageQuery, String defaultSort, String defaultOrder, boolean camelToUnderscore) {
        Page<?> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        String sortField = pageQuery.getField();
        if (camelToUnderscore) {
            sortField = camelToUnderscore(sortField);
            defaultSort = camelToUnderscore(defaultSort);
        }
        if (StringUtils.isNotBlank(pageQuery.getField())
                && StringUtils.isNotBlank(pageQuery.getOrder())
                && !StringUtils.equalsIgnoreCase(pageQuery.getField(), "null")
                && !StringUtils.equalsIgnoreCase(pageQuery.getOrder(), "null")) {
            if (StringUtils.equals(pageQuery.getOrder(), PageQuery.ORDER_DESC)) {
                page.addOrder(OrderItem.desc(sortField));
            } else {
                page.addOrder(OrderItem.asc(sortField));
            }
        } else {
            if (StringUtils.isNotBlank(defaultSort)) {
                if (StringUtils.equals(defaultOrder, PageQuery.ORDER_DESC)) {
                    page.addOrder(OrderItem.desc(defaultSort));
                } else {
                    page.addOrder(OrderItem.asc(defaultSort));
                }
            }
        }
        return page;
    }

    /**
     * 处理排序 for mybatis-plus
     *
     * @param pageQuery PageQuery
     * @param page    Page
     */
    public static void handlePageSort(PageQuery pageQuery, Page<?> page) {
        handlePageSort(pageQuery, null, null, false);
    }

    /**
     * 处理排序 for mybatis-plus
     *
     * @param pageQuery           PageQuery
     * @param page              Page
     * @param camelToUnderscore 是否开启驼峰转下划线
     */
    public static void handlePageSort(PageQuery pageQuery, Page<?> page, boolean camelToUnderscore) {
        handlePageSort(pageQuery, null, null, camelToUnderscore);
    }

    /**
     * 处理排序 for mybatis-plus
     *
     * @param pageQuery           PageQuery
     * @param wrapper           wrapper
     * @param defaultSort       默认排序的字段
     * @param defaultOrder      默认排序规则
     * @param camelToUnderscore 是否开启驼峰转下划线
     */
    public static void handleWrapperSort(PageQuery pageQuery, QueryWrapper<?> wrapper, String defaultSort, String defaultOrder, boolean camelToUnderscore) {
        String sortField = pageQuery.getField();
        if (camelToUnderscore) {
            sortField = camelToUnderscore(sortField);
            defaultSort = camelToUnderscore(defaultSort);
        }
        if (StringUtils.isNotBlank(pageQuery.getField())
                && StringUtils.isNotBlank(pageQuery.getOrder())
                && !StringUtils.equalsIgnoreCase(pageQuery.getField(), "null")
                && !StringUtils.equalsIgnoreCase(pageQuery.getOrder(), "null")) {
            if (StringUtils.equals(pageQuery.getOrder(), PageQuery.ORDER_DESC)) {
                wrapper.orderByDesc(sortField);
            } else {
                wrapper.orderByAsc(sortField);
            }
        } else {
            if (StringUtils.isNotBlank(defaultSort)) {
                if (StringUtils.equals(defaultOrder, PageQuery.ORDER_DESC)) {
                    wrapper.orderByDesc(defaultSort);
                } else {
                    wrapper.orderByAsc(defaultSort);
                }
            }
        }
    }

    /**
     * 处理排序 for mybatis-plus
     *
     * @param pageQuery PageQuery
     * @param wrapper wrapper
     */
    public static void handleWrapperSort(PageQuery pageQuery, QueryWrapper<?> wrapper) {
        handleWrapperSort(pageQuery, wrapper, null, null, false);
    }

    /**
     * 处理排序 for mybatis-plus
     *
     * @param pageQuery           PageQuery
     * @param wrapper           wrapper
     * @param camelToUnderscore 是否开启驼峰转下划线
     */
    public static void handleWrapperSort(PageQuery pageQuery, QueryWrapper<?> wrapper, boolean camelToUnderscore) {
        handleWrapperSort(pageQuery, wrapper, null, null, camelToUnderscore);
    }


    /**
     * 驼峰转下划线
     *
     * @param value 待转换值
     * @return 结果
     */
    public static String camelToUnderscore(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        String[] arr = StringUtils.splitByCharacterTypeCamelCase(value);
        if (arr.length == 0) {
            return value;
        }
        StringBuilder result = new StringBuilder();
        IntStream.range(0, arr.length).forEach(i -> {
            if (i != arr.length - 1) {
                result.append(arr[i]).append("_");
            } else {
                result.append(arr[i]);
            }
        });
        return StringUtils.lowerCase(result.toString());
    }

    /**
     * 下划线转驼峰
     *
     * @param value 待转换值
     * @return 结果
     */
    public static String underscoreToCamel(String value) {
        StringBuilder result = new StringBuilder();
        String[] arr = value.split("_");
        for (String s : arr) {
            result.append((String.valueOf(s.charAt(0))).toUpperCase()).append(s.substring(1));
        }
        return result.toString();
    }

}

package com.qcmoke.fms.vo;

import lombok.Data;

import java.util.List;

/**
 * @author qcmoke
 */
@Data
public class StatisticsDataVo {
    /**
     * 每月订单数
     */
    private List<Integer> orderQuantity;

    /**
     * 营业额
     */
    private List<Double> turnover;

    /**
     * 纯利润
     */
    private List<Double> profit;
}

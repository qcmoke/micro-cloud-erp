package com.qcmoke.oms.vo;

import com.qcmoke.oms.entity.SaleOrderMaster;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author qcmoke
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SaleOrderMasterVo extends SaleOrderMaster {
    private List<SaleOrderDetailVo> details;
    /**
     * 客户姓名
     */
    private String customerName;
    /**
     * 支付方式
     */
    private String payTypeInfo;
    /**
     * 订单状态
     */
    private String statusInfo;
    /**
     * 发票类型
     */
    private String billTypeInfo;

    /**
     * 发货申请状态
     */
    private String transferStockStatusInfo;
}

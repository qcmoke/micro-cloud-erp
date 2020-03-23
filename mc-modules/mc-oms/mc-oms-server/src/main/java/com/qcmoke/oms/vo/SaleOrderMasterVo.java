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
     * 支付方式【1->支付宝；2->微信；3->银联； 4->货到付款；】
     */
    private String payTypeInfo;
    /**
     * 订单状态【0->待付款；1->待发货；2->已发货；3->未确认收货；4->已确认收货；5->已完成；6->已关闭；7->无效订单】
     */
    private String statusInfo;
    /**
     * 发票类型[0->不开发票；1->电子发票；2->纸质发票]
     */
    private String billTypeInfo;

    /**
     * 发货申请状态(1:未移交申请；2:已移交申请；3:移交失败；4:已完成移交；)
     */
    private String transferStockStatusInfo;
}

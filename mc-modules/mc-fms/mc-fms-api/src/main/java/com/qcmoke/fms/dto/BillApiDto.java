package com.qcmoke.fms.dto;

import com.qcmoke.fms.constant.DealType;
import com.qcmoke.fms.constant.PayType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 流水账单
 *
 * @author qcmoke
 */
@Accessors(chain = true)
@Data
public class BillApiDto implements Serializable {

    /**
     * 账目类型(1:采购付款；2:采购退货收款；3:销售收款；4:销售退款)
     */
    private DealType dealType;

    /**
     * 交易单据编号(采购单，采购退货单，销售订单，销售退货单等编号)
     */
    private Long dealNum;

    /**
     * 支付方式
     */
    private PayType payType;

    /**
     * 合计金额
     */
    private Double totalAmount;

}

package com.qcmoke.oms.vo;

import com.qcmoke.oms.entity.SaleRefund;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author qcmoke
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SaleRefundVo extends SaleRefund {
    /**
     * 仓库审核状态（1：未审核；2：审核通过；3：审核不通过）
     */
    private String stockCheckStatusInfo;

    /**
     * 入库状态（1：未入库；2：已入库）
     */
    private String stockInStatusInfo;

    private String refundChannelInfo;
}

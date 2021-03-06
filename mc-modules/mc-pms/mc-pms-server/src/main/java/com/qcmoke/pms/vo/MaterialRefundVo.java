package com.qcmoke.pms.vo;

import com.qcmoke.pms.entity.MaterialRefund;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author qcmoke
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MaterialRefundVo extends MaterialRefund {

    private String refundChannelInfo;


    /**
     * 仓库审核状态（1：未审核；2：审核通过；3：审核不通过）
     */
    private String stockCheckStatusInfo;

    /**
     * 发货状态（1：未发货；2：已发货）
     */
    private String stockOutStatusInfo;
}

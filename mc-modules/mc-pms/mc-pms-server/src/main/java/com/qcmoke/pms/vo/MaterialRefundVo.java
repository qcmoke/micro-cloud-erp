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
}

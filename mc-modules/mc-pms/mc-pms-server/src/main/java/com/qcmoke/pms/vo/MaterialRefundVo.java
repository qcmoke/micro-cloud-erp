package com.qcmoke.pms.vo;

import com.qcmoke.pms.entity.MaterialRefund;
import com.qcmoke.pms.entity.PurchaseOrderMaster;
import com.qcmoke.ums.vo.UserVo;
import lombok.Data;

/**
 * @author qcmoke
 */
@Data
public class MaterialRefundVo extends MaterialRefund {
    private UserVo createUser;
    private UserVo checkUser;
    private String statusInfo;
    private String refundChannelInfo;
    private String checkStatusInfo;
    private PurchaseOrderMaster purchaseOrderMaster;
}

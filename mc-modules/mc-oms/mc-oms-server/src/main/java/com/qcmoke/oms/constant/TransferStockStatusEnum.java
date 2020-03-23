package com.qcmoke.oms.constant;

import lombok.Getter;

/**
 * @author qcmoke
 */
@Getter
public enum TransferStockStatusEnum {
    /**
     * 移交库存状态(1:未移交；2:已移交申请；3:移交失败；3:已完成移交(仓库审核成功的情况)；)
     */
    NO_TRANSFER(1, "未移交"),
    ALREADY_TRANSFER(2, "已移交申请"),
    FAIL(3, "移交失败"),
    FINISH(4, "已完成移交");


    private final int status;
    private final String info;

    TransferStockStatusEnum(Integer status, String info) {
        this.status = status;
        this.info = info;
    }
}

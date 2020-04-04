package com.qcmoke.oms.constant;

import lombok.Getter;

/**
 * @author qcmoke
 */
@Getter
public enum StockCheckStatusEnum {
    /**
     * 仓库审核状态（1:未提交申请；2:已申请未审核；3：审核不通过；4：审核通过）
     */
    NO_TRANSFER(1, "未提交申请"),
    ALREADY_TRANSFER(2, "已申请未审核"),
    FAIL(3, "审核不通过"),
    FINISH(4, "审核通过");


    private final int status;
    private final String info;

    StockCheckStatusEnum(Integer status, String info) {
        this.status = status;
        this.info = info;
    }
}

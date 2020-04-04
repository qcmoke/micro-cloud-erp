package com.qcmoke.pms.constant;

import lombok.Getter;

/**
 * @author qcmoke
 */
@Getter
public enum StockCheckStatusEnum {
    /**
     * 仓库审核状态（1:未提交申请；2:已申请未审核；3：审核不通过；4：审核通过）
     */
    NO_APPLY(1, "未提交申请"),
    APPLIED_BUT_NO_CHECK(2, "已申请未审核"),
    FAIL(3, "审核不通过"),
    PASS(4, "审核通过");


    private final int status;
    private final String info;

    StockCheckStatusEnum(Integer status, String info) {
        this.status = status;
        this.info = info;
    }
}

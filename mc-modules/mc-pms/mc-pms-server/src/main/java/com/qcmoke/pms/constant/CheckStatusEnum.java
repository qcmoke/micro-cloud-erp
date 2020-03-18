package com.qcmoke.pms.constant;

import lombok.Getter;

/**
 * @author qcmoke
 */
@Getter
public enum CheckStatusEnum {
    /**
     * 《入库状态》
     * 1:未提交入库申请
     * 2:已提交申请但未审核
     * 3:审核不通过
     * 4:审核通过
     */
    NO_APPLY(1, "未提交入库申请"),
    APPLY_BUT_NO_CHECK(2, "已提交申请但未审核"),
    NO_PASS(3, "审核不通过"),
    PASS(4, "审核通过");


    private final int status;
    private final String info;

    CheckStatusEnum(Integer status, String info) {
        this.status = status;
        this.info = info;
    }

}

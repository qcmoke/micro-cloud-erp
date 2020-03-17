package com.qcmoke.pms.constant;

import lombok.Getter;

/**
 * @author qcmoke
 */
@Getter
public enum WarehouseStatusEnum {
    /**
     * 《入库状态》
     * 0:未提交入库申请
     * 1:已提交申请但未审核
     * 2:审核不通过
     * 3:审核通过但未入库
     * 4:审核通过并已入库
     */
    NO_APPLY(0, "未提交入库申请"),
    APPLY_BUT_NO_CHECK(1, "已提交申请但未审核"),
    NO_PASS(2, "审核不通过"),
    PASS_BUT_NO_STOCKED(3, "审核通过但未入库"),
    PASS_AND_STOCKED(4, "审核通过并已入库");

    private final int status;
    private final String info;

    WarehouseStatusEnum(Integer status, String info) {
        this.status = status;
        this.info = info;
    }

}

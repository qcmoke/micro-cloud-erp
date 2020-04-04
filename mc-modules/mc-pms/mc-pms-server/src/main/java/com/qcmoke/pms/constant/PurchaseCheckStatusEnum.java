package com.qcmoke.pms.constant;

/**
 * @author qcmoke
 */
public enum PurchaseCheckStatusEnum {
    /**
     * 采购审批状态（1:未提交申请；2:已提交申请但未审核；3:审核不通过；4:审核通过）
     * 由采购员提交申请；采购管理员审批
     */
    NO_APPLY(1, "未提交申请"),
    APPLIED_BUT_NO_CHECK(2, "已申请未审核"),
    FAIL(3, "审核不通过"),
    PASS(4, "审核通过");


    private final int value;
    private final String info;

    public int value() {
        return value;
    }

    public String info() {
        return info;
    }

    PurchaseCheckStatusEnum(Integer value, String info) {
        this.value = value;
        this.info = info;
    }
}

package com.qcmoke.wms.constant;

/**
 * @author qcmoke
 */

public enum  CheckStatusEnum {

    /**
     * 库存管理员审核状态（1:未审核(默认)；2:审核不通过；3:审核通过）
     */
    NO_REVIEWED(1, "未审核(默认)"),
    NO_PASS(2, "审核不通过"),
    PASS(3, "审核通过");

    private final int value;
    private final String info;

    CheckStatusEnum(Integer value, String info) {
        this.value = value;
        this.info = info;
    }

    public int value() {
        return this.value;
    }

    public String info() {
        return this.info;
    }
}

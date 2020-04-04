package com.qcmoke.oms.constant;

/**
 * @author qcmoke
 */
public enum OutStatusEnum {

    /**
     * 发货状态（1：未发货；2：已发货）
     */
    NOT_SHIPPED(1, "未发货"),
    SHIPPED(2, "已发货");

    private final int value;
    private final String info;

    OutStatusEnum(Integer value, String info) {
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

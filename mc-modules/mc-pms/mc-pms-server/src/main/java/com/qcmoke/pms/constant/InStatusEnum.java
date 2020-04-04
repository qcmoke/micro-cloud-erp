package com.qcmoke.pms.constant;

/**
 * @author qcmoke
 */
public enum InStatusEnum {

    /**
     * 收货入库状态（1：未入库；2：已入库）
     */
    NOT_SHIPPED(1, "未入库"),
    SHIPPED(2, "已入库");

    private final int value;
    private final String info;

    InStatusEnum(Integer value, String info) {
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

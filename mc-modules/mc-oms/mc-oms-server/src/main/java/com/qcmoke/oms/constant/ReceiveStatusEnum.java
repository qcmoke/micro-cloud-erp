package com.qcmoke.oms.constant;

/**
 * @author qcmoke
 */
public enum ReceiveStatusEnum {

    /**
     * 客户收货状态（1：未收货；2：已收货）
     */
    UN_FINISHED(1, "未收货"),
    FINISHED(2, "已收货");

    private final int value;
    private final String info;

    ReceiveStatusEnum(Integer value, String info) {
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

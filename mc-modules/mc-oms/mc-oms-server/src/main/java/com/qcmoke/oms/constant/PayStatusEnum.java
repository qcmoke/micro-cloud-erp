package com.qcmoke.oms.constant;

/**
 * @author qcmoke
 */
public enum PayStatusEnum {

    /**
     * 支付状态【1:未支付；2:已支付】
     */
    NO_PAID(1, "未支付"),
    PAID(2, "已支付");

    private final int value;
    private final String info;

    PayStatusEnum(Integer value, String info) {
        this.value = value;
        this.info = info;
    }

    public int value() {
        return this.value;
    }

    public String info() {
        return this.info;
    }

    public boolean isPaid() {
        return this.value == PAID.value;
    }

    public boolean isNoPaid() {
        return !isPaid();
    }
}

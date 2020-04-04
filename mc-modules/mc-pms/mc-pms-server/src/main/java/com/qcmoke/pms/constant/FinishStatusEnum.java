package com.qcmoke.pms.constant;

/**
 * @author qcmoke
 */
public enum FinishStatusEnum {

    /**
     * 完成状态（1：未完成；2：已完成）
     */
    UN_FINISHED(1, "未完成"),
    FINISHED(2, "已完成");

    private final int value;
    private final String info;

    FinishStatusEnum(Integer value, String info) {
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

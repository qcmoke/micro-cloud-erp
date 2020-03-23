package com.qcmoke.wms.constant;

/**
 * @author qcmoke
 */
public enum FinishStatusEnum {
    /**
     * 出入库完成状态
     */
    NO_FINISHED(1, "未完成"),
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

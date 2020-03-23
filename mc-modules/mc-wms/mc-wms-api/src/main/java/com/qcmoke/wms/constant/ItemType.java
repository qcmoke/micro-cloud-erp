package com.qcmoke.wms.constant;

/**
 * @author qcmoke
 */

public enum ItemType {

    /**
     *
     */
    MATERIAL(1, "物料"),
    PRODUCT(2, "产品");

    private final int value;
    private final String info;

    ItemType(Integer value, String info) {
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

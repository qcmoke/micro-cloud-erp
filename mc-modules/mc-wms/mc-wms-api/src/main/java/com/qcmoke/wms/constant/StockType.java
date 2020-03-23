package com.qcmoke.wms.constant;

import com.qcmoke.common.exception.GlobalCommonException;

/**
 * @author qcmoke
 */

public enum StockType {

    /**
     * 出入库类型(1:采购入库；2:销售出库；3:采购退货出库；4:销售退货入库)
     */
    PURCHASE_IN(1, "采购入库"),
    SALE_OUT(2, "销售出库"),
    PURCHASE_OUT(3, "采购退货出库"),
    SALE_IN(4, "销售退货入库");


    private final int value;
    private final String info;

    StockType(Integer value, String info) {
        this.value = value;
        this.info = info;
    }

    public int value() {
        return this.value;
    }

    public String info() {
        return this.info;
    }


    public static StockType valueOf(int value) {
        return resolve(value);
    }

    public static StockType resolve(int value) {
        StockType[] var1 = values();
        for (StockType stockType : var1) {
            if (stockType.value == value) {
                return stockType;
            }
        }
        throw new GlobalCommonException("操作异常");
    }
}

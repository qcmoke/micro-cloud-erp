package com.qcmoke.fms.constant;

import com.qcmoke.common.exception.GlobalCommonException;

/**
 * 账目类型
 * @author qcmoke
 */

public enum DealType  {
    /**
     * 账目类型(1:采购付款；2:采购退货收款；3:销售收款；4:销售退款)
     */
    PURCHASE_OUT(1, "采购付款"),
    PURCHASE_IN(2, "采购退货收款"),
    SALE_IN(3, "销售收款"),
    SALE_OUT(4, "销售退款");


    private final int value;
    private final String info;

    DealType(Integer value, String info) {
        this.value = value;
        this.info = info;
    }

    public int value() {
        return this.value;
    }

    public String info() {
        return this.info;
    }


    public static DealType valueOf(int value) {
        return resolve(value);
    }

    public static DealType resolve(int value) {
        DealType[] var1 = values();
        for (DealType stockType : var1) {
            if (stockType.value == value) {
                return stockType;
            }
        }
        throw new GlobalCommonException("操作异常");
    }
}

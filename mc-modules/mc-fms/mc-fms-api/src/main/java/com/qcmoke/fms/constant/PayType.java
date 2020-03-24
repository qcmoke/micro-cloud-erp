package com.qcmoke.fms.constant;

import com.qcmoke.common.exception.GlobalCommonException;

/**
 * @author qcmoke
 */

public enum PayType {

    /**
     * 支付方式【1->支付宝；2->微信；3->银联；】
     */
    ALI_PAY(1, "支付宝账户"),
    WEI_PAY(2, "微信账户"),
    UNION_PAY(3, "银联账户");
    private final int value;
    private final String info;

    PayType(Integer value, String info) {
        this.value = value;
        this.info = info;
    }

    public int value() {
        return this.value;
    }


    public static PayType valueOf(int value) {
        return resolve(value);
    }

    public static PayType resolve(int value) {
        PayType[] var1 = values();
        for (PayType stockType : var1) {
            if (stockType.value == value) {
                return stockType;
            }
        }
        throw new GlobalCommonException("操作异常");
    }


}

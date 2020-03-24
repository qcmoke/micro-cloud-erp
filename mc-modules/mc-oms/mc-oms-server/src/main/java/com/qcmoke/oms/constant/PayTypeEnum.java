package com.qcmoke.oms.constant;

/**
 * @author qcmoke
 */

public enum PayTypeEnum {

    /**
     * 支付方式【1->支付宝；2->微信；3->银联；】
     */
    ALI_PAY(1, "支付宝"),
    WEI_PAY(2, "微信"),
    UNION_PAY(3, "银联");
    private final int value;
    private final String info;

    PayTypeEnum(Integer value, String info) {
        this.value = value;
        this.info = info;
    }

    public int value() {
        return this.value;
    }


}

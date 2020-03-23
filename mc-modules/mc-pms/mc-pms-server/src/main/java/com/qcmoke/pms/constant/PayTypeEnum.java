package com.qcmoke.pms.constant;

/**
 * @author qcmoke
 */

public enum PayTypeEnum {

    /**
     * 支付方式【1->支付宝；2->微信；3->银联； 4->货到付款；】
     */
    ALI_PAY(1, "支付宝"),
    WEI_PAY(2, "微信"),
    UNION_PAY(3, "银联"),
    CASH_ON_DELIVERY(4, "货到付款");
    private final int value;
    private final String info;

    PayTypeEnum(Integer value, String info) {
        this.value = value;
        this.info = info;
    }

    public int value() {
        return this.value;
    }


    public static boolean contains(int payType) {
        for (PayTypeEnum payTypeEnum : PayTypeEnum.values()) {
            if (payTypeEnum.value() == payType) {
                return true;
            }
        }
        return false;
    }

    public static boolean notContains(int payType) {
        return !contains(payType);
    }
}

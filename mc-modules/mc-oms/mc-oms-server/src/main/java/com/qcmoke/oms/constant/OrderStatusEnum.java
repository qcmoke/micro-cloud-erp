package com.qcmoke.oms.constant;

import com.qcmoke.common.exception.GlobalCommonException;
import lombok.Getter;

/**
 * @author qcmoke
 */
@Getter
public enum OrderStatusEnum {
    /**
     * 订单状态【1->待付款；2->待发货；3->已发货；4->未确认收货；5->已确认收货；6->已完成；7->已关闭；-1->无效订单】
     */
    NO_PAY(1, "待付款"),
    PAID_NO_SHIPPED(2, "已支付未发货"),
    SHIPPED(3, "已发货"),
    SHIPPED_NO_RECEIVED(4, "已发货未确认收货"),
    RECEIVED(5, "已确认收货"),
    FINISHED(6, "已完成"),
    CLOSE(7, "已关闭"),
    INVALID_ORDER(-1, "无效订单");

    private final int value;
    private final String info;

    OrderStatusEnum(Integer value, String info) {
        this.value = value;
        this.info = info;
    }

    public int value() {
        return this.value;
    }

    public String info() {
        return this.info;
    }

    public static OrderStatusEnum valueOf(int statusCode) {
        return resolve(statusCode);
    }

    public static OrderStatusEnum resolve(int statusCode) {
        OrderStatusEnum[] var1 = values();
        for (OrderStatusEnum status : var1) {
            if (status.value == statusCode) {
                return status;
            }
        }
        throw new GlobalCommonException("操作异常");
    }


    /**
     * 将要修改的状态是按步骤操作
     */
    public boolean isFollowTheSteps(OrderStatusEnum currentOrderStatus) {
        return this.value == currentOrderStatus.value + 1;
    }

    public boolean isNotFollowTheSteps(OrderStatusEnum currentOrderStatus) {
        return !isFollowTheSteps(currentOrderStatus);
    }


    public static boolean isLessAndEqOther(OrderStatusEnum anEnum, OrderStatusEnum other) {
        return anEnum.value <= other.value;
    }

    public static boolean isMoreAndEqOther(OrderStatusEnum anEnum, OrderStatusEnum other) {
        return anEnum.value >= other.value;
    }
}

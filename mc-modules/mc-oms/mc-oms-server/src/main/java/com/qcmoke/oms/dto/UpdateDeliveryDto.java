package com.qcmoke.oms.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author qcmoke
 */
@Data
public class UpdateDeliveryDto implements Serializable {

    /**
     * 是否已经收货
     */
    private Boolean isReceived;

    /**
     * 销售订单主表编号
     */
    private Long masterId;

    /**
     * 收货人确认收货时间
     */
    private Date receiveTime;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人电话号码
     */
    private String receiverPhone;

    /**
     * 收货人所在详细地址
     */
    private String receiverDetailAddress;

}

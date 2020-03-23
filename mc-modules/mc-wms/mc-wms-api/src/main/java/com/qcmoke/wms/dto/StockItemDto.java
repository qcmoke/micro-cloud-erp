package com.qcmoke.wms.dto;

import com.qcmoke.wms.constant.ItemType;
import com.qcmoke.wms.constant.StockType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author qcmoke
 */
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class StockItemDto implements Serializable {

    /**
     * 订单编号(必填)
     */
    private Long orderId;


    /**
     * (必填)
     * 出入库类型(1:入库；2:出库)
     */
    private StockType stockType;


    /**
     * (必填)
     * 货物类型（1:物料；2:产品）
     */
    private ItemType itemType;

    /**
     * 备注
     * （可选）
     */
    private String remark;

    /**
     * 明细
     * (必填)
     */
    private List<StockItemDetailDto> stockItemDetailDtoList;

}

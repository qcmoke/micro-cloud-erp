package com.qcmoke.wms.vo;

import com.qcmoke.wms.entity.StockItem;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author qcmoke
 */
@Data
public class StockItemVo extends StockItem {

    private List<StockItemDetailVo> stockItemDetailVos;

    /**
     * 出入库类型(1:入库；2:出库)
     */
    private String stockTypeInfo;

    /**
     * 货物类型（1:物料；2:产品）
     */
    private String itemTypeInfo;

    /**
     * 库存管理员
     */
    private String adminName;

    /**
     * 交接申请用户
     */
    private String applyUserName;

    /**
     * 库存管理员审核状态（1:未审核；2:审核不通过；3:审核通过）
     */
    private String checkStatusInfo;

    /**
     * 完成出入库状态（1:未完成；2:已完成）
     */
    private String finishStatusInfo;


}

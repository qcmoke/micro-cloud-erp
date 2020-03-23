package com.qcmoke.wms.vo;

import com.qcmoke.wms.entity.StockItemDetail;
import lombok.Data;

/**
 * @author qcmoke
 */
@Data
public class StockItemDetailVo extends StockItemDetail {
    /**
     * 商品类型
     */
    private Integer itemType;
    /**
     * 货物名称
     */
    private String itemName;
    /**
     * 货物库存量
     */
    private Double itemCount;

    /**
     * 货物图片
     */
    private String img;

    /**
     * 货物单位
     */
    private String unit;

    /**
     * 货物规格
     */
    private String standard;
}

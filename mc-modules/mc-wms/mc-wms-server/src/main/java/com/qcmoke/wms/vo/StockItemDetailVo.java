package com.qcmoke.wms.vo;

import com.qcmoke.wms.entity.StockItemDetail;
import lombok.Data;

/**
 * @author qcmoke
 */
@Data
public class StockItemDetailVo extends StockItemDetail {
    private String materialName;
}

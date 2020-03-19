package com.qcmoke.wms.vo;

import com.qcmoke.wms.entity.Stock;
import lombok.Data;

/**
 * @author qcmoke
 */
@Data
public class StockVo extends Stock {
    private String itemName;
}
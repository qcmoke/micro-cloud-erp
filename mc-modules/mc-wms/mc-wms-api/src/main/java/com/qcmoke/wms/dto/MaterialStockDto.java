package com.qcmoke.wms.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qcmoke
 */

@Data
public class MaterialStockDto implements Serializable {
    /**
     * 原料编号
     */
    private Long materialId;

    /**
     * 原料名称
     */
    private String materialName;

    /**
     * 数量
     */
    private Double count;
}

package com.qcmoke.oms.vo;

import com.qcmoke.oms.entity.Product;
import com.qcmoke.oms.entity.SaleOrderDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author qcmoke
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SaleOrderDetailVo extends SaleOrderDetail {
    /**
     * 产品编号
     */
    private String productName;

    private Product product;
}

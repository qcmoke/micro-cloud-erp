package com.qcmoke.pms.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qcmoke.pms.entity.PurchaseOrderMaster;
import com.qcmoke.pms.entity.Supplier;
import lombok.Data;

import java.util.Set;

/**
 * @author qcmoke
 */
@Data
@JsonIgnoreProperties(value = {"handler"}) //避免使用懒加载时返回转换handler成json报错的问题
public class PurchaseOrderMasterVo extends PurchaseOrderMaster {
    /**
     * 供应商id
     */
    private Supplier supplier;

    /**
     * 对应的订单明细
     */
    private Set<PurchaseOrderDetailVo> purchaseOrderDetailVoSet;


    /**
     * 操作员id
     */
    private String operator;
    
}

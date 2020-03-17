package com.qcmoke.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qcmoke.pms.entity.PurchaseOrderDetail;
import com.qcmoke.pms.vo.PurchaseOrderDetailVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-12
 */
public interface PurchaseOrderDetailService extends IService<PurchaseOrderDetail> {

    List<PurchaseOrderDetailVo> getListByMasterId(Long masterId);
}

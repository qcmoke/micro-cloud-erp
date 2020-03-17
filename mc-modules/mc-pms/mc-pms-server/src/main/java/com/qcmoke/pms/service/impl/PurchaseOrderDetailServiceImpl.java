package com.qcmoke.pms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.pms.entity.PurchaseOrderDetail;
import com.qcmoke.pms.mapper.PurchaseOrderDetailMapper;
import com.qcmoke.pms.service.PurchaseOrderDetailService;
import com.qcmoke.pms.vo.PurchaseOrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-12
 */
@Service
public class PurchaseOrderDetailServiceImpl extends ServiceImpl<PurchaseOrderDetailMapper, PurchaseOrderDetail> implements PurchaseOrderDetailService {


    @Autowired
    private PurchaseOrderDetailMapper purchaseOrderDetailMapper;
    @Override
    public List<PurchaseOrderDetailVo> getListByMasterId(Long masterId) {
        return purchaseOrderDetailMapper.getListByMasterId(masterId);
    }
}

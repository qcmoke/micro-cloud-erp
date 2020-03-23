package com.qcmoke.pms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qcmoke.common.vo.PageResult;
import com.qcmoke.pms.dto.PurchaseOrderMasterDto;
import com.qcmoke.pms.entity.PurchaseOrderMaster;
import com.qcmoke.pms.vo.PurchaseOrderMasterVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-12
 */
public interface PurchaseOrderMasterService extends IService<PurchaseOrderMaster> {

    PageResult<PurchaseOrderMasterVo> getPage(Page<PurchaseOrderMaster> page, PurchaseOrderMaster purchaseOrderMaster);

    void transferToStock(Long masterId, Long currentUserId);


    PageResult<PurchaseOrderMasterVo> pageForAddStock(Page<PurchaseOrderMaster> page, PurchaseOrderMaster purchaseOrderMaster);

    void createOrUpdatePurchaseOrder(PurchaseOrderMasterDto purchaseOrderMasterDto, Long currentUserId);

    void deleteByIdList(List<Long> idList);

    void successForInItemToStock(List<Long> masterIdList);

    void checkCallBackForCreateStockPreReview(Long orderId, boolean isOk);

    boolean checkPass(Long masterId);

    boolean checkFail(Long masterId);

    boolean toApplyCheck(Long masterId);
}
